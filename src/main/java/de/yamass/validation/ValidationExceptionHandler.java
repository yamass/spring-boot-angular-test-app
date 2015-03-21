package de.yamass.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ValidationException.class })
    protected ResponseEntity<Object> handleInvalidRequest(RuntimeException e, WebRequest request) {
        ValidationException ire = (ValidationException) e;
        List<FieldValidationError> fieldValidationErrors = new ArrayList<>();

        List<FieldError> fieldErrors = ire.getErrors().getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            FieldValidationError fieldValidationError = new FieldValidationError();
            fieldValidationError.setObjectName(fieldError.getObjectName());
            fieldValidationError.setFieldName(fieldError.getField());
            fieldValidationError.setErrorCode(fieldError.getCode());
            fieldValidationError.setMessage(fieldError.getDefaultMessage());
            fieldValidationErrors.add(fieldValidationError);
        }

        ValidationErrors error = new ValidationErrors();
        error.setFieldErrors(fieldValidationErrors);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

}