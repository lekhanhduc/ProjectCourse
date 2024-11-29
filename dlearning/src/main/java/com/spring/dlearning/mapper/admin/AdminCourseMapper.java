package com.spring.dlearning.mapper.admin;

import com.spring.dlearning.dto.response.admin.AdminCourseResponse;
import com.spring.dlearning.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminCourseMapper {

    @Mapping(source = "courseLevel", target = "level") // ánh xạ enum CourseLevel thành chuỗi level
    @Mapping(source = "author.name", target = "authorName", defaultValue = "Unknown") // ánh xạ tên tác giả hoặc gán giá trị mặc định
    @Mapping(source = "createdAt", target = "createdAt") // ánh xạ trực tiếp
    @Mapping(source = "updatedAt", target = "updatedAt") // ánh xạ trực tiếp
    AdminCourseResponse toCourseResponse(Course course);
}
