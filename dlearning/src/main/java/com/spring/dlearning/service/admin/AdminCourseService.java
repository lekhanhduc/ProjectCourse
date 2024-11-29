package com.spring.dlearning.service.admin;

import com.spring.dlearning.dto.response.admin.AdminCourseResponse;
import com.spring.dlearning.entity.Course;
import com.spring.dlearning.exception.AppException;
import com.spring.dlearning.exception.ErrorCode;
import com.spring.dlearning.mapper.admin.AdminCourseMapper;
import com.spring.dlearning.repository.CourseRepository;
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
public class AdminCourseService {

    CourseRepository courseRepository;
    AdminCourseMapper courseMapper;

    public Page<AdminCourseResponse> getAllCourses(Pageable pageable) {
        return courseRepository.findAll(pageable)
                .map(courseMapper::toCourseResponse);
    }

    public Page<AdminCourseResponse> searchCoursesByKeywords(String[] keywords, Pageable pageable) {
        return courseRepository.searchByMultipleKeywords(keywords, pageable)
                .map(courseMapper::toCourseResponse);
    }

    @Transactional
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public void banCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        if (!course.getEnabled()) {
            throw new AppException(ErrorCode.COURSE_ALREADY_BANNED);
        }

        course.setEnabled(false);
        courseRepository.save(course);
    }

    @Transactional
    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public void unbanCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        course.setEnabled(true);
        courseRepository.save(course);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public Page<AdminCourseResponse> getBannedCourses(Pageable pageable) {
        return courseRepository.findByEnabled(false, pageable)
                .map(courseMapper::toCourseResponse);
    }

    @PreAuthorize("isAuthenticated() && hasAuthority('ADMIN')")
    public Page<AdminCourseResponse> getActiveCourses(Pageable pageable) {
        return courseRepository.findByEnabled(true, pageable)
                .map(courseMapper::toCourseResponse);
    }

    public AdminCourseResponse getCourseDetails(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.COURSE_NOT_EXISTED));

        return AdminCourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .enabled(course.getEnabled())
                .authorId(course.getAuthor() != null ? course.getAuthor().getId() : null) // Thêm authorId
                .authorName(course.getAuthor() != null ? course.getAuthor().getName() : null)
                .language(course.getLanguage())
                .level(course.getCourseLevel().toString())
                .duration(course.getDuration())
                .points(course.getPoints())
                .thumbnail(course.getThumbnail()) // Thêm trường này
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}


