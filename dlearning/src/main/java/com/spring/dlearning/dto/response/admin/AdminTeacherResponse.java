package com.spring.dlearning.dto.response.admin;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime; // Đổi từ LocalDate sang LocalDateTime

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminTeacherResponse {
    private Long id;
    private String name;
    private String email;
    private String gender;
    private String role;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdAt;
}


