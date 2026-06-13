package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.model.RoleModel;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateCustomerUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @InjectMocks
    private CreateCustomerUseCase createCustomerUseCase;

    private UserModel validCustomer;
    private RoleModel customerRole;

    @BeforeEach
    void setUp() {
        customerRole = UserModelFactory.createCustomerRole();
        validCustomer = UserModelFactory.createValidUser();
    }

    // ─── Happy path

    @Test
    void When_CustomerInformationIsCorrect_Expect_CustomerToBeSavedSuccessfully() {
        // Arrange
        UserModel savedCustomer = UserModelFactory.createSavedUser(customerRole);

        when(userPersistencePort.existsByEmail(validCustomer.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validCustomer.getDocumentNumber())).thenReturn(false);
        when(passwordEncoderPort.encode(validCustomer.getPassword())).thenReturn("encodedPassword");
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
        when(passwordEncoderPort.encode("secret123")).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(validCustomer);

        // Act
        UserModel result = createCustomerUseCase.createCustomer(validCustomer);

        // Assert
        assertEquals("encodedPassword", result.getPassword());
    }

    // ─── Field validation exceptions

    @Test
    void Expect_FieldsValidationException_When_NameIsBlank() {
        validCustomer.setName("");
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
        validCustomer.setDocumentNumber("  ");
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberHasInvalidFormat() {
        validCustomer.setDocumentNumber("ABC-123");
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
        validCustomer.setPhone("not-a-phone");
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailIsBlank() {
        validCustomer.setEmail("");
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
        validCustomer.setEmail("invalid-email");
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    @Test
    void Expect_FieldsValidationException_When_PasswordIsBlank() {
        validCustomer.setPassword(null);
        assertThrows(FieldsValidationException.class,
                () -> createCustomerUseCase.createCustomer(validCustomer));
    }

    // ─── Business rule exceptions

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
