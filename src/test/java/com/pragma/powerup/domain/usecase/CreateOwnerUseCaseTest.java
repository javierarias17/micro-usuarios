package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.NotAdultException;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.validator.UserValidator;
import com.pragma.powerup.domain.validator.strategy.OwnerValidationStrategy;
import com.pragma.powerup.factory.UserModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateOwnerUseCaseTest {

    private static final String BLANK_VALUE = "  ";
    private static final String DOCUMENT_NUMBER_INVALID = "ABC-123";
    private static final String PHONE_INVALID = "not-a-phone";
    private static final String EMAIL_INVALID = "invalid-email";
    private static final String RAW_PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final LocalDate BIRTH_DATE_ADULT = LocalDate.of(2006, Month.JUNE, 15);
    private static final LocalDate BIRTH_DATE_UNDERAGE = LocalDate.of(2015, Month.JUNE, 15);

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    private CreateOwnerUseCase createOwnerUseCase;

    private UserModel validUser;
    private RoleModel ownerRole;

    @BeforeEach
    void setUp() {
        ownerRole = UserModelFactory.createOwnerRole();
        validUser = UserModelFactory.createValidUser();
        createOwnerUseCase = new CreateOwnerUseCase(
                userPersistencePort, passwordEncoderPort,
                new UserValidator(new OwnerValidationStrategy()));
    }

    // ─── Happy path

    @Test
    void When_OwnerInformationIsCorrect_Expect_OwnerToBeSavedSuccessfully() {
        // Arrange
        UserModel savedUser = UserModelFactory.createSavedUser(ownerRole);

        when(userPersistencePort.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validUser.getDocumentNumber())).thenReturn(false);
        when(passwordEncoderPort.encode(validUser.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedUser);

        // Act
        UserModel result = createOwnerUseCase.createOwner(validUser);

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
    void When_OwnerInformationIsCorrect_Expect_PasswordToBeEncoded() {
        // Arrange
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(validUser);

        // Act
        UserModel result = createOwnerUseCase.createOwner(validUser);

        // Assert
        assertEquals(ENCODED_PASSWORD, result.getPassword());
    }

    @Test
    void When_OwnerInformationIsCorrect_Expect_OwnerRoleToBeAssigned() {
        // Arrange
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(validUser);

        // Act
        UserModel result = createOwnerUseCase.createOwner(validUser);

        // Assert
        assertEquals(ownerRole.getId(), result.getRole().getId());
    }

    @Test
    void When_OwnerIsExactly18YearsOld_Expect_OwnerToBeSavedSuccessfully() {
        // Arrange
        UserModel userExactly18 = UserModelFactory.createUserWithBirthDate(BIRTH_DATE_ADULT);

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(userExactly18);

        // Act & Assert
        assertDoesNotThrow(() -> createOwnerUseCase.createOwner(userExactly18));
    }

    // ─── Exceptions path

    @Test
    void Expect_FieldsValidationException_When_NameIsBlank() {
        validUser.setName(BLANK_VALUE);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_LastNameIsNull() {
        validUser.setLastName(null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberIsBlank() {
        validUser.setDocumentNumber(BLANK_VALUE);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberHasInvalidFormat() {
        validUser.setDocumentNumber(DOCUMENT_NUMBER_INVALID);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneIsNull() {
        validUser.setPhone(null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneHasInvalidFormat() {
        validUser.setPhone(PHONE_INVALID);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_BirthDateIsNull() {
        validUser.setBirthDate(null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailIsBlank() {
        validUser.setEmail(BLANK_VALUE);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
        validUser.setEmail(EMAIL_INVALID);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_FieldsValidationException_When_PasswordIsNull() {
        validUser.setPassword(null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_MailAlreadyExistsException_When_EmailIsAlreadyRegistered() {
        // Arrange
        when(userPersistencePort.existsByEmail(validUser.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(MailAlreadyExistsException.class,
                () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_DocumentNumberAlreadyExistsException_When_DocumentNumberIsAlreadyRegistered() {
        // Arrange
        when(userPersistencePort.existsByEmail(validUser.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validUser.getDocumentNumber())).thenReturn(true);

        // Act & Assert
        assertThrows(DocumentNumberAlreadyExistsException.class,
                () -> createOwnerUseCase.createOwner(validUser));
    }

    @Test
    void Expect_NotAdultException_When_OwnerIsUnderage() {
        // Arrange
        UserModel userUnder18 = UserModelFactory.createUserWithBirthDate(BIRTH_DATE_UNDERAGE);

        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);

        // Act & Assert
        assertThrows(NotAdultException.class,
                () -> createOwnerUseCase.createOwner(userUnder18));
    }
}
