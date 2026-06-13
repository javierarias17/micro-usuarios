package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.domain.common.RegexConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@Schema(description = "Request body to create a customer account")
public class CustomerRequestDto {

    @Schema(description = "Customer's first name", example = "Maria")
    @NotBlank(message = ValidationMessageConstants.MSG_NAME_REQUIRED)
    private String name;

    @Schema(description = "Customer's last name", example = "Lopez")
    @NotBlank(message = ValidationMessageConstants.MSG_LAST_NAME_REQUIRED)
    private String lastName;

    @Schema(description = "Customer's document number (digits only)", example = "987654321")
    @NotBlank(message = ValidationMessageConstants.MSG_DOCUMENT_NUMBER_REQUIRED)
    @Pattern(regexp = RegexConstants.DOCUMENT_NUMBER_REGEX, message = ValidationMessageConstants.MSG_DOCUMENT_NUMBER_DIGITS_ONLY)
    private String documentNumber;

    @Schema(description = "Customer's phone number (max 13 chars, optional + prefix)", example = "+573009876543")
    @NotBlank(message = ValidationMessageConstants.MSG_PHONE_REQUIRED)
    @Pattern(regexp = RegexConstants.PHONE_REGEX, message = ValidationMessageConstants.MSG_PHONE_FORMAT)
    private String phone;

    @Schema(description = "Customer's email address", example = "maria.lopez@example.com")
    @NotBlank(message = ValidationMessageConstants.MSG_EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.MSG_EMAIL_FORMAT)
    private String email;

    @Schema(description = "Customer's password", example = "SecurePass123!")
    @NotBlank(message = ValidationMessageConstants.MSG_PASSWORD_REQUIRED)
    private String password;
}
