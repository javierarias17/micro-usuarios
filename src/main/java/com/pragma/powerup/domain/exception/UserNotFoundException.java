package com.pragma.powerup.domain.exception;

import com.pragma.powerup.domain.exception.constant.FunctionalMessageConstants;

import java.util.Map;

public class UserNotFoundException extends FunctionalException {
    public UserNotFoundException() {
        super(FunctionalMessageConstants.USER_NOT_FOUND, Map.of());
    }
}
