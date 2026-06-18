package com.pragma.powerup.domain.model.valueobject;

import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
import com.pragma.powerup.domain.exception.FieldsValidationException;

import java.util.LinkedHashMap;
import java.util.Map;

public record UserPassword(String value) {

    public UserPassword {
        Map<String, String> errors = new LinkedHashMap<>();
        validate(value, errors);
        if (!errors.isEmpty())
            throw new FieldsValidationException(errors);
    }

    public static void validate(String value, Map<String, String> errors) {
        if (value == null || value.isBlank())
            errors.put(FieldConstants.PASSWORD, ValidationMessageConstants.MSG_PASSWORD_REQUIRED);
    }
}
