package com.spring.dlearning.controller.admin;

import com.spring.dlearning.dto.response.admin.AdminTeacherResponse;
import com.spring.dlearning.dto.response.admin.AdminUserResponse;
import com.spring.dlearning.service.admin.AdminTeacherService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/teachers")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminTeacherController {

    AdminTeacherService teacherService;

    @GetMapping
    public ResponseEntity<Page<AdminTeacherResponse>> getAllTeachers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminTeacherResponse> teachers = teacherService.getAllTeachers(pageable);
        return ResponseEntity.ok(teachers);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AdminTeacherResponse>> searchTeachersByKeywords(
            @RequestParam String keywords,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {
        String[] keywordArray = keywords.split("\\s+");
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminTeacherResponse> teachers = teacherService.searchTeachersByKeywords(keywordArray, pageable);
        return ResponseEntity.ok(teachers);
    }

    @PutMapping("/{userId}/remove-role")
    public ResponseEntity<String> removeTeacherRole(@PathVariable Long userId) {
        teacherService.removeTeacherRole(userId);
        return ResponseEntity.ok("User role updated to USER and registration status set to null successfully");
    }

    @GetMapping("/applications")
    public ResponseEntity<Page<AdminUserResponse>> getPendingTeacherApplications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminUserResponse> applications = teacherService.getPendingTeacherApplications(pageable);
        return ResponseEntity.ok(applications);
    }

    private Sort getSortOrder(String[] sort) {
        String sortBy = sort.length > 0 ? sort[0] : "name";
        String sortDir = sort.length > 1 ? sort[1] : "asc";
        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }
}
