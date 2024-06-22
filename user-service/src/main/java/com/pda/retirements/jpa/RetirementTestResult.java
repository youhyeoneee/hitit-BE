package com.pda.retirements.jpa;

import com.pda.retirements.dto.RetirementTestResponseDto;
import com.pda.utils.api_utils.StringListConverter;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "retirement_test_results")
public class RetirementTestResult {
    @Id
    private int userId;

    @Convert(converter = StringListConverter.class)
    private List<String> type;
    @Convert(converter = StringListConverter.class)
    private List<String> monthlyLivingExpenses;
    private String expectedNationalPension;
    private String totalRealEstateValue;
    private String careerEffortScore;
    private int assetLife;
    private int lifeExpectancy;
    private int optimalMonthlyLivingExpenses;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public RetirementTestResult(int userId, RetirementTestResponseDto testResponseDto) {
        this.userId = userId;
        this.type = testResponseDto.getType();
        setResults(testResponseDto);
        LocalDateTime nowLocalDateTime = LocalDateTime.now();
        this.createdAt = nowLocalDateTime;
        this.updatedAt = nowLocalDateTime;
    }

    public void setResults(RetirementTestResponseDto testResponseDto) {
        this.monthlyLivingExpenses = testResponseDto.getMonthlyLivingExpenses();
        this.expectedNationalPension = testResponseDto.getExpectedNationalPension();
        this.totalRealEstateValue = testResponseDto.getTotalRealEstateValue();
        this.careerEffortScore = testResponseDto.getCareerEffortScore();
        this.assetLife = testResponseDto.getAssetLife();
        this.lifeExpectancy = testResponseDto.getLifeExpectancy();
        this.optimalMonthlyLivingExpenses = testResponseDto.getOptimalMonthlyLivingExpenses();
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

}




