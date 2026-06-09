package com.pragma.powerup.application.dto.request;

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
@Schema(description = "Request body to create an user")
public class UserRequestDto {

    @Schema(description = "User's first name", example = "John")
    @NotBlank(message = "Name is required")
    private String name;

    @Schema(description = "User's last name", example = "Doe")
    @NotBlank(message = "Last name is required")
    private String lastName;

    @Schema(description = "User's document number (digits only)", example = "123456789")
    @NotBlank(message = "Document number is required")
    @Pattern(regexp = "\\d+", message = "Document number must contain only digits")
    private String documentNumber;

    @Schema(description = "User's phone number (max 13 chars, optional + prefix)", example = "+573001234567")
    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(\\+\\d{1,12}|\\d{1,13})$", message = "Phone must contain at most 13 characters and must start with + if provided")
    private String phone;

    @Schema(description = "User's birth date in yyyy-MM-dd format", example = "1990-05-15", type = "string", format = "date")
    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    @NotBlank(message = "Email is required")
    @Email(message = "Email must have a valid format")
    private String email;

    @Schema(description = "User's password", example = "SecurePass123!")
    @NotBlank(message = "Password is required")
    private String password;
}
