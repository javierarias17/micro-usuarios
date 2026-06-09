package com.pragma.powerup.domain.exception;

import lombok.Getter;

@Getter
public enum TechnicalExceptionResponse {
    ROLE_NOT_FOUND("Role not found in the system");

    private final String message;

    TechnicalExceptionResponse(String message) {
        this.message = message;
    }
}
