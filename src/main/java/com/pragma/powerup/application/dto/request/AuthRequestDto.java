package com.pragma.powerup.application.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
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
    @NotBlank(message = ValidationMessageConstants.MSG_EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.MSG_EMAIL_FORMAT)
    private String email;

    @Schema(description = "User password", example = "secret123")
    @NotBlank(message = ValidationMessageConstants.MSG_PASSWORD_REQUIRED)
    @JsonProperty("password")
    private String userPassword;
}
