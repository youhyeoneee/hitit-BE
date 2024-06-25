package com.pda.portfolio_service.controller;

import com.pda.portfolio_service.service.PortfolioService;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.utils.rabbitmq.dto.NotificationDto;
import com.pda.utils.rabbitmq.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@AllArgsConstructor
@Slf4j
public class PortfolioMessageController {

    private final PortfolioService portfolioService;

    @PostMapping(value = "/send/message")
    public ApiUtils.ApiResult sendNotificationMessageTest(@RequestBody NotificationDto notificationDto) {
        portfolioService.sendNotificationMessage(notificationDto);
        return success(notificationDto);
    }
}
