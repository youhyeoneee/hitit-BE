package com.pda.utils.exception.investment_tests;

public class QuestionNotFoundException extends RuntimeException {
    String message;

    public QuestionNotFoundException(int questionNo) {
        this.message = questionNo + "번 질문이 존재하지 않습니다.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
