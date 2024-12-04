package com.spring.dlearning.controller;

import com.spring.dlearning.dto.request.WithdrawalRequest;
import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.PageResponse;
import com.spring.dlearning.dto.response.WithdrawalHistoryResponse;
import com.spring.dlearning.dto.response.WithdrawalResponse;
import com.spring.dlearning.dto.response.admin.AdminUserResponse;
import com.spring.dlearning.entity.WithdrawalHistory;
import com.spring.dlearning.service.WithdrawalService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
public class WithdrawalController {

    WithdrawalService withdrawalService;

    @GetMapping("/get-withdrawal")
    public ApiResponse<PageResponse<WithdrawalHistoryResponse>> getWithdrawal(
            @RequestParam("page") Optional<Integer> pageOptional,
            @RequestParam("size") Optional<Integer> sizeOptional
    ) {
        int page = pageOptional.orElse(1);
        int size = sizeOptional.orElse(3);

        return ApiResponse.<PageResponse<WithdrawalHistoryResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(withdrawalService.getWithdrawalHistory(page, size))
                .build();
    }

//    @GetMapping("get-all-withdrawal")
//    public ApiResponse<List<WithdrawalHistoryResponse>> getAllWithdrawal() {
//        return ApiResponse.<List<WithdrawalHistoryResponse>>builder()
//                .code(HttpStatus.OK.value())
//                .result(withdrawalService.getAllWithDrawalHistory())
//                .build();
//    }

    @GetMapping("get-all-withdrawal")
    public ResponseEntity<Page<WithdrawalHistoryResponse>> getAllWithdrawal(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "user.name,asc", required = false)  String[] sort) {

        // Điều chỉnh page để Spring bắt đầu từ 0 (page - 1)
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<WithdrawalHistoryResponse> users = withdrawalService.getAllWithDrawalHistory(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("get-processing-withdrawal")
    public ResponseEntity<Page<WithdrawalHistoryResponse>> getProcessingWithdrawal(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "user.name,asc", required = false)  String[] sort) {

        // Điều chỉnh page để Spring bắt đầu từ 0 (page - 1)
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<WithdrawalHistoryResponse> users = withdrawalService.getProcessingWithdrawal(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("get-cancelled-withdrawal")
    public ResponseEntity<Page<WithdrawalHistoryResponse>> getCancelledWithdrawal(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "user.name,asc", required = false)  String[] sort) {

        // Điều chỉnh page để Spring bắt đầu từ 0 (page - 1)
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<WithdrawalHistoryResponse> users = withdrawalService.getCancelledWithdrawal(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("get-completed-withdrawal")
    public ResponseEntity<Page<WithdrawalHistoryResponse>> getCompletedWithdrawal(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "user.name,asc", required = false)  String[] sort) {

        // Điều chỉnh page để Spring bắt đầu từ 0 (page - 1)
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<WithdrawalHistoryResponse> users = withdrawalService.getCompletedWithdrawal(pageable);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/add-withdrawal")
    public ApiResponse<WithdrawalResponse> addWithdrawal(@RequestBody @Valid WithdrawalRequest withdrawalRequest) {
        return ApiResponse.<WithdrawalResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(withdrawalService.addWithdrawal(withdrawalRequest))
                .build();
    }

    @PostMapping("/accept-withdrawal/{id}")
    public ApiResponse<Void> acceptWithdrawal(@PathVariable Long id){
        withdrawalService.acceptWithdrawal(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .build();

    }

    @PostMapping("/cancel-withdrawal/{id}")
    public ApiResponse<Void> cancelWithdrawal(@PathVariable Long id){
        withdrawalService.cancelWithdrawal(id);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .build();

    }

    private Sort getSortOrder(String[] sort) {
        String sortBy = sort.length > 0 ? sort[0] : "name";
        String sortDir = sort.length > 1 ? sort[1] : "asc";

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }

}
