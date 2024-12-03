package com.spring.dlearning.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Request object to create a new post")
public class PostCreationRequest {

    @NotBlank(message = "CONTENT_POST_INVALID")
    @Schema(description = "Content of the post", example = "This is my first post!")
    String content;

    @Schema(description = "Optional image for the post", example = "https://example.com/image.jpg")
    String image;

}

