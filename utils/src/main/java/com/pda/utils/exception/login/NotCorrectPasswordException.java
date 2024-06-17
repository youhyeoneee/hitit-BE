package com.pda.utils.exception.login;

public class NotCorrectPasswordException extends RuntimeException {
    String message;

    public NotCorrectPasswordException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
