package com.pragma.powerup.domain.validator.strategy;

import com.pragma.powerup.domain.exception.FieldsValidationException;
import com.pragma.powerup.domain.model.UserModel;

import java.util.LinkedHashMap;
import java.util.Map;

public class EmployeeValidationStrategy extends AbstractUserValidationStrategy {

    @Override
    public void validate(UserModel user) {
        Map<String, String> errors = new LinkedHashMap<>();
        collectCommonErrors(user, errors);
        if (!errors.isEmpty())
            throw new FieldsValidationException(errors);
    }
}
