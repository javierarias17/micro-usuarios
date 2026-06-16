package com.pragma.powerup.domain.validator.strategy;

import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.common.RegexConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.validator.FieldValidator;

import java.util.Map;

abstract class AbstractUserValidationStrategy implements UserValidationStrategy {

    protected void collectCommonErrors(UserModel user, Map<String, String> errors) {
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
    }
}
