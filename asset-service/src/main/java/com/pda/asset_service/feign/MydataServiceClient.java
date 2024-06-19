package com.pda.asset_service.feign;

import com.pda.asset_service.dto.BankAccountDto;
import com.pda.asset_service.dto.BankAccountResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@FeignClient(name = "mydata-service", url = "http://localhost:8082")
public interface MydataServiceClient {

    @GetMapping("/api/mydata/bank-account/{userId}")
    List<BankAccountDto> getAllBankAccountData(@PathVariable("userId") int userId);
    @GetMapping("/api/mydata/bank-account/{userId}/{bankName}")
    Optional<List<BankAccountResponseDto>> getBankAccountsByUserIdAndBankName(@PathVariable("userId") int userId, @PathVariable("bankName") String bankName);

}
