package com.spring.dlearning.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class AdminReviewDTO {
    private Long courseId;
    private Double averageRating;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updatedAt;
    private Long userId;
    private String userName;
    private String userAvatar;
    private String title;  // Thêm trường title
    private String thumbnail;  // Thêm trường thumbnail
}
