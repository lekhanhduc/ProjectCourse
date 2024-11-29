package com.spring.dlearning.mapper.admin;

import com.spring.dlearning.dto.response.admin.AdminTeacherResponse;
import com.spring.dlearning.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminTeacherMapper {
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "role.name", target = "role")
    AdminTeacherResponse toTeacherResponse(User user);
}


