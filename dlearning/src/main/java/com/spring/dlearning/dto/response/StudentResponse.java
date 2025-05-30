package com.spring.dlearning.dto.response;

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
public class StudentResponse {
    String name;
    String email;
    String avatar;
    String courseName;

    @JsonFormat(pattern = "EEEE, dd MMMM yyyy")
    LocalDateTime createAt;
}
