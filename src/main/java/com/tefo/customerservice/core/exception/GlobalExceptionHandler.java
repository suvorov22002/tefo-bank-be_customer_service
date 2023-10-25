package com.tefo.customerservice.core.exception;

import com.tefo.library.commonutils.constants.ValidationMessages;
import com.tefo.library.commonutils.exception.EntityAlreadyExistsException;
import com.tefo.library.commonutils.exception.EntityNotFoundException;
import com.tefo.library.commonutils.exception.UniqueConstraintViolationException;
import com.tefo.library.commonutils.exception.utils.ExceptionDetails;
import com.tefo.library.commonutils.exception.utils.FieldErrorDetails;
import com.tefo.library.customdata.field.value.ValueValidationException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValueValidationException.class)
    public ResponseEntity<ExceptionDetails> handleCustomFieldValidationException(ValueValidationException ex) {
        return createValidationExceptionsResponse(List.of(
                FieldErrorDetails.builder()
                        .fieldName(ex.getFieldName())
                        .errorMessage(ex.getMessage())
                        .build())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDetails> handleGeneralExceptions(Exception ex) {
        return new ResponseEntity<>(getExceptionDetailsObject(ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDetails> handleIllegalArgumentException(IllegalArgumentException ex) {
        return new ResponseEntity<>(getExceptionDetailsObject(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleNotFoundExceptions(EntityNotFoundException ex) {
        return new ResponseEntity<>(getExceptionDetailsObject(ex.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionDetails> handleEntityAlreadyExistsExceptions(EntityAlreadyExistsException ex) {
        return new ResponseEntity<>(getExceptionDetailsObject(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<ExceptionDetails> handleMessageConversionExceptions(HttpMessageConversionException ex) {
        return new ResponseEntity<>(getExceptionDetailsObject(ex.getMessage()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionDetails> handleValidationExceptions(ConstraintViolationException ex) {
        List<FieldErrorDetails> fieldErrors = ex.getConstraintViolations()
                .stream()
                .map(field ->
                        FieldErrorDetails.builder()
                                .fieldName(field.getPropertyPath().toString())
                                .errorMessage(field.getMessageTemplate())
                                .build()
                )
                .toList();
        return createValidationExceptionsResponse(fieldErrors);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<FieldErrorDetails> fieldErrors = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(fieldError ->
                        FieldErrorDetails.builder()
                                .fieldName(fieldError.getField())
                                .errorMessage(fieldError.getDefaultMessage())
                                .build()
                )
                .toList();
        return createValidationExceptionsResponse(fieldErrors);
    }

    @ExceptionHandler(UniqueConstraintViolationException.class)
    public ResponseEntity<ExceptionDetails> handleUniqueValidationException(UniqueConstraintViolationException ex) {
        return createValidationExceptionsResponse(List.of(
                FieldErrorDetails.builder()
                        .fieldName(ex.getFieldName())
                        .errorMessage(ex.getMessage())
                        .build())
        );
    }

    @ExceptionHandler(IllegalCustomerActionException.class)
    public ResponseEntity<ExceptionDetails> handleIllegalCustomerActionException(IllegalCustomerActionException ex) {
        return new ResponseEntity<>(getExceptionDetailsObject(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AccessDeniedToCustomerActionException.class)
    public ResponseEntity<ExceptionDetails> handleAccessDenyToCustomerActionException(AccessDeniedToCustomerActionException ex) {
        return new ResponseEntity<>(getExceptionDetailsObject(ex.getMessage()), HttpStatus.FORBIDDEN);
    }

    private ExceptionDetails getExceptionDetailsObject(List<FieldErrorDetails> fieldErrors) {
        return ExceptionDetails.builder()
                .fieldErrorDetails(fieldErrors)
                .message(ValidationMessages.VALIDATION_VIOLATION_MESSAGE)
                .build();
    }

    private ExceptionDetails getExceptionDetailsObject(String message) {
        return ExceptionDetails.builder()
                .message(message)
                .build();
    }

    private ResponseEntity<ExceptionDetails> createValidationExceptionsResponse(List<FieldErrorDetails> fieldErrors) {
        return new ResponseEntity<>(getExceptionDetailsObject(fieldErrors
        ), HttpStatus.BAD_REQUEST);
    }
}
