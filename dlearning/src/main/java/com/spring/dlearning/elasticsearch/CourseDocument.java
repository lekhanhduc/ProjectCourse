package com.spring.dlearning.elasticsearch;

import com.spring.dlearning.common.CourseLevel;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "course-document")
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CourseDocument {

    @Id
    String id;

    @Field(type = FieldType.Text, name = "title")
    String title;

    @Field(type = FieldType.Text, name = "author")
    String author;

    @Field(type = FieldType.Long, name = "points")
    Long points;

    @Field(type = FieldType.Text, name = "language")
    String language;

    @Field(type = FieldType.Text, name = "description")
    String description;

    @Field(type = FieldType.Text, name = "thumbnail")
    String thumbnail;

    @Field(type = FieldType.Keyword, name = "level")
    CourseLevel level;

}

