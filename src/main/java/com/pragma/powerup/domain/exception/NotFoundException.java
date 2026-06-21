package com.pragma.powerup.domain.exception;

import java.util.Map;

public class NotFoundException extends FunctionalException {
    public static final String NOT_FOUND = "Resource not found";

    public NotFoundException() {
        super(NOT_FOUND, Map.of());
    }
}
