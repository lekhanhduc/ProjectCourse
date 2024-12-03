package com.spring.dlearning.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object containing details about the user's certificate")
public class CertificateResponse {

    @Schema(description = "The ID of the certificate", example = "101")
    Long certificateId;

    @Schema(description = "The name of the course associated with the certificate", example = "Advanced Java Programming")
    String courseName;

    @Schema(description = "The email of the user who received the certificate", example = "user@example.com")
    String email;

    @Schema(description = "The username of the user who received the certificate", example = "john_doe")
    String username;

    @Schema(description = "The author of the course or certification body", example = "Spring Framework")
    String author;

    @Schema(description = "The issue date of the certificate", example = "Friday, 20 May 2024")
    @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
    LocalDateTime issueDate;

    @Schema(description = "The URL of the certificate", example = "https://example.com/certificates/101")
    String certificateUrl;
}
