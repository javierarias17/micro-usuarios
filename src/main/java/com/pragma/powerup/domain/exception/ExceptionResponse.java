package com.pragma.powerup.domain.exception;

import lombok.Getter;

@Getter
public enum ExceptionResponse {
    BUSINESS_VALIDATION_FAILED("Business validation failed"),
    MAIL_ALREADY_EXISTS("Mail already exists in the system"),
    DOCUMENT_NUMBER_ALREADY_EXISTS("Document number already exists in the system"),
    NOT_ADULT("The user must be of legal age (18+)");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

}
