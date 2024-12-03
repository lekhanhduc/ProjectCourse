package com.spring.dlearning.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonInclude;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object containing lesson details after creation")
public class LessonCreationResponse {

    @Schema(description = "ID of the course", example = "101")
    Long courseId;

    @Schema(description = "ID of the chapter", example = "5")
    Long chapterId;

    @Schema(description = "ID of the lesson", example = "2")
    Long lessonId;

    @Schema(description = "Name of the lesson", example = "Introduction to Java")
    String lessonName;

    @Schema(description = "URL of the lesson's video", example = "https://example.com/video/lesson-2")
    String videoUrl;

    @Schema(description = "Description of the lesson", example = "This lesson covers the basics of Java programming.")
    String lessonDescription;
}

