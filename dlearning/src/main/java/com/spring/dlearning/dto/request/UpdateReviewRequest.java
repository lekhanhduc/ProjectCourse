package com.spring.dlearning.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateReviewRequest {

    @Schema(description = "The updated content of the review", example = "Great course, very informative!")
    String content;

    @Schema(description = "The updated rating for the review (1-5)", example = "5")
    Integer rating;
}
