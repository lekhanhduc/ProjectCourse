package com.spring.dlearning.service.admin;

import com.spring.dlearning.common.RegistrationStatus;
import com.spring.dlearning.constant.PredefinedRole;
import com.spring.dlearning.dto.response.admin.AdminTeacherResponse;
import com.spring.dlearning.dto.response.admin.AdminUserResponse;
import com.spring.dlearning.entity.Role;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.mapper.admin.AdminUserMapper;
import com.spring.dlearning.mapper.admin.AdminTeacherMapper;
import com.spring.dlearning.repository.RoleRepository;
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
public class AdminTeacherService {

    UserRepository userRepository;
    AdminTeacherMapper teacherMapper;
    RoleRepository roleRepository;
    AdminUserMapper userMapper;

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public Page<AdminTeacherResponse> getAllTeachers(Pageable pageable) {
        return userRepository.findByRoleName(PredefinedRole.TEACHER_ROLE, pageable)
                .map(teacherMapper::toTeacherResponse);
    }

    public Page<AdminTeacherResponse> searchTeachersByKeywords(String[] keywords, Pageable pageable) {
        return userRepository.searchByRoleAndMultipleKeywords(PredefinedRole.TEACHER_ROLE, keywords, pageable)
                .map(teacherMapper::toTeacherResponse);
    }

    @Transactional
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public void removeTeacherRole(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Role userRole = roleRepository.findByName(PredefinedRole.USER_ROLE)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_EXISTED));

        user.setRole(userRole);
        user.setRegistrationStatus(null);
        userRepository.save(user);
    }

    public Page<AdminUserResponse> getPendingTeacherApplications(Pageable pageable) {
        return userRepository.findByRoleNameAndRegistrationStatus(PredefinedRole.USER_ROLE, RegistrationStatus.PENDING, pageable)
                .map(userMapper::toUserResponse);
    }

}
