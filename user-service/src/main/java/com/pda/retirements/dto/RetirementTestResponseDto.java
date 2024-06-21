package com.pda.retirements.dto;

import com.pda.retirements.jpa.RetirementType;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
public class RetirementTestResponseDto {
    Type type;

    public RetirementTestResponseDto(RetirementType retirementType) {
        this.type = new Type(retirementType.getName(), retirementType.getDescription());
    }

    @Getter
    @AllArgsConstructor
    class Type {
        private final String name;
        private final String description;
    }

}

