package com.pragma.powerup.factory;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;

import java.time.LocalDate;

public class UserModelFactory {

    private UserModelFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static RoleModel createOwnerRole() {
        return RoleModel.builder()
                .id(2L)
                .name("OWNER")
                .description("Restaurant owner")
                .build();
    }

    public static RoleModel createNonOwnerRole() {
        return RoleModel.builder()
                .id(1L)
                .name("CLIENT")
                .description("Regular client")
                .build();
    }

    public static RoleModel createEmployeeRole() {
        return RoleModel.builder()
                .id(3L)
                .name("EMPLOYEE")
                .description("Restaurant employee")
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

    public static UserModel createSavedUserWithPassword(RoleModel role, String encodedPassword) {
        return UserModel.builder()
                .id(1L)
                .name("Armando")
                .lastName("Diaz")
                .documentNumber("1061769969")
                .phone("+573197633852")
                .birthDate(LocalDate.of(1993, 9, 17))
                .email("armando-diaz@gmail.com")
                .password(encodedPassword)
                .role(role)
                .build();
    }

    public static UserModel createSavedUser(RoleModel role) {
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
