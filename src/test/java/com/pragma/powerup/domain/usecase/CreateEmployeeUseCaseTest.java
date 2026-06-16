package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.model.RoleModel;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IUserPersistencePort;
import com.pragma.powerup.domain.validator.UserValidator;
import com.pragma.powerup.domain.validator.strategy.EmployeeValidationStrategy;
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
class CreateEmployeeUseCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    private CreateEmployeeUseCase createEmployeeUseCase;

    private UserModel validEmployee;
    private RoleModel employeeRole;

    @BeforeEach
    void setUp() {
        employeeRole = UserModelFactory.createEmployeeRole();
        validEmployee = UserModelFactory.createValidUser();
        createEmployeeUseCase = new CreateEmployeeUseCase(
                userPersistencePort, passwordEncoderPort,
                new UserValidator(new EmployeeValidationStrategy()));
    }

    // ─── Happy path

    @Test
    void When_EmployeeInformationIsCorrect_Expect_EmployeeToBeSavedSuccessfully() {
        // Arrange
        UserModel savedEmployee = UserModelFactory.createSavedUser(employeeRole);

        when(userPersistencePort.existsByEmail(validEmployee.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validEmployee.getDocumentNumber())).thenReturn(false);
        when(passwordEncoderPort.encode(validEmployee.getPassword())).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedEmployee);

        // Act
        UserModel result = createEmployeeUseCase.createEmployee(validEmployee);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getId());
        assertEquals(savedEmployee.getName(), result.getName());
        assertEquals(savedEmployee.getLastName(), result.getLastName());
        assertEquals(savedEmployee.getDocumentNumber(), result.getDocumentNumber());
        assertEquals(savedEmployee.getPhone(), result.getPhone());
        assertEquals(savedEmployee.getBirthDate(), result.getBirthDate());
        assertEquals(savedEmployee.getEmail(), result.getEmail());
        assertEquals(employeeRole, result.getRole());
    }

    @Test
    void When_EmployeeInformationIsCorrect_Expect_PasswordToBeEncoded() {
        // Arrange
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(passwordEncoderPort.encode("secret123")).thenReturn("encodedPassword");
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(validEmployee);

        // Act
        UserModel result = createEmployeeUseCase.createEmployee(validEmployee);

        // Assert
        assertEquals("encodedPassword", result.getPassword());
    }

    // ─── Exceptions path

    @Test
    void Expect_FieldsValidationException_When_NameIsBlank() {
        // Arrange
        validEmployee.setName("");

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_LastNameIsBlank() {
        // Arrange
        validEmployee.setLastName(null);

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberIsBlank() {
        // Arrange
        validEmployee.setDocumentNumber("  ");

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberHasInvalidFormat() {
        // Arrange
        validEmployee.setDocumentNumber("ABC-123");

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneIsBlank() {
        // Arrange
        validEmployee.setPhone(null);

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneHasInvalidFormat() {
        // Arrange
        validEmployee.setPhone("not-a-phone");

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailIsBlank() {
        // Arrange
        validEmployee.setEmail("");

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
        // Arrange
        validEmployee.setEmail("invalid-email");

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_FieldsValidationException_When_PasswordIsBlank() {
        // Arrange
        validEmployee.setPassword(null);

        // Act & Assert
        assertThrows(FieldsValidationException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_MailAlreadyExistsException_When_EmailIsAlreadyRegistered() {
        // Arrange
        when(userPersistencePort.existsByEmail(validEmployee.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(MailAlreadyExistsException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }

    @Test
    void Expect_DocumentNumberAlreadyExistsException_When_DocumentNumberIsAlreadyRegistered() {
        // Arrange
        when(userPersistencePort.existsByEmail(validEmployee.getEmail())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(validEmployee.getDocumentNumber())).thenReturn(true);

        // Act & Assert
        assertThrows(DocumentNumberAlreadyExistsException.class,
                () -> createEmployeeUseCase.createEmployee(validEmployee));
    }
}
