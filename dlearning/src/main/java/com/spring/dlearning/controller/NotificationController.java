package com.spring.dlearning.controller;

import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.entity.Notification;
import com.spring.dlearning.service.NotificationService;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
@Slf4j
public class NotificationController {

    NotificationService notificationService;

    @GetMapping("/notification-current")
    ApiResponse<List<Notification>> getCurrentNotification() {
        var result = notificationService.getNotificationsForCurrentUser();

        return ApiResponse.<List<Notification>>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @DeleteMapping("/notification/{id}")
    ApiResponse<Boolean> deleteNotification(@PathVariable @Min(1) Long id) {
        try {
            notificationService.deleteNotification(id);
            return ApiResponse.<Boolean>builder()
                    .code(HttpStatus.OK.value())
                    .result(true)
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.<Boolean>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .result(false)
                    .build();
        }
    }

    @PutMapping("/is-read/{id}")
    ApiResponse<Boolean> markAllAsRead(@PathVariable @Min(1) Long id) {
        try {
            notificationService.markAsRead(id);
            return ApiResponse.<Boolean>builder()
                    .code(HttpStatus.OK.value())
                    .build();
        } catch (Exception e) {
            log.error(e.getMessage());
            return ApiResponse.<Boolean>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
        }
    }

}
