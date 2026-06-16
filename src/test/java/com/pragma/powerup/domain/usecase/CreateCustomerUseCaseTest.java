package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.validator.UserValidator;
import com.pragma.powerup.domain.validator.strategy.CustomerValidationStrategy;
import com.pragma.powerup.factory.UserModelFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    private static final String BLANK_VALUE = "  ";
    private static final String DOCUMENT_NUMBER_INVALID = "ABC-123";
    private static final String PHONE_INVALID = "not-a-phone";
    private static final String EMAIL_INVALID = "invalid-email";
    private static final String RAW_PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "encodedPassword";

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    private CreateCustomerUseCase createCustomerUseCase;

    private UserModel validCustomer;
    private RoleModel customerRole;

    @BeforeEach
    void setUp() {
        customerRole = UserModelFactory.createCustomerRole();
        validCustomer = UserModelFactory.createValidUser();
        createCustomerUseCase = new CreateCustomerUseCase(
                userPersistencePort, passwordEncoderPort,
                new UserValidator(new CustomerValidationStrategy()));
    }

    // ─── Happy path

    @Test
    void When_CustomerInformationIsCorrect_Expect_CustomerToBeSavedSuccessfully() {
        // Arrange
        UserModel savedCustomer = UserModelFactory.createSavedUser(customerRole);

        when(userPersistencePort.existsByEmail(validCustomer.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validCustomer.getDocumentNumber())).thenReturn(false);
        when(passwordEncoderPort.encode(validCustomer.getPassword())).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedCustomer);

        // Act
        UserModel result = createCustomerUseCase.createCustomer(validCustomer);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(savedCustomer.getName(), result.getName());
        assertEquals(savedCustomer.getLastName(), result.getLastName());
        assertEquals(savedCustomer.getDocumentNumber(), result.getDocumentNumber());
        assertEquals(savedCustomer.getPhone(), result.getPhone());
        assertEquals(savedCustomer.getEmail(), result.getEmail());
        assertEquals(customerRole, result.getRole());
    }

    @Test
    void When_CustomerInformationIsCorrect_Expect_PasswordToBeEncoded() {
        // Arrange
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(validCustomer);

        // Act
        UserModel result = createCustomerUseCase.createCustomer(validCustomer);

        // Assert
        assertEquals(ENCODED_PASSWORD, result.getPassword());
    }

    // ─── Exceptions path

    @Test
    void Expect_FieldsValidationException_When_NameIsBlank() {
        validCustomer.setName(BLANK_VALUE);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_LastNameIsBlank() {
        validCustomer.setLastName(null);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberIsBlank() {
        validCustomer.setDocumentNumber(BLANK_VALUE);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberHasInvalidFormat() {
        validCustomer.setDocumentNumber(DOCUMENT_NUMBER_INVALID);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneIsBlank() {
        validCustomer.setPhone(null);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneHasInvalidFormat() {
        validCustomer.setPhone(PHONE_INVALID);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailIsBlank() {
        validCustomer.setEmail(BLANK_VALUE);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
        validCustomer.setEmail(EMAIL_INVALID);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_PasswordIsBlank() {
        validCustomer.setPassword(null);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_MailAlreadyExistsException_When_EmailIsAlreadyRegistered() {
        when(userPersistencePort.existsByEmail(validCustomer.getEmail())).thenReturn(true);
        assertThrows(MailAlreadyExistsException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_DocumentNumberAlreadyExistsException_When_DocumentNumberIsAlreadyRegistered() {
        when(userPersistencePort.existsByEmail(validCustomer.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validCustomer.getDocumentNumber())).thenReturn(true);
        assertThrows(DocumentNumberAlreadyExistsException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }
}
