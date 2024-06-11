package com.pda.user_service.jpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

@Entity
@Table(name = "test_user")
@Getter
public class RDSTest {

    @Id
    private int id;
    private String name;
}
