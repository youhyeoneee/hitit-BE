package com.pda.utils.rabbitmq.service;

import com.pda.utils.rabbitmq.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.exchanges.userExchange}")
    private String userExchangeName;

    @Value("${spring.rabbitmq.routing.userRoutingKey}")
    private String userRoutingKey;

    public void sendNotificationMsgToUserService(NotificationDto notificationDto) {
        rabbitTemplate.convertAndSend(userExchangeName, userRoutingKey, notificationDto);
    }
}