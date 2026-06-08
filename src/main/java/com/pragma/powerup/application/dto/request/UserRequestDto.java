package com.pragma.powerup.application.dto.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;

@Getter
@Setter
public class UserRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Document number is required")
    @Pattern(regexp = "\\d+", message = "Document number must contain only digits")
    private String documentNumber;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^(\\+[0-9]{1,12}|[0-9]{1,13})$", message = "Phone must contain at most 13 characters and must start with + if provided")
    private String phone;

    @NotNull(message = "Birth date is required")
    private LocalDate birthDate;

    @NotBlank(message = "Email is required")
    @Email(message = "Email must have a valid format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;
}
