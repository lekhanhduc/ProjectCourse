package com.spring.dlearning.service;

import com.spring.dlearning.common.TransactionStatus;
import com.spring.dlearning.dto.request.WithdrawalRequest;
import com.spring.dlearning.dto.response.PageResponse;
import com.spring.dlearning.dto.response.WithdrawalHistoryResponse;
import com.spring.dlearning.dto.response.WithdrawalResponse;
import com.spring.dlearning.dto.response.admin.AdminCourseResponse;
import com.spring.dlearning.dto.response.admin.AdminUserResponse;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.entity.WithdrawalHistory;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.mapper.WithdrawalMapper;
import com.spring.dlearning.repository.UserRepository;
import com.spring.dlearning.repository.WithdrawalRepository;
import com.spring.dlearning.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class WithdrawalService {

    WithdrawalRepository withdrawalRepository;
    UserRepository userRepository;
    WithdrawalMapper withdrawalMapper;

    public PageResponse<WithdrawalHistoryResponse> getWithdrawalHistory(int page, int size){

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(Sort.Direction.DESC, "id"));

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Page<WithdrawalHistory> withdrawalHistory = withdrawalRepository.findAll(pageable);

        List<WithdrawalHistoryResponse> withdrawalHistoryResponses = withdrawalHistory.stream()
                .filter(withdrawl -> withdrawl.getUser().getId() == user.getId())
                .map(withdrawl -> withdrawalMapper.toWithdrawalHistoryResponse(withdrawl))
                .toList();

        return PageResponse.<WithdrawalHistoryResponse>builder()
                .currentPage(page)
                .pageSize(pageable.getPageSize())
                .totalPages(withdrawalHistory.getTotalPages())
                .totalElements(withdrawalHistory.getTotalElements())
                .data(withdrawalHistoryResponses)
                .build();
    }

//    public List<WithdrawalHistoryResponse> getAllWithDrawalHistory(){
//        List<WithdrawalHistory> withdrawalHistory = withdrawalRepository.findAll();
//
//        List<WithdrawalHistoryResponse> withdrawalHistoryResponses = withdrawalHistory.stream()
//                .map(withdrawl -> withdrawalMapper.toWithdrawalHistoryResponse(withdrawl))
//                .toList();
//
//        return withdrawalHistoryResponses;
//    }

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public Page<WithdrawalHistoryResponse> getAllWithDrawalHistory(Pageable pageable) {
        return withdrawalRepository.findAll(pageable).map(withdrawl -> withdrawalMapper.toWithdrawalHistoryResponse(withdrawl));
    }

    public Page<WithdrawalHistoryResponse> getCompletedWithdrawal(Pageable pageable) {
        return withdrawalRepository.findByStatus(TransactionStatus.COMPLETED, pageable)
                .map(withdrawalMapper::toWithdrawalHistoryResponse);
    }

    public Page<WithdrawalHistoryResponse> getProcessingWithdrawal(Pageable pageable) {
        return withdrawalRepository.findByStatus(TransactionStatus.PROCESSING, pageable)
                .map(withdrawalMapper::toWithdrawalHistoryResponse);
    }

    public Page<WithdrawalHistoryResponse> getCancelledWithdrawal(Pageable pageable) {
        return withdrawalRepository.findByStatus(TransactionStatus.CANCELLED, pageable)
                .map(withdrawalMapper::toWithdrawalHistoryResponse);
    }

    public WithdrawalResponse addWithdrawal(WithdrawalRequest withdrawalRequest) {

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        if (withdrawalRequest.getPoints() == null || withdrawalRequest.getPoints() <= 0 || withdrawalRequest.getPoints() > user.getPoints()) {
            throw new AppException(ErrorCode.POINT_INVALID);
        }

        if (withdrawalRequest.getBankNumber() == null || withdrawalRequest.getBankNumber().isEmpty()) {
            throw new AppException(ErrorCode.BANK_NUMBER_INVALID);
        }

        WithdrawalHistory withdrawalHistory = withdrawalMapper.toWithdrawalHistory(withdrawalRequest);
        withdrawalHistory.setUser(user);
        withdrawalHistory.setStatus(TransactionStatus.PROCESSING);
        withdrawalHistory.setMoney(withdrawalRequest.getPoints() * 1000);
        withdrawalRepository.save(withdrawalHistory);

        user.setPoints(user.getPoints() - withdrawalRequest.getPoints());
        userRepository.save(user);

        return withdrawalMapper.toWithdrawalResponse(withdrawalHistory);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public void acceptWithdrawal(Long id){
        WithdrawalHistory withdrawalHistory = withdrawalRepository.findById(id).get();

        withdrawalHistory.setStatus(TransactionStatus.COMPLETED);
        withdrawalRepository.save(withdrawalHistory);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public void cancelWithdrawal(Long id){
        WithdrawalHistory withdrawalHistory = withdrawalRepository.findById(id).get();
        withdrawalHistory.setStatus(TransactionStatus.CANCELLED);
        User user = withdrawalHistory.getUser();
        user.setPoints(user.getPoints() + withdrawalHistory.getPoints());
        withdrawalRepository.save(withdrawalHistory);
    }
}
