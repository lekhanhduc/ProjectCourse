package com.spring.dlearning.dto.response.admin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminCourseResponse {
    private Long id;
    private Long authorId;
    private String title;
    private String description;
    private Boolean enabled;
    private String authorName;
    private String language;
    private String level;
    private Integer duration;
    private Long points;
    private String thumbnail;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}