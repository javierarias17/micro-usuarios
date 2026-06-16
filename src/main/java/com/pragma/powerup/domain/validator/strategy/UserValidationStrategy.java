package com.pragma.powerup.domain.validator.strategy;

import com.pragma.powerup.domain.model.UserModel;

public interface UserValidationStrategy {
    void validate(UserModel user);
}
