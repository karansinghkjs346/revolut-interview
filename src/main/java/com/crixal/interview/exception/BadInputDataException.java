package com.crixal.interview.exception;

public class BadInputDataException extends RuntimeException {
    public BadInputDataException(String message) {
        super(message);
    }

    public BadInputDataException(Throwable cause) {
        super(cause);
    }
}
