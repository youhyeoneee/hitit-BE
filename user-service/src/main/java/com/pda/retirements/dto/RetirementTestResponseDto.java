package com.pda.retirements.dto;

import com.pda.retirements.jpa.RetirementType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Getter
public class RetirementTestResponseDto {
    ArrayList<String> type;

    public RetirementTestResponseDto(RetirementType retirementType) {
        this.type = new ArrayList<>(Arrays.asList(retirementType.getName(), retirementType.getDescription()));
    }
}

