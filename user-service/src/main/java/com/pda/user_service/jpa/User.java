package com.pda.user_service.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class User {
    @Id
    private int id;
    private String email;
    private String name;
    private String password;
    private String phone;
    private String birthdate;
    private String gender;
    private String type;
    private String mydata;

    public User(String name, String email, String password, String phone, String birthdate, String gender) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.birthdate = birthdate;
        this.gender = gender;
    }
}
