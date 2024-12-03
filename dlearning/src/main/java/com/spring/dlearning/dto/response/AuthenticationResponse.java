package com.spring.dlearning.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Response object containing authentication details after successful authentication")
public class AuthenticationResponse {

    @Schema(description = "The authentication token received after successful authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    String token;

    @Schema(description = "The role of the authenticated user", example = "USER")
    String role;

    @Schema(description = "Indicates if the authentication was successful", example = "true")
    boolean authenticated;
}
