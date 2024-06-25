package com.pda.mydata_service.dto;

import com.pda.mydata_service.jpa.SecurityAccount;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecurityStockDto {
    private Integer id;
    private String accountNo;

    private String stockCode;
}
