package com.pragma.powerup.domain.validator;

import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.common.RegexConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.model.UserModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class UserValidator {

    private UserValidator() {
        throw new IllegalStateException("Utility class");
    }

    public static void validateForEmployeeCreation(UserModel user) {
        Map<String, String> errors = new LinkedHashMap<>();

        FieldValidator.validateNotBlank(user.getName(), FieldConstants.NAME,
                ValidationMessageConstants.MSG_NAME_REQUIRED, errors);

        FieldValidator.validateNotBlank(user.getLastName(), FieldConstants.LAST_NAME,
                ValidationMessageConstants.MSG_LAST_NAME_REQUIRED, errors);

        FieldValidator.validateNotBlankAndPattern(
                user.getDocumentNumber(),
                FieldConstants.DOCUMENT_NUMBER,
                ValidationMessageConstants.MSG_DOCUMENT_NUMBER_REQUIRED,
                RegexConstants.DOCUMENT_NUMBER_REGEX,
                ValidationMessageConstants.MSG_DOCUMENT_NUMBER_DIGITS_ONLY,
                errors);

        FieldValidator.validateNotBlankAndPattern(
                user.getPhone(),
                FieldConstants.PHONE,
                ValidationMessageConstants.MSG_PHONE_REQUIRED,
                RegexConstants.PHONE_REGEX,
                ValidationMessageConstants.MSG_PHONE_FORMAT,
                errors);

        FieldValidator.validateNotBlankAndPattern(
                user.getEmail(),
                FieldConstants.EMAIL,
                ValidationMessageConstants.MSG_EMAIL_REQUIRED,
                RegexConstants.EMAIL_REGEX,
                ValidationMessageConstants.MSG_EMAIL_FORMAT,
                errors);

        FieldValidator.validateNotBlank(user.getPassword(), FieldConstants.PASSWORD,
                ValidationMessageConstants.MSG_PASSWORD_REQUIRED, errors);

        if (!errors.isEmpty())
            throw new FieldsValidationException(errors);
    }
}
