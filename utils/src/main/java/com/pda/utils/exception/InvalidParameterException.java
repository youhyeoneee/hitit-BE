package com.pda.utils.exception;

public class InvalidParameterException extends RuntimeException {
    String message;

    public InvalidParameterException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
