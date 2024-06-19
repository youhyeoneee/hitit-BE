package com.pda.investment_test.jpa.user_answer;

import com.pda.user_service.jpa.User;
import com.pda.utils.api_utils.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "user_answers_2")
public class UserAnswer {

    @Id
    private int userId;

    @Convert(converter = StringListConverter.class)
    private List<String> answers;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UserAnswer(int userId, List<String> answers) {
        this.userId = userId;
        this.answers = answers;
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        this.createdAt = nowLocalDateTime;
        this.updatedAt = nowLocalDateTime;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}