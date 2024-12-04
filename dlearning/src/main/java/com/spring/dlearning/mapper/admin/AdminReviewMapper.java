//package com.spring.dlearning.mapper.admin;
//
//import com.spring.dlearning.dto.response.admin.AdminReviewDTO;
//import org.mapstruct.Mapper;
//
//@Mapper(componentModel = "spring")
//public interface AdminReviewMapper {
//    AdminReviewDTO toAdminReviewDTO(Long courseId, Double averageRating);
//}
package com.spring.dlearning.mapper.admin;

import com.spring.dlearning.dto.response.admin.AdminReviewDTO;
import org.mapstruct.Mapper;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface AdminReviewMapper {
    AdminReviewDTO toAdminReviewDTO(Long courseId, Double averageRating, Long userId, String userName,
                                    String userAvatar, String title, String thumbnail,
                                    LocalDateTime createdAt, LocalDateTime updatedAt);
}
