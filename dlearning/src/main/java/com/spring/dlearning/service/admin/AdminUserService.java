package com.spring.dlearning.service.admin;

import com.spring.dlearning.dto.response.admin.AdminUserResponse;
import com.spring.dlearning.dto.response.admin.TeacherApplicationDetailResponse;
import com.spring.dlearning.entity.Role;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.mapper.admin.AdminUserMapper;
import com.spring.dlearning.repository.RoleRepository;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminUserService {

    UserRepository userRepository;
    AdminUserMapper userMapper;
    RoleRepository roleRepository;

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public Page<AdminUserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponse);
    }

    public Page<AdminUserResponse> searchUsersByKeywords(String[] keywords, Pageable pageable) {
        return userRepository.searchByMultipleKeywords(keywords, pageable)
                .map(userMapper::toUserResponse);
    }

    @Transactional
    public void banUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setEnabled(false);
        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public void unbanUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public void updateUserRole(Long userId, String roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Role newRole = roleRepository.findByName(roleName)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        user.setRole(newRole);
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public TeacherApplicationDetailResponse getUserApplicationDetail(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return TeacherApplicationDetailResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .gender(user.getGender() != null ? user.getGender().toString() : null) // Kiểm tra null
                .avatar(user.getAvatar())
                .dob(user.getDob()) // Giữ nguyên LocalDate
                .cvUrl(user.getCvUrl())
                .certificate(user.getCertificate())
                .facebookLink(user.getFacebookLink())
                .description(user.getDescription())
                .yearsOfExperience(user.getYearsOfExperience())
                .points(user.getPoints())
                .role(user.getRole().getName())
                .build();
    }

}

