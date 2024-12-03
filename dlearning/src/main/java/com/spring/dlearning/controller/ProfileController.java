package com.spring.dlearning.controller;

import com.spring.dlearning.dto.request.UserProfileRequest;
import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.UserProfileResponse;
import com.spring.dlearning.service.CloudinaryService;
import com.spring.dlearning.service.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
@Slf4j
public class ProfileController {

    ProfileService profileService;
    CloudinaryService cloudinaryService;

    @Operation(summary = "Update user profile", description = "This API allows the user to update their profile information.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid input data"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User not authenticated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/update-profile")
    ApiResponse<Void> updateProfile( @RequestBody @Valid UserProfileRequest request){
        profileService.updateProfile(request);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Profile updated successfully")
                .build();
    }

    @GetMapping("/info-user")
    @Operation(summary = "Get user profile information", description = "This API retrieves the user's profile information.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User profile information successfully retrieved"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User not authenticated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    ApiResponse<UserProfileResponse> getUserProfile(){
        var result = profileService.getInfoProfile();

        return ApiResponse.<UserProfileResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Profile info successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Update user profile avatar", description = "This API allows a user to upload and update their avatar image.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Avatar updated successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid file format"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User not authenticated"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping("/update-avatar")
    ApiResponse<String> updateAvatar(@RequestParam("file") MultipartFile file) {
        SecurityContext context = SecurityContextHolder.getContext();
        String email = context.getAuthentication().getName();

        String url = cloudinaryService.uploadImage(file);

        cloudinaryService.updateImage(url, email);

        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .message("Profile updated successfully")
                .build();
    }

    @Operation(summary = "Remove user profile avatar", description = "This API allows a user to remove their profile avatar.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Avatar removed successfully"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error, failed to remove avatar")
    })
    @DeleteMapping("remove-avatar")
    ApiResponse<String> removeAvatar() {
        try {
            cloudinaryService.deleteAvatar();
            return ApiResponse.<String>builder()
                    .code(HttpStatus.OK.value())
                    .message("Profile removed successfully")
                    .build();
        } catch (Exception e) {
            return ApiResponse.<String>builder()
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .message(e.getMessage())
                    .build();
        }
    }

}
