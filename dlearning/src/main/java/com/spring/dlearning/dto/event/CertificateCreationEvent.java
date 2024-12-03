package com.spring.dlearning.dto.event;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Event for creating a certificate after course completion")
public class CertificateCreationEvent {

    @Schema(description = "The ID of the user who completed the course", example = "123")
    Long userId;

    @Schema(description = "The ID of the completed course", example = "456")
    Long courseId;
}
