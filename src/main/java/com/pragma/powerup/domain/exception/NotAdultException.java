package com.pragma.powerup.domain.exception;

import java.util.Map;

public class NotAdultException extends FunctionalException {

    public NotAdultException(String message, Map<String,String> error) {
        super(message, error);
    }
}
