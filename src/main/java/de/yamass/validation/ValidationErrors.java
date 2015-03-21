package de.yamass.validation;

import java.util.List;

public class ValidationErrors {

    private final List<CompositeValidationError> compositeValidationErrors;
    private final List<FieldValidationError> fieldErrors;

    public ValidationErrors(List<FieldValidationError> fieldErrors, List<CompositeValidationError> compositeValidationErrors) {
        this.compositeValidationErrors = compositeValidationErrors;
        this.fieldErrors = fieldErrors;
    }

    public List<FieldValidationError> getFieldErrors() {
        return fieldErrors;
    }

    public List<CompositeValidationError> getCompositeValidationErrors() {
        return compositeValidationErrors;
    }
}