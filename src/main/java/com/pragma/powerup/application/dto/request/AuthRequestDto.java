package com.pragma.powerup.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Schema(description = "Credentials for authentication")
public class AuthRequestDto {

    @Schema(description = "User email", example = "admin@pragma.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must have a valid format")
    private String email;

    @Schema(description = "User password", example = "secret123")
    @NotBlank(message = "Password is required")
    private String password;
}
