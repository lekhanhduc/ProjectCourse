package com.spring.dlearning.mapper.admin;

import com.spring.dlearning.dto.response.admin.AdminUserResponse;
import com.spring.dlearning.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AdminUserMapper {
    @Mapping(source = "createdAt", target = "createAt") // ánh xạ thuộc tính từ entity sang DTO
    @Mapping(source = "role.name", target = "role") // ánh xạ thuộc tính 'name' của Role thành chuỗi 'role' trong UserResponse
    @Mapping(source = "enabled", target = "enabled")
    AdminUserResponse toUserResponse(User user);
}
