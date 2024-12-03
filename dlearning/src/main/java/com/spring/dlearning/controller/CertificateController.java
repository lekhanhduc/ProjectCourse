package com.spring.dlearning.controller;

import com.spring.dlearning.dto.event.CertificateCreationEvent;
import com.spring.dlearning.dto.response.ApiResponse;
import com.spring.dlearning.dto.response.CertificateResponse;
import com.spring.dlearning.service.CertificateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/api/v1/certificate")
@Slf4j
public class CertificateController {

    CertificateService certificateService;

    @Operation(summary = "Create a certificate for a user after completing a course",
            description = "This API creates a certificate for a user based on the course completion. The user ID and course ID are required.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Certificate successfully created",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request parameters",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error while creating the certificate",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/creation")
    ApiResponse<Void> createCertification (@RequestBody CertificateCreationEvent request) {
        certificateService.createCertificate(request);
        return ApiResponse.<Void>builder()
                .code(HttpStatus.OK.value())
                .message("Certificate created")
                .build();
    }

    @Operation(summary = "Get certifications for the current logged-in user",
            description = "This API returns a list of certifications for the user currently logged in.")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Successfully retrieved the certifications",
                    content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = CertificateResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "Unauthorized access (if no valid session or token)",
                    content = @Content(mediaType = "application/json")),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Server error while retrieving certifications",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/current-login")
    ApiResponse<List<CertificateResponse>> getCertificationByUserLogin() {
        return ApiResponse.<List<CertificateResponse>>builder()
                .code(HttpStatus.OK.value())
                .result(certificateService.getCertificationByUserLogin())
                .build();
    }

}
