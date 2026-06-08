package com.pragma.powerup.domain.exception;

import java.util.Map;

public class DocumentNumberAlreadyExistsException extends FunctionalException {

    public DocumentNumberAlreadyExistsException(String message, Map<String,String> error) {
        super(message, error);
    }
}
