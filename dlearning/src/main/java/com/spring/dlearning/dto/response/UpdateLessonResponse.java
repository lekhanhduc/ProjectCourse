package com.spring.dlearning.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;
import com.fasterxml.jackson.annotation.JsonInclude;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response object containing the updated lesson details")
public class UpdateLessonResponse {

    @Schema(description = "ID of the course", example = "101")
    Long courseId;

    @Schema(description = "ID of the chapter", example = "5")
    Long chapterId;

    @Schema(description = "Name of the chapter", example = "Java Basics")
    String chapterName;

    @Schema(description = "ID of the lesson", example = "2")
    Long lessonId;

    @Schema(description = "Name of the lesson", example = "Introduction to Java")
    String lessonName;

    @Schema(description = "Description of the lesson", example = "This lesson covers the basics of Java programming.")
    String description;

    @Schema(description = "URL of the lesson's video", example = "https://example.com/video/updated-lesson")
    String videoUrl;
}

