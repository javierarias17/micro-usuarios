package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    private static final String VALID_NAME = "Armando";
    private static final String VALID_LAST_NAME = "Diaz";
    private static final String VALID_DOCUMENT_NUMBER = "1061769969";
    private static final String VALID_PHONE = "+573197633852";
    private static final String VALID_EMAIL = "armando-diaz@gmail.com";
    private static final String RAW_PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    private static final String BLANK_VALUE = "  ";
    private static final String INVALID_DOCUMENT_NUMBER = "ABC-123";
    private static final String INVALID_PHONE = "not-a-phone";
    private static final String INVALID_EMAIL = "invalid-email";

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    private CreateCustomerUseCase createCustomerUseCase;
    private UserCreateCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = UserModelFactory.createCustomerCommand();
        createCustomerUseCase = new CreateCustomerUseCase(userPersistencePort, passwordEncoderPort);
    }

    // ─── Happy path

    @Test
    void When_CustomerInformationIsCorrect_Expect_CustomerToBeSavedSuccessfully() {
        UserModel savedCustomer = UserModelFactory.createSavedUser(UserModelFactory.createCustomerRole());

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedCustomer);

        UserModel result = createCustomerUseCase.createCustomer(validCommand);

        assertNotNull(result);
        assertEquals(savedCustomer.getId(), result.getId());
        assertEquals(savedCustomer.getName(), result.getName());
        assertEquals(savedCustomer.getLastName(), result.getLastName());
        assertEquals(savedCustomer.getDocumentNumber(), result.getDocumentNumber());
        assertEquals(savedCustomer.getPhone(), result.getPhone());
        assertEquals(savedCustomer.getEmail(), result.getEmail());
    }

    @Test
    void When_CustomerInformationIsCorrect_Expect_PasswordToBeEncoded() {
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel savedCustomer = UserModelFactory.createSavedUserWithPassword(
                UserModelFactory.createCustomerRole(), ENCODED_PASSWORD);

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(captor.capture())).thenReturn(savedCustomer);

        createCustomerUseCase.createCustomer(validCommand);

        assertEquals(ENCODED_PASSWORD, captor.getValue().getPassword().value());
    }

    @Test
    void When_CustomerInformationIsCorrect_Expect_CustomerRoleToBeAssigned() {
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel savedCustomer = UserModelFactory.createSavedUser(UserModelFactory.createCustomerRole());

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(captor.capture())).thenReturn(savedCustomer);

        createCustomerUseCase.createCustomer(validCommand);

        assertEquals(DomainConstants.CUSTOMER_ROLE_ID, captor.getValue().getRole().getId());
    }

    // ─── Validation exceptions

    @Test
    void Expect_FieldsValidationException_When_NameIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                BLANK_VALUE, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_LastNameIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, null, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, BLANK_VALUE,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, INVALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                null, null, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                INVALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, BLANK_VALUE, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, INVALID_EMAIL, RAW_PASSWORD, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PasswordIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, null, null);
        assertThrows(FieldsValidationException.class, () -> createCustomerUseCase.createCustomer(cmd));
    }

    // ─── Business rule exceptions

    @Test
    void Expect_MailAlreadyExistsException_When_EmailIsAlreadyRegistered() {
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(true);

        assertThrows(MailAlreadyExistsException.class,
                () -> createCustomerUseCase.createCustomer(validCommand));
    }

    @Test
    void Expect_DocumentNumberAlreadyExistsException_When_DocumentNumberIsAlreadyRegistered() {
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(true);

        assertThrows(DocumentNumberAlreadyExistsException.class,
                () -> createCustomerUseCase.createCustomer(validCommand));
    }
}
