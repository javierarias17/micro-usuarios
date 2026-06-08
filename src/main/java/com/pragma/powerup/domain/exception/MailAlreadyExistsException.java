package com.pragma.powerup.domain.exception;

import java.util.Map;

public class MailAlreadyExistsException extends FunctionalException {

    public MailAlreadyExistsException(String message, Map<String,String> error) {
        super(message, error);
    }
}
