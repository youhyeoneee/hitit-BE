package com.pda.utils.exception;

public class DuplicatedEmailException extends RuntimeException {
    String message;

    public DuplicatedEmailException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

