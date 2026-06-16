package com.pragma.powerup.domain.validator;

import com.pragma.powerup.domain.model.UserModel;
import com.pragma.powerup.domain.validator.strategy.UserValidationStrategy;

public class UserValidator {

    private final UserValidationStrategy strategy;

    public UserValidator(UserValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public void validate(UserModel user) {
        strategy.validate(user);
    }
}
