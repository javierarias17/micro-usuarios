package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.NotAdultException;
import com.pragma.powerup.domain.model.RolModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.factory.UserModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    private UserModel validUser;
    private RolModel ownerRole;

    @BeforeEach
    void setUp() {
        ownerRole = UserModelFactory.createOwnerRole();
        validUser = UserModelFactory.createValidUser();
    }

    // ─── Happy path

    @Test
    void When_UserInformationIsCorrect_Expect_UserToBeSavedSuccessfully() {
        // Arrange
        UserModel savedUser = UserModelFactory.createSavedUser(ownerRole);

        when(userPersistencePort.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validUser.getDocumentNumber())).thenReturn(false);
        when(passwordEncoderPort.encode(validUser.getPassword())).thenReturn("encodedPassword");
        when(userPersistencePort.getRoleById(2L)).thenReturn(ownerRole);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedUser);

        // Act
        UserModel result = createUserUseCase.createUser(validUser);

        // Assert
        assertNotNull(result);
        assertEquals(savedUser.getName(), result.getName());
        assertEquals(savedUser.getLastName(), result.getLastName());
        assertEquals(savedUser.getDocumentNumber(), result.getDocumentNumber());
        assertEquals(savedUser.getPhone(), result.getPhone());
        assertEquals(savedUser.getBirthDate(), result.getBirthDate());
        assertEquals(savedUser.getEmail(), result.getEmail());
        assertEquals(ownerRole, result.getRole());
    }

    @Test
    void When_UserInformationIsCorrect_Expect_PasswordToBeEncoded() {
        // Arrange
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode("secret123")).thenReturn("encodedPassword");
        when(userPersistencePort.getRoleById(anyLong())).thenReturn(ownerRole);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(validUser);

        // Act
        UserModel result = createUserUseCase.createUser(validUser);

        // Assert
        assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    void When_UserInformationIsCorrect_Expect_OwnerRoleToBeAssigned() {
        // Arrange
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");
        when(userPersistencePort.getRoleById(2L)).thenReturn(ownerRole);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(validUser);

        // Act
        UserModel result = createUserUseCase.createUser(validUser);

        // Assert
        assertEquals(ownerRole.getId(), result.getRole().getId());
    }

    @Test
    void When_UserIsExactly18YearsOld_Expect_UserToBeSavedSuccessfully() {
        // Arrange
        UserModel userExactly18 = UserModelFactory.createUserWithBirthDate(LocalDate.now().minusYears(18));

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode(anyString())).thenReturn("encodedPassword");
        when(userPersistencePort.getRoleById(anyLong())).thenReturn(ownerRole);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(userExactly18);

        // Act & Assert
        assertDoesNotThrow(() -> createUserUseCase.createUser(userExactly18));
    }

    // ─── Exceptions path

    @Test
    void Expect_MailAlreadyExistsException_When_EmailIsAlreadyRegistered() {
        // Arrange
        when(userPersistencePort.existsByEmail(validUser.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(MailAlreadyExistsException.class,
                () -> createUserUseCase.createUser(validUser));
    }

    @Test
    void Expect_DocumentNumberAlreadyExistsException_When_DocumentNumberIsAlreadyRegistered() {
        // Arrange
        when(userPersistencePort.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validUser.getDocumentNumber())).thenReturn(true);

        // Act & Assert
        assertThrows(DocumentNumberAlreadyExistsException.class,
                () -> createUserUseCase.createUser(validUser));
    }

    @Test
    void Expect_NotAdultException_When_UserIsUnderage() {
        // Arrange
        UserModel userUnder18 = UserModelFactory.createUserWithBirthDate(LocalDate.now().minusYears(17));

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(NotAdultException.class,
                () -> createUserUseCase.createUser(userUnder18));
    }
}
