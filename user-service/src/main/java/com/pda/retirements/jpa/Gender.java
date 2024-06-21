package com.pda.retirements.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    F("female"),
    M("male");

    private String name;
}
