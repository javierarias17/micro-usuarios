package com.pragma.powerup.domain.validator.strategy;

import com.pragma.powerup.domain.common.FieldConstants;
import com.pragma.powerup.domain.common.ValidationMessageConstants;
import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.validator.FieldValidator;

import java.util.LinkedHashMap;
import java.util.Map;

public class OwnerValidationStrategy extends AbstractUserValidationStrategy {

    @Override
    public void validate(UserModel user) {
        Map<String, String> errors = new LinkedHashMap<>();
        collectCommonErrors(user, errors);
        FieldValidator.validateNotNull(user.getBirthDate(), FieldConstants.BIRTH_DATE,
                ValidationMessageConstants.MSG_BIRTH_DATE_REQUIRED, errors);
        if (!errors.isEmpty())
            throw new FieldsValidationException(errors);
    }
}
