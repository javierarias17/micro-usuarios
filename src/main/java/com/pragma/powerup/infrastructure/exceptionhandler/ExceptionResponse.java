package com.pragma.powerup.infrastructure.exceptionhandler;

public enum ExceptionResponse {
    NO_DATA_FOUND("No data found for the requested petition"),
    MAIL_ALREADY_EXISTS("Mail already exists in the system"),
    DOCUMENT_NUMBER_ALREADY_EXISTS("Document number already exists in the system"),
    NOT_ADULT("The user must be of legal age (18+)"),
    ROL_NOT_FOUND("Role not found");

    private final String message;

    ExceptionResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }
}
