package com.pragma.powerup.domain.exception;

import java.util.Collections;

public class InvalidCredentialsException extends FunctionalException {
    public InvalidCredentialsException(String message) {
        super(message, Collections.emptyMap());
    }
}
