package com.pda.retirements.jpa;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RetirementType {
    LEVEL_4(4, "노후 준비가 충분한 공적 자산형", "가장 이상적인 유형으로 노후 필요 생활비를 충당할 수 있는 자산 규모를 갖고 있고 공적 자산의 비중이 높은 유형이에요."),
    LEVEL_3(3, "노후 준비가 부족한 공적 자산형", "노후 필요 생활비를 충당하기 부족한 자산 규모를 갖고 있고 공적 자산의 비중이 높은 유형으로 노후생활비에 비해서 보유한 은퇴자산의 규모가 적고, 개인연금, 금융자산 등의 선택자산에 관심이 낮아요."),
    LEVEL_2(2, "노후 준비기 충분한 사적 자산형", "노후 필요 생활비를 충당할 수 있는 자산 규모를 갖고 있지만 사적 자산 비중이 높은 유형이에요."),
    LEVEL_1(1, "노후 준비가 부족한 사적 자산형", "노후 필요 생활비를 충당하기 부족한 자산 규모를 갖고 있고 사적 자산 비중이 높은 유형이에요.");

    private final int level;
    private final String name;
    private final String description;
}