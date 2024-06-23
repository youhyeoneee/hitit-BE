package com.pda.asset_service.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PensionId implements Serializable {
    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "pension_name")
    private String pensionName;
}
