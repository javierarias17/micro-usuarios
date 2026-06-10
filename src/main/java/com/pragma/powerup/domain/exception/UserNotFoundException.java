package com.pragma.powerup.domain.exception;

import java.util.Map;

public class UserNotFoundException extends FunctionalException {
    public UserNotFoundException() {
        super(FunctionalExceptionResponse.USER_NOT_FOUND.getMessage(), Map.of());
    }
}
