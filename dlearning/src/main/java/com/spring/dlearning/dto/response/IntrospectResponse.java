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
@Schema(description = "Response object containing the result of the token verification")
public class IntrospectResponse {

    @Schema(description = "Indicates if the token is valid", example = "true")
    boolean valid;

    @Schema(description = "The scope of the token", example = "read write")
    String scope;
}
