package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.UserNotFoundException;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.factory.UserModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateUserRoleUseCaseTest {

    private static final long NON_EXISTENT_USER_ID = 99L;

    @Mock
    private IUserPersistencePort userPersistencePort;

    @InjectMocks
    private ValidateUserRoleUseCase validateUserRoleUseCase;

    private UserModel ownerUser;
    private UserModel nonOwnerUser;
    private UserModel ownerUserWithoutRole;

    @BeforeEach
    void setUp() {
        ownerUser = UserModelFactory.createSavedUser(UserModelFactory.createOwnerRole());
        nonOwnerUser = UserModelFactory.createSavedUser(UserModelFactory.createNonOwnerRole());
        ownerUserWithoutRole = UserModelFactory.createSavedUser(null);
    }

    // ─── isOwner: Happy path

    @Test
    void When_UserExistsAndHasOwnerRole_Expect_TrueReturned() {
        // Arrange
        when(userPersistencePort.getUserById(ownerUser.getId())).thenReturn(Optional.of(ownerUser));

        // Act
        boolean result = validateUserRoleUseCase.isOwner(ownerUser.getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void When_UserExistsAndHasNonOwnerRole_Expect_FalseReturned() {
        // Arrange
        when(userPersistencePort.getUserById(nonOwnerUser.getId())).thenReturn(Optional.of(nonOwnerUser));

        // Act
        boolean result = validateUserRoleUseCase.isOwner(nonOwnerUser.getId());

        // Assert
        assertFalse(result);
    }

    @Test
    void When_UserExistsWithNoRole_Expect_FalseReturnedForIsOwner() {
        // Es test aumenta cobertura; no hace parte de un criterio de aceptación
        // Arrange
        when(userPersistencePort.getUserById(ownerUserWithoutRole.getId()))
                .thenReturn(Optional.of(ownerUserWithoutRole));

        // Act
        boolean result = validateUserRoleUseCase.isOwner(ownerUserWithoutRole.getId());

        // Assert
        assertFalse(result);
    }

    // ─── Exceptions path

    @Test
    void Expect_UserNotFoundException_When_UserDoesNotExistForIsOwner() {
        // Arrange
        when(userPersistencePort.getUserById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> validateUserRoleUseCase.isOwner(NON_EXISTENT_USER_ID));
    }

    // ─── isEmployee: Happy path

    @Test
    void When_UserExistsAndHasEmployeeRole_Expect_TrueReturned() {
        // Arrange
        UserModel employeeUser = UserModelFactory.createSavedUser(UserModelFactory.createEmployeeRole());
        when(userPersistencePort.getUserById(employeeUser.getId())).thenReturn(Optional.of(employeeUser));

        // Act
        boolean result = validateUserRoleUseCase.isEmployee(employeeUser.getId());

        // Assert
        assertTrue(result);
    }

    @Test
    void When_UserExistsAndHasNonEmployeeRole_Expect_FalseReturned() {
        // Arrange
        when(userPersistencePort.getUserById(nonOwnerUser.getId())).thenReturn(Optional.of(nonOwnerUser));

        // Act
        boolean result = validateUserRoleUseCase.isEmployee(nonOwnerUser.getId());

        // Assert
        assertFalse(result);
    }

    @Test
    void When_UserExistsWithNoRole_Expect_FalseReturnedForIsEmployee() {
        // Arrange
        when(userPersistencePort.getUserById(ownerUserWithoutRole.getId()))
                .thenReturn(Optional.of(ownerUserWithoutRole));

        // Act
        boolean result = validateUserRoleUseCase.isEmployee(ownerUserWithoutRole.getId());

        // Assert
        assertFalse(result);
    }

    // ─── isEmployee: Exceptions path

    @Test
    void Expect_UserNotFoundException_When_UserDoesNotExistForIsEmployee() {
        // Arrange
        when(userPersistencePort.getUserById(NON_EXISTENT_USER_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> validateUserRoleUseCase.isEmployee(NON_EXISTENT_USER_ID));
    }
}
