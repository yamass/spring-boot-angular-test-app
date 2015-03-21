package de.yamass.validation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldValidationError {
    private String objectName;
    private String fieldName;
    private String errorCode;
    private String message;

    public String getObjectName() { return objectName; }

    public void setObjectName(String objectName) { this.objectName = objectName; }

    public String getFieldName() { return fieldName; }

    public void setFieldName(String fieldName) { this.fieldName = fieldName; }

    public String getErrorCode() { return errorCode; }

    public void setErrorCode(String errorCode) { this.errorCode = errorCode; }

    public String getMessage() { return message; }

    public void setMessage(String message) { this.message = message; }
}