package com.pda.user_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pda.notification.service.NotificationService;
import com.pda.user_service.jpa.UserRepository;
import com.pda.utils.rabbitmq.dto.NotificationDto;
import com.pda.utils.rabbitmq.service.MessageService;
import com.pda.utils.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("application-mq.properties")
public class UserMessageService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageService messageService;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final JwtTokenProvider jwtTokenProvider;

    @RabbitListener(queues = "${spring.rabbitmq.queue.name}")
    public void receiveMessage(NotificationDto notificationDto) throws IOException {
        log.info("User Queue 내의 결과 값을 반환 받습니다 ");
        log.info("user id : " + notificationDto.getUserId());
        log.info("relalncing id : " + notificationDto.getRebalancingId());
        log.info("check : " + notificationDto.getChecked());
        log.info("summary : " + notificationDto.getSummary());

        // TODO: 알림 Repository에 올리기
        notificationService.sendNotification(notificationDto);
    }


}