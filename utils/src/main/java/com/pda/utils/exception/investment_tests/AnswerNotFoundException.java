package com.pda.utils.exception.investment_tests;

public class AnswerNotFoundException extends RuntimeException {
    String message;

    public AnswerNotFoundException(int questionNo, int answerNo) {
        this.message = "질문 번호 " + questionNo + "번에 대한 답변 번호 " + answerNo + "을(를) 찾을 수 없습니다.";
    }

    @Override
    public String getMessage() {
        return message;
    }
}
