package com.spring.dlearning.controller.admin;

import com.spring.dlearning.dto.response.admin.AdminUserResponse;
import com.spring.dlearning.dto.response.admin.TeacherApplicationDetailResponse;
import com.spring.dlearning.service.admin.AdminUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final AdminUserService userService;

    @GetMapping
    public ResponseEntity<Page<AdminUserResponse>> getAllUsers(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        // Điều chỉnh page để Spring bắt đầu từ 0 (page - 1)
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminUserResponse> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<AdminUserResponse>> searchUsersByKeywords(
            @RequestParam String keywords,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name,asc") String[] sort) {

        String[] keywordArray = keywords.split("\\s+");
        Pageable pageable = PageRequest.of(page - 1, size, getSortOrder(sort));
        Page<AdminUserResponse> users = userService.searchUsersByKeywords(keywordArray, pageable);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/ban/{userId}")
    public ResponseEntity<String> banUser(@PathVariable Long userId) {
        userService.banUser(userId);
        return ResponseEntity.ok("User banned successfully");
    }

    @PostMapping("/unban/{userId}")
    public ResponseEntity<String> unbanUser(@PathVariable Long userId) {
        userService.unbanUser(userId);
        return ResponseEntity.ok("User unbanned successfully");
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<String> updateRole(
            @PathVariable Long userId,
            @RequestParam String roleName) {
        userService.updateUserRole(userId, roleName);
        return ResponseEntity.ok("User role updated successfully");
    }

    @GetMapping("/{userId}/details")
    public ResponseEntity<TeacherApplicationDetailResponse> getUserApplicationDetail(@PathVariable Long userId) {
        TeacherApplicationDetailResponse details = userService.getUserApplicationDetail(userId);
        return ResponseEntity.ok(details);
    }

    private Sort getSortOrder(String[] sort) {
        String sortBy = sort.length > 0 ? sort[0] : "name";
        String sortDir = sort.length > 1 ? sort[1] : "asc";

        Sort.Direction direction = sortDir.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        return Sort.by(direction, sortBy);
    }
}
