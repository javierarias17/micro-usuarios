package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.NotAdultException;
import com.pragma.powerup.domain.model.UserCreateCommand;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.factory.UserModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateOwnerUseCaseTest {

    private static final String VALID_NAME = "Armando";
    private static final String VALID_LAST_NAME = "Diaz";
    private static final String VALID_DOCUMENT_NUMBER = "1061769969";
    private static final String VALID_PHONE = "+573197633852";
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(1993, Month.SEPTEMBER, 17);
    private static final String VALID_EMAIL = "armando-diaz@gmail.com";
    private static final String RAW_PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    private static final String BLANK_VALUE = "  ";
    private static final String INVALID_DOCUMENT_NUMBER = "ABC-123";
    private static final String INVALID_PHONE = "not-a-phone";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final LocalDate BIRTH_DATE_ADULT = LocalDate.of(2006, Month.JUNE, 15);
    private static final LocalDate BIRTH_DATE_UNDERAGE = LocalDate.of(2015, Month.JUNE, 15);

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    private CreateOwnerUseCase createOwnerUseCase;
    private UserCreateCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = UserModelFactory.createOwnerCommand();
        createOwnerUseCase = new CreateOwnerUseCase(userPersistencePort, passwordEncoderPort);
    }

    // ─── Happy path

    @Test
    void When_OwnerInformationIsCorrect_Expect_OwnerToBeSavedSuccessfully() {
        UserModel savedOwner = UserModelFactory.createSavedUser(UserModelFactory.createOwnerRole());

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedOwner);

        UserModel result = createOwnerUseCase.createOwner(validCommand);

        assertNotNull(result);
        assertEquals(savedOwner.getId(), result.getId());
        assertEquals(savedOwner.getName(), result.getName());
        assertEquals(savedOwner.getLastName(), result.getLastName());
        assertEquals(savedOwner.getDocumentNumber(), result.getDocumentNumber());
        assertEquals(savedOwner.getPhone(), result.getPhone());
        assertEquals(savedOwner.getBirthDate(), result.getBirthDate());
        assertEquals(savedOwner.getEmail(), result.getEmail());
    }

    @Test
    void When_OwnerInformationIsCorrect_Expect_PasswordToBeEncoded() {
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel savedOwner = UserModelFactory.createSavedUserWithPassword(
                UserModelFactory.createOwnerRole(), ENCODED_PASSWORD);

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(captor.capture())).thenReturn(savedOwner);

        createOwnerUseCase.createOwner(validCommand);

        assertEquals(ENCODED_PASSWORD, captor.getValue().getPassword().value());
    }

    @Test
    void When_OwnerInformationIsCorrect_Expect_OwnerRoleToBeAssigned() {
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel savedOwner = UserModelFactory.createSavedUser(UserModelFactory.createOwnerRole());

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(captor.capture())).thenReturn(savedOwner);

        createOwnerUseCase.createOwner(validCommand);

        assertEquals(DomainConstants.OWNER_ROLE_ID, captor.getValue().getRole().getId());
    }

    @Test
    void When_OwnerIsExactly18YearsOld_Expect_OwnerToBeSavedSuccessfully() {
        UserCreateCommand command = UserModelFactory.createOwnerCommandWithBirthDate(BIRTH_DATE_ADULT);
        UserModel savedOwner = UserModelFactory.createSavedUser(UserModelFactory.createOwnerRole());

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedOwner);

        assertDoesNotThrow(() -> createOwnerUseCase.createOwner(command));
    }

    // ─── Validation exceptions

    @Test
    void Expect_FieldsValidationException_When_NameIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                BLANK_VALUE, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, VALID_BIRTH_DATE, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_LastNameIsNull() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, null, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, VALID_BIRTH_DATE, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, BLANK_VALUE,
                VALID_PHONE, VALID_BIRTH_DATE, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, INVALID_DOCUMENT_NUMBER,
                VALID_PHONE, VALID_BIRTH_DATE, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneIsNull() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                null, VALID_BIRTH_DATE, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                INVALID_PHONE, VALID_BIRTH_DATE, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_BirthDateIsNull() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, VALID_BIRTH_DATE, BLANK_VALUE, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, VALID_BIRTH_DATE, INVALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PasswordIsNull() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, VALID_BIRTH_DATE, VALID_EMAIL, null, null);
        assertThrows(FieldsValidationException.class, () -> createOwnerUseCase.createOwner(cmd));
    }

    // ─── Business rule exceptions

    @Test
    void Expect_MailAlreadyExistsException_When_EmailIsAlreadyRegistered() {
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(true);

        assertThrows(MailAlreadyExistsException.class,
                () -> createOwnerUseCase.createOwner(validCommand));
    }

    @Test
    void Expect_DocumentNumberAlreadyExistsException_When_DocumentNumberIsAlreadyRegistered() {
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(true);

        assertThrows(DocumentNumberAlreadyExistsException.class,
                () -> createOwnerUseCase.createOwner(validCommand));
    }

    @Test
    void Expect_NotAdultException_When_OwnerIsUnderage() {
        UserCreateCommand command = UserModelFactory.createOwnerCommandWithBirthDate(BIRTH_DATE_UNDERAGE);

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);

        assertThrows(NotAdultException.class, () -> createOwnerUseCase.createOwner(command));
    }
}
