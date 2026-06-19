package com.pragma.powerup.domain.model;

import java.time.LocalDate;

public record UserCreateCommand(
        String name,
        String lastName,
        String documentNumber,
        String phone,
        LocalDate birthDate,
        String email,
        String password,
        Long restaurantId
) {}
