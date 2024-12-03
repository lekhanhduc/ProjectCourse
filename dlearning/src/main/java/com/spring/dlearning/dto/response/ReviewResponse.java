package com.spring.dlearning.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object representing a course review including rating, content, and reply details.")
public class ReviewResponse {

    @Schema(description = "Unique identifier for the review", example = "1")
    Long id;

    @Schema(description = "Name of the reviewer", example = "John Doe")
    String name;

    @Schema(description = "Avatar URL of the reviewer", example = "https://example.com/avatar.jpg")
    String avatar;

    @Schema(description = "Content of the review", example = "This course is excellent!")
    String content;

    @Schema(description = "Rating given by the reviewer, between 1 and 5", example = "5")
    Integer rating;

    @Schema(description = "Timestamp when the review was created", example = "2024-05-01T12:00:00")
    LocalDateTime createdAt;

    @Schema(description = "Timestamp when the review was last updated", example = "2024-05-02T15:30:00")
    LocalDateTime updatedAt;

    @Schema(description = "List of replies to the review", example = "[{id: 2, name: 'Admin', content: 'Thank you for the feedback!'}]")
    List<ReviewResponse> replies = new ArrayList<>();
}
