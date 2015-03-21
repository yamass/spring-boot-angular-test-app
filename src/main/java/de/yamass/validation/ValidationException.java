package de.yamass.validation;

import org.springframework.validation.Errors;

@SuppressWarnings("serial")
public class ValidationException extends RuntimeException {
    private Errors errors;

    public ValidationException(Errors errors) {
        super();
        this.errors = errors;
    }

    public Errors getErrors() { return errors; }
}