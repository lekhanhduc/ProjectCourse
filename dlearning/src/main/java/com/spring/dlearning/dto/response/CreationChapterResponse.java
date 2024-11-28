package com.spring.dlearning.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.Set;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreationChapterResponse {

    String userName;
    Long courseId;
    Long chapterId;
    String chapterName;
    String description;
    Set<LessonDto> lessons;

    @Setter
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public static class LessonDto{
        Long lessonId;
        String lessonName;
        String videoUrl;
        String lessonDescription;
    }
}
