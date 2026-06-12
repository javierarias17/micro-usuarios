package com.pragma.powerup.application.dto.request;

import com.pragma.powerup.domain.common.RegexConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Request body to create an owner")
public class OwnerRequestDto {

    @Schema(description = "User's first name", example = "John")
    @NotBlank(message = ValidationMessageConstants.MSG_NAME_REQUIRED)
    private String name;

    @Schema(description = "User's last name", example = "Doe")
    @NotBlank(message = ValidationMessageConstants.MSG_LAST_NAME_REQUIRED)
    private String lastName;

    @Schema(description = "User's document number (digits only)", example = "123456789")
    @NotBlank(message = ValidationMessageConstants.MSG_DOCUMENT_NUMBER_REQUIRED)
    @Pattern(regexp = RegexConstants.DOCUMENT_NUMBER_REGEX, message = ValidationMessageConstants.MSG_DOCUMENT_NUMBER_DIGITS_ONLY)
    private String documentNumber;

    @Schema(description = "User's phone number (max 13 chars, optional + prefix)", example = "+573001234567")
    @NotBlank(message = ValidationMessageConstants.MSG_PHONE_REQUIRED)
    @Pattern(regexp = RegexConstants.PHONE_REGEX, message = ValidationMessageConstants.MSG_PHONE_FORMAT)
    private String phone;

    @Schema(description = "User's birth date in yyyy-MM-dd format", example = "1990-05-15", type = "string", format = "date")
    @NotNull(message = ValidationMessageConstants.MSG_BIRTH_DATE_REQUIRED)
    private LocalDate birthDate;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank(message = ValidationMessageConstants.MSG_EMAIL_REQUIRED)
    @Email(message = ValidationMessageConstants.MSG_EMAIL_FORMAT)
    private String email;

    @Schema(description = "User's password", example = "SecurePass123!")
    @NotBlank(message = ValidationMessageConstants.MSG_PASSWORD_REQUIRED)
    private String password;
}
