package com.spring.dlearning.controller;

import com.spring.dlearning.dto.request.EmailRequest;
import com.spring.dlearning.dto.request.PasswordCreationRequest;
import com.spring.dlearning.dto.request.UserCreationRequest;
import com.spring.dlearning.dto.request.VerifyOtpRequest;
import com.spring.dlearning.dto.response.*;
import com.spring.dlearning.service.CloudinaryService;
import com.spring.dlearning.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import java.io.UnsupportedEncodingException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1")
public class UserController {

    UserService userService;
    CloudinaryService cloudinaryService;


    @Operation(summary = "Create new user", description = "Create a new user after verifying the OTP code sent to the email.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "User created successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid OTP or email already exists", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreationRequest request,
                                                @RequestParam String otp) {
        var result = userService.createUser(request, otp);

        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.CREATED.value())
                .result(result)
                .build();
    }

    @Operation(summary = "Check if user exists", description = "Checks whether a user exists by email.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User existence checked successfully",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = Boolean.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found")
    })
    @PostMapping("/check-exists-user")
    ApiResponse<Boolean> checkExistsUser(@RequestBody EmailRequest request){
        var result = userService.findByEmail(request);

        return ApiResponse.<Boolean>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @Operation(summary = "Create a new password for the user", description = "This API allows the user to create a new password for their account.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password has been successfully created.",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE, schema = @Schema(implementation = PasswordCreationForFirstRequest.class))),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid password format.")
    })
    @PostMapping("/create-password")
    ApiResponse<Void> createPassword(@RequestBody @Valid PasswordCreationForFirstRequest request){
        userService.createPassword(request);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Password has ben created, you could use it to log-in")
                .build();
    }

    @Operation(summary = "Send OTP for password reset", description = "This API sends an OTP (One-Time Password) to the user's email for password reset.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "OTP has been successfully sent to the provided email."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Invalid email format or other input error."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                    description = "Server error while sending OTP.")
    })
    @PostMapping("/send-otp")
    public ApiResponse<Void> sendOtpForgotPassword(@RequestBody EmailRequest request)
            throws MessagingException, UnsupportedEncodingException {

        userService.sendOtpForgotPassword(request);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("OTP sent successfully to the email.")
                .build();
    }


    @Operation(summary = "Send OTP for user registration", description = "This API sends an OTP (One-Time Password) to the provided email address for user registration.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200",
                    description = "OTP has been successfully sent to the provided email."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400",
                    description = "Invalid email format or other input error."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500",
                    description = "Server error while sending OTP.")
    })
    @PostMapping("/send-otp-register")
    ApiResponse<Void> sendOtpRegister(@RequestBody EmailRequest request)
            throws MessagingException, UnsupportedEncodingException {
        userService.sendOtpRegister(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Send Otp Successfully")
                .build();
    }


    @Operation(summary = "Verify OTP",
            description = "This API verifies the OTP (One-Time Password) sent to the user for registration or password recovery.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "OTP verified successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid OTP or email."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found or OTP expired."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error while verifying OTP.")
    })
    @PostMapping("/verify-otp")
    public ApiResponse<VerifyOtpResponse> verifyOtp(@RequestBody VerifyOtpRequest request) {
        var result = userService.verifyOtp(request);

        return ApiResponse.<VerifyOtpResponse>builder()
                .code(HttpStatus.OK.value())
                .message("Verify Otp Successfully")
                .result(result)
                .build();
    }

    @Operation(summary = "Reset Password",
            description = "This API allows the user to reset their password by providing their email, OTP, and new password.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Password reset successfully."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid OTP, email, or password."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "User not found or OTP expired."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error while resetting password.")
    })
    @PostMapping("/reset-password")
    ApiResponse<Void> resetPassword(@RequestBody @Valid PasswordCreationRequest request ){
        userService.resetPassword(request);

        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Reset Password Successfully")
                .build();
    }

    @Operation(
            summary = "Get a list of all users",
            description = "This API retrieves a list of all registered users in the system."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved the list of users."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error while fetching users.")
    })
    @GetMapping("/users")
    ApiResponse<List<UserResponse>> getUsers() {
        var result = userService.getAllUsers();

        return ApiResponse.<List<UserResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();
    }

    @Operation(
            summary = "Get current user's information",
            description = "This API retrieves the information of the currently authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved the user information."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User is not authenticated or session has expired."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error while fetching user information.")
    })
    @GetMapping("/my-info")
    ApiResponse<UserResponse> getMyInfo(){
        var result = userService.getMyInfo();
        return ApiResponse.<UserResponse>builder()
                .code(HttpStatus.OK.value())
                .result(result)
                .build();

    }

    @Operation(
            summary = "Get user avatar",
            description = "This API retrieves the avatar image URL of the currently authenticated user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved the avatar image URL."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User is not authenticated."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error while fetching the avatar.")
    })
    @GetMapping("/get-avatar")
    ApiResponse<String> getAvatar(){
        String urlAvatar = cloudinaryService.getImage();
        return ApiResponse.<String>builder()
                .code(HttpStatus.OK.value())
                .result(urlAvatar)
                .build();
    }

    @Operation(
            summary = "Get points for the currently logged-in user",
            description = "This API retrieves the current points of the logged-in user."
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved points for the logged-in user."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "User is not authenticated or session has expired."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Internal server error while fetching user points.")
    })
    @GetMapping("/get-points-user-current")
    ApiResponse<GetPointsCurrentLogin> getPointsUserLogin(){
        return ApiResponse.<GetPointsCurrentLogin>builder()
                .code(HttpStatus.OK.value())
                .result(userService.getPointsCurrentLogin())
                .build();
    }

}
