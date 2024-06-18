package com.pda.investment_test.jpa.answer;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "answers")
public class Answer {
    @Id
    private int id;
    private int questionNo;
    private int no;
    private String content;
    private int score;
}
