package de.yamass.validation;

/**
 * @author Yann Massard
 */
public class CompositeValidationError {

    private String objectName;
    private String errorCode;
    private String message;

    public CompositeValidationError() {
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
