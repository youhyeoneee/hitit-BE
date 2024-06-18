package com.pda.utils.exception.investment_tests;

public class NotFoundQuestionException extends RuntimeException {
    String message;

    public NotFoundQuestionException(int questionNo) {
        this.message = questionNo + "번 질문이 존재하지 않습니다.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
