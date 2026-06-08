package com.pragma.powerup.infrastructure.exceptionhandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.pragma.powerup.domain.exception.DocumentNumberAlreadyExistsException;
import com.pragma.powerup.domain.exception.FunctionalException;
import com.pragma.powerup.domain.exception.MailAlreadyExistsException;
import com.pragma.powerup.domain.exception.NotAdultException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class ControllerAdvisor {

        private static final String AN_UNEXPECTED_ERROR_OCCURRED = "An unexpected error occurred. Please contact the administrator.";
        private static final String VALIDATION_FAILED = "Validation failed";
        private static final String INVALID_REQUEST_BODY = "Invalid request body";
        private static final String ERRORS = "errors";
        private static final String FIELD = "field";
        private static final String MESSAGE = "message";

        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<Map<String, Object>> handleHttpMessageNotReadableException(
                        HttpMessageNotReadableException ex) {

                Throwable cause = ex.getCause();

                if (cause instanceof InvalidFormatException invalidFormatException
                                && !invalidFormatException.getPath().isEmpty()) {

                        String fieldName = invalidFormatException.getPath().get(0).getFieldName();
                        String expectedType = invalidFormatException.getTargetType().getSimpleName();

                        Map<String, String> detail = new LinkedHashMap<>();
                        detail.put(FIELD, fieldName);
                        detail.put(MESSAGE, "Invalid value for field, expected type " + expectedType);

                        Map<String, Object> response = new LinkedHashMap<>();
                        response.put(MESSAGE, INVALID_REQUEST_BODY);
                        response.put(ERRORS, Collections.singletonList(detail));
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
                }

                Map<String, Object> response = new LinkedHashMap<>();
                response.put(MESSAGE, INVALID_REQUEST_BODY);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<Map<String, Object>> handleValidationExceptions(
                        MethodArgumentNotValidException ex) {

                List<Map<String, String>> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(error -> {
                                        Map<String, String> errorDetail = new HashMap<>();
                                        errorDetail.put(FIELD, error.getField());
                                        errorDetail.put(MESSAGE, error.getDefaultMessage());
                                        return errorDetail;
                                })
                                .toList();

                Map<String, Object> response = new LinkedHashMap<>();
                response.put(MESSAGE, VALIDATION_FAILED);
                response.put(ERRORS, errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        @ExceptionHandler(MailAlreadyExistsException.class)
        public ResponseEntity<Map<String, Object>> handleMailAlreadyExistsException(
                        MailAlreadyExistsException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrorResponse(ex));
        }

        @ExceptionHandler(DocumentNumberAlreadyExistsException.class)
        public ResponseEntity<Map<String, Object>> handleDocumentNumberAlreadyExistsException(
                        DocumentNumberAlreadyExistsException ex) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(buildErrorResponse(ex));
        }

        @ExceptionHandler(NotAdultException.class)
        public ResponseEntity<Map<String, Object>> handleNotAdultException(
                        NotAdultException ex) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(buildErrorResponse(ex));
        }

        private Map<String, Object> buildErrorResponse(FunctionalException ex) {
                List<Map<String, String>> errorList = ex.getErrors().entrySet().stream()
                                .map(entry -> {
                                        Map<String, String> detail = new LinkedHashMap<>();
                                        detail.put(FIELD, entry.getKey());
                                        detail.put(MESSAGE, entry.getValue());
                                        return detail;
                                }).toList();

                Map<String, Object> response = new LinkedHashMap<>();
                response.put(MESSAGE, ex.getMessage());
                if(!errorList.isEmpty()){
                    response.put(ERRORS, errorList);
                }
                return response;
        }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedException(
            Exception ex) {

        log.error("Unexpected error", ex);

        Map<String, Object> response = new LinkedHashMap<>();
        response.put(MESSAGE, AN_UNEXPECTED_ERROR_OCCURRED);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
