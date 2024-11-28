package com.spring.dlearning.service;

import com.spring.dlearning.constant.PredefinedRole;
import com.spring.dlearning.dto.request.IsCourseCompleteRequest;
import com.spring.dlearning.dto.response.BuyCourseResponse;
import com.spring.dlearning.dto.response.CoursePurchaseResponse;
import com.spring.dlearning.dto.response.IsCourseCompleteResponse;
import com.spring.dlearning.entity.Course;
import com.spring.dlearning.entity.Enrollment;
import com.spring.dlearning.entity.User;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.mapper.EnrollmentMapper;
import com.spring.dlearning.repository.CourseRepository;
import com.spring.dlearning.repository.EnrollmentRepository;
import com.spring.dlearning.repository.UserRepository;
import com.spring.dlearning.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EnrollmentService {

    EnrollmentRepository enrollmentRepository;
    UserRepository userRepository;
    CourseRepository courseRepository;
    EnrollmentMapper enrollmentMapper;

    @PreAuthorize("isAuthenticated()")
    public List<BuyCourseResponse> getCourseByUserCurrent(){
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        List<Enrollment> enrollments = enrollmentRepository.findCourseByUser(user);

        return enrollments.stream().map(enrollmentMapper::toBuyCourseResponse).toList();
    }

    @PreAuthorize("isAuthenticated()")
    public CoursePurchaseResponse checkPurchase (Long courseId) {
        if ( courseId <= 0 ){
            throw new AppException(ErrorCode.INVALID_PATH_VARIABLE_ID);
        }
        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User userCurrent = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        if(Objects.equals(userCurrent.getRole().getName(), PredefinedRole.ADMIN_ROLE)){
            return CoursePurchaseResponse.builder()
                    .userId(userCurrent.getId())
                    .courseId(courseId)
                    .purchased(true)
                    .build();
        }

        Optional<Enrollment> enrollment = enrollmentRepository.checkPurchase(userCurrent, course);

        if(enrollment.isEmpty()){
            return CoursePurchaseResponse.builder()
                    .userId(userCurrent.getId())
                    .courseId(courseId)
                    .purchased(false)
                    .build();
        }

        return enrollmentMapper.toCoursePurchaseResponse(enrollment.get());
    }

    public IsCourseCompleteResponse isCompleteCourse (IsCourseCompleteRequest request) {
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        String email = SecurityUtils.getCurrentUserLogin()
                .orElseThrow(() -> new AppException(ErrorCode.EMAIL_INVALID));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        return IsCourseCompleteResponse.builder()
                .isComplete(enrollmentRepository.isCourseCompleteByUser(user, course))
                .build();
    }
}
