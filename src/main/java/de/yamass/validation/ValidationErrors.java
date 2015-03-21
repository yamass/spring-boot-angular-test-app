package de.yamass.validation;

import java.util.List;

public class ValidationErrors {
    private List<FieldValidationError> fieldErrors;

    public ValidationErrors() { }

    public List<FieldValidationError> getFieldErrors() { return fieldErrors; }

    public void setFieldErrors(List<FieldValidationError> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}