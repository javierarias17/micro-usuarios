package com.pragma.powerup.domain.common;

public class ValidationMessageConstants {

    private ValidationMessageConstants() {
        throw new IllegalStateException("Utility class");
    }

    // Name
    public static final String MSG_NAME_REQUIRED = "Name is required";
    public static final String MSG_LAST_NAME_REQUIRED = "Last name is required";

    // Document
    public static final String MSG_DOCUMENT_NUMBER_REQUIRED = "Document number is required";
    public static final String MSG_DOCUMENT_NUMBER_DIGITS_ONLY = "Document number must contain only digits";

    // Phone
    public static final String MSG_PHONE_REQUIRED = "Phone is required";
    public static final String MSG_PHONE_FORMAT = "Phone must contain at most 13 characters and must start with + if provided";

    // Date
    public static final String MSG_BIRTH_DATE_REQUIRED = "Birth date is required";

    // Email
    public static final String MSG_EMAIL_REQUIRED = "Email is required";
    public static final String MSG_EMAIL_FORMAT = "Email must have a valid format";

    // Password
    public static final String MSG_PASSWORD_REQUIRED = "Password is required";

    // Restaurant
    public static final String MSG_RESTAURANT_ID_REQUIRED = "Restaurant ID is required";
}
