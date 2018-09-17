package com.crixal.interview.exception;

public class RetryOperationException extends RuntimeException {
    public RetryOperationException(String message) {
        super(message);
    }
}
