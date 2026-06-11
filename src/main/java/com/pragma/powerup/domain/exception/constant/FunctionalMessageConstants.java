package com.pragma.powerup.domain.exception.constant;

public class FunctionalMessageConstants {

    private FunctionalMessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String BUSINESS_VALIDATION_FAILED = "Business validation failed";
    public static final String MAIL_ALREADY_EXISTS = "Mail already exists in the system";
    public static final String DOCUMENT_NUMBER_ALREADY_EXISTS = "Document number already exists in the system";
    public static final String OWNER_NOT_OF_LEGAL_AGE = "The owner must be of legal age (18+)";
    public static final String USER_NOT_FOUND = "User not found in the system";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";
}
