package de.yamass.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ValidationExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({ ValidationException.class })
    protected ResponseEntity<Object> handleInvalidRequest(ValidationException e, WebRequest request) {

        List<FieldValidationError> fieldValidationErrors = new ArrayList<>();
        for (FieldError fieldError : e.getErrors().getFieldErrors()) {
            FieldValidationError fieldValidationError = new FieldValidationError();
            fieldValidationError.setObjectName(fieldError.getObjectName());
            fieldValidationError.setFieldName(fieldError.getField());
            fieldValidationError.setErrorCode(fieldError.getCode());
            fieldValidationError.setMessage(fieldError.getDefaultMessage());
            fieldValidationErrors.add(fieldValidationError);
        }

        List<CompositeValidationError> compositeValidationErrors = new ArrayList<>();
        for (ObjectError globalError : e.getErrors().getGlobalErrors()) {
            CompositeValidationError compositeValidationError = new CompositeValidationError();
            compositeValidationError.setObjectName(globalError.getObjectName());
            compositeValidationError.setErrorCode(globalError.getCode());
            compositeValidationError.setMessage(globalError.getDefaultMessage());
            compositeValidationErrors.add(compositeValidationError);
        }


        ValidationErrors error = new ValidationErrors(fieldValidationErrors, compositeValidationErrors);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        return handleExceptionInternal(e, error, headers, HttpStatus.UNPROCESSABLE_ENTITY, request);
    }

}