package com.pragma.powerup.domain.usecase;

import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.exception.ForbiddenException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.model.UserCreateCommand;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.spi.IPasswordEncoderPort;
import com.pragma.powerup.domain.spi.IPlazoletaServicePort;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateEmployeeUseCaseTest {

    private static final String VALID_NAME = "Armando";
    private static final String VALID_LAST_NAME = "Diaz";
    private static final String VALID_DOCUMENT_NUMBER = "1061769969";
    private static final String VALID_PHONE = "+573197633852";
    private static final String VALID_EMAIL = "armando-diaz@gmail.com";
    private static final String RAW_PASSWORD = "secret123";
    private static final String ENCODED_PASSWORD = "encodedPassword";
    private static final Long VALID_RESTAURANT_ID = 1L;

    private static final String BLANK_VALUE = "  ";
    private static final String INVALID_DOCUMENT_NUMBER = "ABC-123";
    private static final String INVALID_PHONE = "not-a-phone";
    private static final String INVALID_EMAIL = "invalid-email";

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncoderPort passwordEncoderPort;

    @Mock
    private IPlazoletaServicePort plazoletaServicePort;

    private CreateEmployeeUseCase createEmployeeUseCase;
    private UserCreateCommand validCommand;

    @BeforeEach
    void setUp() {
        validCommand = UserModelFactory.createEmployeeCommand();
        createEmployeeUseCase = new CreateEmployeeUseCase(
                userPersistencePort, passwordEncoderPort, plazoletaServicePort);
    }

    // ─── Happy path

    @Test
    void When_EmployeeInformationIsCorrect_Expect_EmployeeToBeSavedSuccessfully() {
        UserModel savedEmployee = UserModelFactory.createSavedEmployee();

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(plazoletaServicePort.isOwnerOfRestaurant(anyLong())).thenReturn(true);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(any(UserModel.class))).thenReturn(savedEmployee);

        UserModel result = createEmployeeUseCase.createEmployee(validCommand);

        assertNotNull(result);
        assertEquals(savedEmployee.getId(), result.getId());
        assertEquals(savedEmployee.getName(), result.getName());
        assertEquals(savedEmployee.getLastName(), result.getLastName());
        assertEquals(savedEmployee.getDocumentNumber(), result.getDocumentNumber());
        assertEquals(savedEmployee.getPhone(), result.getPhone());
        assertEquals(savedEmployee.getEmail(), result.getEmail());
        assertEquals(VALID_RESTAURANT_ID, result.getRestaurantId().value());
    }

    @Test
    void When_EmployeeInformationIsCorrect_Expect_PasswordToBeEncoded() {
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel savedEmployee = UserModelFactory.createSavedEmployeeWithPassword(ENCODED_PASSWORD);

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(plazoletaServicePort.isOwnerOfRestaurant(anyLong())).thenReturn(true);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(captor.capture())).thenReturn(savedEmployee);

        createEmployeeUseCase.createEmployee(validCommand);

        assertEquals(ENCODED_PASSWORD, captor.getValue().getPassword().value());
    }

    @Test
    void When_EmployeeInformationIsCorrect_Expect_EmployeeRoleToBeAssigned() {
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel savedEmployee = UserModelFactory.createSavedEmployee();

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(plazoletaServicePort.isOwnerOfRestaurant(anyLong())).thenReturn(true);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(captor.capture())).thenReturn(savedEmployee);

        createEmployeeUseCase.createEmployee(validCommand);

        assertEquals(DomainConstants.EMPLOYEE_ROLE_ID, captor.getValue().getRole().getId());
    }

    @Test
    void When_EmployeeInformationIsCorrect_Expect_RestaurantIdToBeAssigned() {
        ArgumentCaptor<UserModel> captor = ArgumentCaptor.forClass(UserModel.class);
        UserModel savedEmployee = UserModelFactory.createSavedEmployee();

        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(plazoletaServicePort.isOwnerOfRestaurant(anyLong())).thenReturn(true);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(false);
        when(userPersistencePort.saveUser(captor.capture())).thenReturn(savedEmployee);

        createEmployeeUseCase.createEmployee(validCommand);

        assertEquals(VALID_RESTAURANT_ID, captor.getValue().getRestaurantId().value());
    }

    // ─── Validation exceptions

    @Test
    void Expect_FieldsValidationException_When_RestaurantIdIsNull() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, null);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_NameIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                BLANK_VALUE, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_LastNameIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, null, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, BLANK_VALUE,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_DocumentNumberHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, INVALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                null, null, VALID_EMAIL, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PhoneHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                INVALID_PHONE, null, VALID_EMAIL, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, BLANK_VALUE, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, INVALID_EMAIL, RAW_PASSWORD, VALID_RESTAURANT_ID);
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    @Test
    void Expect_FieldsValidationException_When_PasswordIsBlank() {
        UserCreateCommand cmd = new UserCreateCommand(
                VALID_NAME, VALID_LAST_NAME, VALID_DOCUMENT_NUMBER,
                VALID_PHONE, null, VALID_EMAIL, null, VALID_RESTAURANT_ID);
        assertThrows(FieldsValidationException.class, () -> createEmployeeUseCase.createEmployee(cmd));
    }

    // ─── Business rule exceptions

    @Test
    void Expect_ForbiddenException_When_AuthenticatedUserIsNotRestaurantOwner() {
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(plazoletaServicePort.isOwnerOfRestaurant(anyLong())).thenReturn(false);

        assertThrows(ForbiddenException.class,
                () -> createEmployeeUseCase.createEmployee(validCommand));
    }

    @Test
    void Expect_MailAlreadyExistsException_When_EmailIsAlreadyRegistered() {
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(plazoletaServicePort.isOwnerOfRestaurant(anyLong())).thenReturn(true);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(true);

        assertThrows(MailAlreadyExistsException.class,
                () -> createEmployeeUseCase.createEmployee(validCommand));
    }

    @Test
    void Expect_DocumentNumberAlreadyExistsException_When_DocumentNumberIsAlreadyRegistered() {
        when(passwordEncoderPort.encode(anyString())).thenReturn(ENCODED_PASSWORD);
        when(plazoletaServicePort.isOwnerOfRestaurant(anyLong())).thenReturn(true);
        when(userPersistencePort.existsByEmail(anyString())).thenReturn(false);
        when(userPersistencePort.existsByDocumentNumber(anyString())).thenReturn(true);

        assertThrows(DocumentNumberAlreadyExistsException.class,
                () -> createEmployeeUseCase.createEmployee(validCommand));
    }
}
