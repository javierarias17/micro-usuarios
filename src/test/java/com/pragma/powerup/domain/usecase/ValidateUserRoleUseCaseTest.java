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

    // ─── Happy path

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
    void When_UserExistsWithNoRole_Expect_FalseReturned() {
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
    void Expect_UserNotFoundException_When_UserDoesNotExist() {
        // Arrange
        Long nonExistentUserId = 99L;
        when(userPersistencePort.getUserById(nonExistentUserId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class,
                () -> validateUserRoleUseCase.isOwner(nonExistentUserId));
    }
}
