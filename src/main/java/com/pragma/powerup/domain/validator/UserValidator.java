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

        if (isBlank(user.getName()))
            errors.put(FieldConstants.NAME, ValidationMessageConstants.MSG_NAME_REQUIRED);

        if (isBlank(user.getLastName()))
            errors.put(FieldConstants.LAST_NAME, ValidationMessageConstants.MSG_LAST_NAME_REQUIRED);

        if (isBlank(user.getDocumentNumber()))
            errors.put(FieldConstants.DOCUMENT_NUMBER, ValidationMessageConstants.MSG_DOCUMENT_NUMBER_REQUIRED);
        else if (!user.getDocumentNumber().matches(RegexConstants.DOCUMENT_NUMBER_REGEX))
            errors.put(FieldConstants.DOCUMENT_NUMBER, ValidationMessageConstants.MSG_DOCUMENT_NUMBER_DIGITS_ONLY);

        if (isBlank(user.getPhone()))
            errors.put(FieldConstants.PHONE, ValidationMessageConstants.MSG_PHONE_REQUIRED);
        else if (!user.getPhone().matches(RegexConstants.PHONE_REGEX))
            errors.put(FieldConstants.PHONE, ValidationMessageConstants.MSG_PHONE_FORMAT);

        if (isBlank(user.getEmail()))
            errors.put(FieldConstants.EMAIL, ValidationMessageConstants.MSG_EMAIL_REQUIRED);
        else if (!user.getEmail().matches(RegexConstants.EMAIL_REGEX))
            errors.put(FieldConstants.EMAIL, ValidationMessageConstants.MSG_EMAIL_FORMAT);

        if (isBlank(user.getPassword()))
            errors.put(FieldConstants.PASSWORD, ValidationMessageConstants.MSG_PASSWORD_REQUIRED);

        if (!errors.isEmpty())
            throw new FieldsValidationException(errors);
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
