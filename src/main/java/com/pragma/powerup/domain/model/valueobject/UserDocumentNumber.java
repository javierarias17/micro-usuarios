package com.pragma.powerup.domain.model.valueobject;

import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.common.RegexConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
import com.pragma.powerup.domain.exception.FieldsValidationException;

import java.util.LinkedHashMap;
import java.util.Map;

public record UserDocumentNumber(String value) {

    public UserDocumentNumber {
        Map<String, String> errors = new LinkedHashMap<>();
        validate(value, errors);
        if (!errors.isEmpty())
            throw new FieldsValidationException(errors);
    }

    public static void validate(String value, Map<String, String> errors) {
        if (value == null || value.isBlank())
            errors.put(FieldConstants.DOCUMENT_NUMBER, ValidationMessageConstants.MSG_DOCUMENT_NUMBER_REQUIRED);
        else if (!value.matches(RegexConstants.DOCUMENT_NUMBER_REGEX))
            errors.put(FieldConstants.DOCUMENT_NUMBER, ValidationMessageConstants.MSG_DOCUMENT_NUMBER_DIGITS_ONLY);
    }
}
