package com.pda.utils.exception.investment_tests;

public class UserAnswerNotFoundException extends RuntimeException {
    public UserAnswerNotFoundException(int userId) {
        super(userId + "번 유저의 저장된 테스트 결과가 없습니다.");
    }

    public UserAnswerNotFoundException(String message) {
        super(message);
    }
}
