package com.pragma.powerup.domain.exception;

import lombok.Getter;

@Getter
public enum FunctionalExceptionResponse {
    BUSINESS_VALIDATION_FAILED("Business validation failed"),
    MAIL_ALREADY_EXISTS("Mail already exists in the system"),
    DOCUMENT_NUMBER_ALREADY_EXISTS("Document number already exists in the system"),
    NOT_ADULT("The user must be of legal age (18+)"),
    USER_NOT_FOUND("User not found in the system"),
    INVALID_CREDENTIALS("Invalid email or password");


    private final String message;

    FunctionalExceptionResponse(String message) {
        this.message = message;
    }

}
