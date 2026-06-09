package com.pragma.powerup.factory;

import com.pragma.powerup.domain.model.RolModel;
import com.pragma.powerup.domain.model.UserModel;

import java.time.LocalDate;

public class UserModelFactory {

    private UserModelFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static RolModel createOwnerRole() {
        return RolModel.builder()
                .id(2L)
                .name("OWNER")
                .description("Restaurant owner")
                .build();
    }

    public static UserModel createValidUser() {
        return UserModel.builder()
                .name("Armando")
                .lastName("Diaz")
                .documentNumber("1061769969")
                .phone("+573197633852")
                .birthDate(LocalDate.of(1993, 9, 17))
                .email("armando-diaz@gmail.com")
                .password("secret123")
                .build();
    }

    public static UserModel createUserWithBirthDate(LocalDate birthDate) {
        return UserModel.builder()
                .name("Armando")
                .lastName("Diaz")
                .documentNumber("1061769969")
                .phone("+573197633852")
                .birthDate(birthDate)
                .email("armando-diaz@gmail.com")
                .password("secret123")
                .build();
    }

    public static UserModel createSavedUser(RolModel role) {
        return UserModel.builder()
                .id(1L)
                .name("Armando")
                .lastName("Diaz")
                .documentNumber("1061769969")
                .phone("+573197633852")
                .birthDate(LocalDate.of(1993, 9, 17))
                .email("armando-diaz@gmail.com")
                .role(role)
                .build();
    }
}
