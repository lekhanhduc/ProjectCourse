package com.spring.dlearning.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FavoriteResponse {
    Integer favoriteId;
    String name;
    String author;
    String title;
    String thumbnail;
    BigDecimal price;
    Long courseId;
}
