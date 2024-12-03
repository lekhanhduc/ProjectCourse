package com.spring.dlearning.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object for the result of updating a review")
public class UpdateReviewResponse {
    Long id;

    @Schema(description = "Message explaining the update result", example = "Review successfully updated.")
    String content;
}
