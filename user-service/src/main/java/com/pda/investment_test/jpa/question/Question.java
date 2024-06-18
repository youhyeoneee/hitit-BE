package com.pda.investment_test.jpa.question;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "questions")
public class Question {
    @Id
    private int no;
    private String content;
    private String category;
}
