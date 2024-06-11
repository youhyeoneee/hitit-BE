package com.pda.utils.exception.login;

public class NotFoundUserException extends RuntimeException {
    String message;

    public NotFoundUserException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
