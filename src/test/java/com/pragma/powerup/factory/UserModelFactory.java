package com.pragma.powerup.factory;

import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserCreateCommand;
import com.pragma.powerup.domain.model.UserModel;

import java.time.LocalDate;
import java.time.Month;

public class UserModelFactory {

    private UserModelFactory() {
        throw new IllegalStateException("Utility class");
    }

    // ─── Role factories

    public static RoleModel createOwnerRole() {
        return RoleModel.builder()
                .id(2L)
                .name("OWNER")
                .description("Restaurant owner")
                .build();
    }

    public static RoleModel createEmployeeRole() {
        return RoleModel.builder()
                .id(3L)
                .name("EMPLOYEE")
                .description("Restaurant employee")
                .build();
    }

    public static RoleModel createCustomerRole() {
        return RoleModel.builder()
                .id(4L)
                .name("CUSTOMER")
                .description("Regular customer")
                .build();
    }

    public static RoleModel createNonOwnerRole() {
        return createCustomerRole();
    }

    // ─── Command factories (entradas a los casos de uso)

    public static UserCreateCommand createOwnerCommand() {
        return new UserCreateCommand(
                "Armando", "Diaz", "1061769969", "+573197633852",
                LocalDate.of(1993, Month.SEPTEMBER, 17),
                "armando-diaz@gmail.com", "secret123");
    }

    public static UserCreateCommand createOwnerCommandWithBirthDate(LocalDate birthDate) {
        return new UserCreateCommand(
                "Armando", "Diaz", "1061769969", "+573197633852",
                birthDate,
                "armando-diaz@gmail.com", "secret123");
    }

    public static UserCreateCommand createEmployeeCommand() {
        return new UserCreateCommand(
                "Armando", "Diaz", "1061769969", "+573197633852",
                null,
                "armando-diaz@gmail.com", "secret123");
    }

    public static UserCreateCommand createCustomerCommand() {
        return new UserCreateCommand(
                "Armando", "Diaz", "1061769969", "+573197633852",
                null,
                "armando-diaz@gmail.com", "secret123");
    }

    // ─── UserModel factories (valores de retorno de los mocks)

    public static UserModel createSavedUser(RoleModel role) {
        return UserModel.builder()
                .id(1L)
                .name("Armando")
                .lastName("Diaz")
                .documentNumber("1061769969")
                .phone("+573197633852")
                .birthDate(LocalDate.of(1993, Month.SEPTEMBER, 17))
                .email("armando-diaz@gmail.com")
                .password("encodedPassword")
                .role(role)
                .build();
    }

    public static UserModel createSavedUserWithPassword(RoleModel role, String encodedPassword) {
        return UserModel.builder()
                .id(1L)
                .name("Armando")
                .lastName("Diaz")
                .documentNumber("1061769969")
                .phone("+573197633852")
                .birthDate(LocalDate.of(1993, Month.SEPTEMBER, 17))
                .email("armando-diaz@gmail.com")
                .password(encodedPassword)
                .role(role)
                .build();
    }
}
