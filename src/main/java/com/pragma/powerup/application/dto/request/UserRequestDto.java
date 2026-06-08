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

    @NotBlank
    private String name;

    @NotBlank
    private String lastName;

    @NotBlank
    @Pattern(regexp = "\\d+", message = "Document number must contain only digits")
    private String documentNumber;

    @NotBlank
    @Pattern(regexp = "^(\\+[0-9]{1,12}|[0-9]{1,13})$", message = "Phone must contain at most 13 characters and must start with + if provided")
    private String phone;

    @NotNull
    private LocalDate birthDate;

    @NotBlank
    @Email(message = "Email must have a valid format")
    private String email;

    @NotBlank
    private String password;
}
