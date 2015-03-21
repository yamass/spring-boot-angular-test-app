package de.yamass.validation;

import org.springframework.validation.Errors;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
    private Errors errors;

    public ValidationException(String message, Errors errors) {
        super(message);
        this.errors = errors;
    }

    public Errors getErrors() { return errors; }
}