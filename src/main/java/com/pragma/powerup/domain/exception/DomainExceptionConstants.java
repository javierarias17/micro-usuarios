package com.pragma.powerup.domain.exception;

public class DomainExceptionConstants {
    private DomainExceptionConstants() {
        throw new IllegalStateException("Utility class");
    }
    public static final String BIRTH_DATE = "birthDate";
    public static final String EMAIL = "email";
    public static final String DOCUMENT_NUMBER = "documentNumber";
}
