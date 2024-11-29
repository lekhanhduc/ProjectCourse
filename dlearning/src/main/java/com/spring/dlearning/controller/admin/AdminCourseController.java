package com.spring.dlearning.controller.admin;

import com.spring.dlearning.dto.response.admin.AdminCourseResponse;
import com.spring.dlearning.service.admin.AdminCourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/courses")
@RequiredArgsConstructor
public class AdminCourseController {

    private final AdminCourseService courseService;

    @GetMapping
    public ResponseEntity<Page<AdminCourseResponse>> getAllCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminCourseResponse> courses = courseService.getAllCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AdminCourseResponse>> searchCourses(
            @RequestParam String keywords,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String[] sort) {

        String[] keywordArray = keywords.split("\\s+");
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminCourseResponse> courses = courseService.searchCoursesByKeywords(keywordArray, pageable);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/{courseId}/ban")
    public ResponseEntity<String> banCourse(@PathVariable Long courseId) {
        courseService.banCourse(courseId);
        return ResponseEntity.ok("Course banned successfully.");
    }

    @PostMapping("/{courseId}/unban")
    public ResponseEntity<String> unbanCourse(@PathVariable Long courseId) {
        courseService.unbanCourse(courseId);
        return ResponseEntity.ok("Course unbanned successfully.");
    }

    @GetMapping("/banned")
    public ResponseEntity<Page<AdminCourseResponse>> getBannedCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminCourseResponse> courses = courseService.getBannedCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/active")
    public ResponseEntity<Page<AdminCourseResponse>> getActiveCourses(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "title,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminCourseResponse> courses = courseService.getActiveCourses(pageable);
        return ResponseEntity.ok(courses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminCourseResponse> getCourseDetails(@PathVariable Long id) {
        AdminCourseResponse response = courseService.getCourseDetails(id);
        return ResponseEntity.ok(response);
    }

    private Sort getSortOrder(String[] sort) {
        String sortBy = sort.length > 0 ? sort[0] : "title";
        String sortDir = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }
}