package com.pda.portfolio_service.controller;

import com.pda.utils.rabbitmq.dto.AlarmDto;
import com.pda.utils.rabbitmq.service.MessageService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
public class PortfolioMessageController {

    MessageService messageService;

    @PostMapping(value = "/send/message")
    public ResponseEntity<?> sendMessage(@RequestParam String serviceName, @RequestBody AlarmDto alarmDto) {
        if (Objects.equals(serviceName, "user")) {
            messageService.sendMessageToUserService(alarmDto);
        }

        return ResponseEntity.ok("Message sent to RabbitMQ!");
    }
}
