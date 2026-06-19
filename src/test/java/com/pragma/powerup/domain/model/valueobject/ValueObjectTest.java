package com.pragma.powerup.domain.model.valueobject;

import com.pragma.powerup.domain.common.DomainConstants;
import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ValueObjectTest {

    private static final String VALID_NAME = "Armando";
    private static final String VALID_LAST_NAME = "Diaz";
    private static final String BLANK_VALUE = "  ";
    private static final String VALID_DOCUMENT_NUMBER = "1061769969";
    private static final String INVALID_DOCUMENT_NUMBER = "ABC-123";
    private static final String VALID_EMAIL = "user@example.com";
    private static final String INVALID_EMAIL = "not-an-email";
    private static final String VALID_PASSWORD = "secret123";
    private static final String VALID_PHONE = "+573197633852";
    private static final String VALID_PHONE_WITHOUT_PREFIX = "3197633852";
    private static final String INVALID_PHONE = "++invalid++phone";
    private static final LocalDate VALID_BIRTH_DATE = LocalDate.of(1990, 1, 1);
    private static final Long VALID_RESTAURANT_ID = 1L;

    // ─── UserName ──────────────────────────────────────────────────────────────

    @Nested
    class UserNameTest {

        @Test
        void When_ValidName_Expect_UserNameCreated() {
            UserName name = new UserName(VALID_NAME);
            assertEquals(VALID_NAME, name.value());
        }

        @Test
        void Expect_FieldsValidationException_When_NameIsNull() {
            assertThrows(FieldsValidationException.class, () -> new UserName(null));
        }

        @Test
        void Expect_FieldsValidationException_When_NameIsBlank() {
            assertThrows(FieldsValidationException.class, () -> new UserName(BLANK_VALUE));
        }

        @Test
        void validate_WhenBlank_AddsError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserName.validate(BLANK_VALUE, errors);
            assertTrue(errors.containsKey(FieldConstants.NAME));
        }

        @Test
        void validate_WhenValid_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserName.validate(VALID_NAME, errors);
            assertTrue(errors.isEmpty());
        }
    }

    // ─── UserLastName ──────────────────────────────────────────────────────────

    @Nested
    class UserLastNameTest {

        @Test
        void When_ValidLastName_Expect_UserLastNameCreated() {
            UserLastName lastName = new UserLastName(VALID_LAST_NAME);
            assertEquals(VALID_LAST_NAME, lastName.value());
        }

        @Test
        void Expect_FieldsValidationException_When_LastNameIsNull() {
            assertThrows(FieldsValidationException.class, () -> new UserLastName(null));
        }

        @Test
        void Expect_FieldsValidationException_When_LastNameIsBlank() {
            assertThrows(FieldsValidationException.class, () -> new UserLastName(BLANK_VALUE));
        }

        @Test
        void validate_WhenBlank_AddsError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserLastName.validate(null, errors);
            assertTrue(errors.containsKey(FieldConstants.LAST_NAME));
        }

        @Test
        void validate_WhenValid_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserLastName.validate(VALID_LAST_NAME, errors);
            assertTrue(errors.isEmpty());
        }
    }

    // ─── UserDocumentNumber ────────────────────────────────────────────────────

    @Nested
    class UserDocumentNumberTest {

        @Test
        void When_ValidDocumentNumber_Expect_Created() {
            UserDocumentNumber doc = new UserDocumentNumber(VALID_DOCUMENT_NUMBER);
            assertEquals(VALID_DOCUMENT_NUMBER, doc.value());
        }

        @Test
        void Expect_FieldsValidationException_When_DocumentNumberIsNull() {
            assertThrows(FieldsValidationException.class, () -> new UserDocumentNumber(null));
        }

        @Test
        void Expect_FieldsValidationException_When_DocumentNumberIsBlank() {
            assertThrows(FieldsValidationException.class, () -> new UserDocumentNumber(BLANK_VALUE));
        }

        @Test
        void Expect_FieldsValidationException_When_DocumentNumberHasNonDigits() {
            assertThrows(FieldsValidationException.class, () -> new UserDocumentNumber(INVALID_DOCUMENT_NUMBER));
        }

        @Test
        void validate_WhenNull_AddsRequiredError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserDocumentNumber.validate(null, errors);
            assertTrue(errors.containsKey(FieldConstants.DOCUMENT_NUMBER));
        }

        @Test
        void validate_WhenNonDigits_AddsFormatError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserDocumentNumber.validate(INVALID_DOCUMENT_NUMBER, errors);
            assertTrue(errors.containsKey(FieldConstants.DOCUMENT_NUMBER));
        }

        @Test
        void validate_WhenValid_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserDocumentNumber.validate(VALID_DOCUMENT_NUMBER, errors);
            assertTrue(errors.isEmpty());
        }
    }

    // ─── UserEmail ─────────────────────────────────────────────────────────────

    @Nested
    class UserEmailTest {

        @Test
        void When_ValidEmail_Expect_Created() {
            UserEmail email = new UserEmail(VALID_EMAIL);
            assertEquals(VALID_EMAIL, email.value());
        }

        @Test
        void Expect_FieldsValidationException_When_EmailIsNull() {
            assertThrows(FieldsValidationException.class, () -> new UserEmail(null));
        }

        @Test
        void Expect_FieldsValidationException_When_EmailIsBlank() {
            assertThrows(FieldsValidationException.class, () -> new UserEmail(BLANK_VALUE));
        }

        @Test
        void Expect_FieldsValidationException_When_EmailHasInvalidFormat() {
            assertThrows(FieldsValidationException.class, () -> new UserEmail(INVALID_EMAIL));
        }

        @Test
        void validate_WhenNull_AddsRequiredError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserEmail.validate(null, errors);
            assertTrue(errors.containsKey(FieldConstants.EMAIL));
        }

        @Test
        void validate_WhenInvalidFormat_AddsFormatError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserEmail.validate(INVALID_EMAIL, errors);
            assertTrue(errors.containsKey(FieldConstants.EMAIL));
        }

        @Test
        void validate_WhenValid_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserEmail.validate(VALID_EMAIL, errors);
            assertTrue(errors.isEmpty());
        }
    }

    // ─── UserPassword ──────────────────────────────────────────────────────────

    @Nested
    class UserPasswordTest {

        @Test
        void When_ValidPassword_Expect_Created() {
            UserPassword password = new UserPassword(VALID_PASSWORD);
            assertEquals(VALID_PASSWORD, password.value());
        }

        @Test
        void Expect_FieldsValidationException_When_PasswordIsNull() {
            assertThrows(FieldsValidationException.class, () -> new UserPassword(null));
        }

        @Test
        void Expect_FieldsValidationException_When_PasswordIsBlank() {
            assertThrows(FieldsValidationException.class, () -> new UserPassword(BLANK_VALUE));
        }

        @Test
        void validate_WhenNull_AddsError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserPassword.validate(null, errors);
            assertTrue(errors.containsKey(FieldConstants.PASSWORD));
        }

        @Test
        void validate_WhenValid_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserPassword.validate(VALID_PASSWORD, errors);
            assertTrue(errors.isEmpty());
        }
    }

    // ─── UserPhone ─────────────────────────────────────────────────────────────

    @Nested
    class UserPhoneTest {

        @Test
        void When_ValidPhoneWithPlus_Expect_Created() {
            UserPhone phone = new UserPhone(VALID_PHONE);
            assertEquals(VALID_PHONE, phone.value());
        }

        @Test
        void When_ValidPhoneWithoutPlus_Expect_Created() {
            UserPhone phone = new UserPhone(VALID_PHONE_WITHOUT_PREFIX);
            assertEquals(VALID_PHONE_WITHOUT_PREFIX, phone.value());
        }

        @Test
        void Expect_FieldsValidationException_When_PhoneIsNull() {
            assertThrows(FieldsValidationException.class, () -> new UserPhone(null));
        }

        @Test
        void Expect_FieldsValidationException_When_PhoneIsBlank() {
            assertThrows(FieldsValidationException.class, () -> new UserPhone(BLANK_VALUE));
        }

        @Test
        void Expect_FieldsValidationException_When_PhoneHasInvalidFormat() {
            assertThrows(FieldsValidationException.class, () -> new UserPhone(INVALID_PHONE));
        }

        @Test
        void validate_WhenNull_AddsRequiredError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserPhone.validate(null, errors);
            assertTrue(errors.containsKey(FieldConstants.PHONE));
        }

        @Test
        void validate_WhenInvalidFormat_AddsFormatError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserPhone.validate(INVALID_PHONE, errors);
            assertTrue(errors.containsKey(FieldConstants.PHONE));
        }

        @Test
        void validate_WhenValid_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserPhone.validate(VALID_PHONE, errors);
            assertTrue(errors.isEmpty());
        }
    }

    // ─── UserBirthDate ─────────────────────────────────────────────────────────

    @Nested
    class UserBirthDateTest {

        @Test
        void validate_WhenOwnerRoleAndNullDate_AddsError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserBirthDate.validate(null, DomainConstants.OWNER_ROLE_ID, errors);
            assertTrue(errors.containsKey(FieldConstants.BIRTH_DATE));
        }

        @Test
        void validate_WhenOwnerRoleAndValidDate_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserBirthDate.validate(VALID_BIRTH_DATE, DomainConstants.OWNER_ROLE_ID, errors);
            assertTrue(errors.isEmpty());
        }

        @Test
        void validate_WhenNonOwnerRoleAndNullDate_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserBirthDate.validate(null, DomainConstants.EMPLOYEE_ROLE_ID, errors);
            assertTrue(errors.isEmpty());
        }

        @Test
        void validate_WhenNullRoleAndNullDate_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            UserBirthDate.validate(null, null, errors);
            assertTrue(errors.isEmpty());
        }
    }

    // ─── RestaurantId ──────────────────────────────────────────────────────────

    @Nested
    class RestaurantIdTest {

        @Test
        void When_ValidRestaurantId_Expect_Created() {
            RestaurantId restaurantId = new RestaurantId(VALID_RESTAURANT_ID);
            assertEquals(VALID_RESTAURANT_ID, restaurantId.value());
        }

        @Test
        void validate_WhenEmployeeRoleAndNullValue_AddsError() {
            Map<String, String> errors = new LinkedHashMap<>();
            RestaurantId.validate(null, DomainConstants.EMPLOYEE_ROLE_ID, errors);
            assertTrue(errors.containsKey(FieldConstants.RESTAURANT_ID));
        }

        @Test
        void validate_WhenEmployeeRoleAndValidValue_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            RestaurantId.validate(VALID_RESTAURANT_ID, DomainConstants.EMPLOYEE_ROLE_ID, errors);
            assertTrue(errors.isEmpty());
        }

        @Test
        void validate_WhenNonEmployeeRoleAndNullValue_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            RestaurantId.validate(null, DomainConstants.OWNER_ROLE_ID, errors);
            assertTrue(errors.isEmpty());
        }

        @Test
        void validate_WhenNullRoleAndNullValue_NoError() {
            Map<String, String> errors = new LinkedHashMap<>();
            RestaurantId.validate(null, null, errors);
            assertTrue(errors.isEmpty());
        }
    }
}
