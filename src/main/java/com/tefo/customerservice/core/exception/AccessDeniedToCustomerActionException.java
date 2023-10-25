package com.tefo.customerservice.core.exception;

public class AccessDeniedToCustomerActionException extends RuntimeException {
    public AccessDeniedToCustomerActionException(String message) {
        super(message);
    }
}
