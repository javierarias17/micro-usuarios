package com.pragma.powerup.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Schema(description = "Response body after creating an user")
public class UserResponseDto {

    @Schema(description = "Generated user ID", example = "1")
    private Long id;

    @Schema(description = "User's first name", example = "John")
    private String name;

    @Schema(description = "User's last name", example = "Doe")
    private String lastName;

    @Schema(description = "User's document number", example = "123456789")
    private String documentNumber;

    @Schema(description = "User's phone number", example = "+573001234567")
    private String phone;

    @Schema(description = "User's birth date", example = "1990-05-15", type = "string", format = "date")
    private LocalDate birthDate;

    @Schema(description = "User's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Assigned role ID", example = "2")
    private Long idRole;
}
