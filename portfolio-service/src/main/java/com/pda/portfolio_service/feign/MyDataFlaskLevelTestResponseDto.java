package com.pda.portfolio_service.feign;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MyDataFlaskLevelTestResponseDto {
    private List<FundGroup> response;

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class FundGroup {
        private List<Fund> funds;
        private String fundClass;
    }

    // Getters and setters
    public List<FundGroup> getResponse() {
        return response;
    }

    public void setResponse(List<FundGroup> response) {
        this.response = response;
    }

    @ToString
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class Fund {
        private double arimaPercent;
        private double arimaPrice;
        private String arimaUpdate;
        private double bond;
        private double bondForeign;
        private String companyName;
        private double drvNav;
        private double etc;
        private String fundCode;
        private String fundName;
        private String fundType;
        private String fundTypeDetail;
        private String hashtag;
        private double investment;
        @JsonProperty("return_1m")
        private double return1m;

        @JsonProperty("return_1y")
        private double return1y;

        @JsonProperty("return_3m")
        private double return3m;

        @JsonProperty("return_3y")
        private double return3y;

        @JsonProperty("return_5y")
        private double return5y;

        @JsonProperty("return_6m")
        private double return6m;

        @JsonProperty("return_idx")
        private double returnIdx;

        @JsonProperty("return_ytd")
        private double returnYtd;
        private int riskGrade;
        private String riskGradeTxt;
        private double setAmount;
        private String setDate;
        private double stdPrice;
        private double stock;
        private double stockForeign;
        private Float stockRatio;
        private Float bondRatio;
    }
}