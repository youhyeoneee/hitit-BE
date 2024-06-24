package com.pda.user_service.service;

import com.pda.user_service.jpa.User;
import com.pda.user_service.jpa.UserRepository;
import com.pda.utils.api_utils.CustomStringUtils;
import com.pda.utils.exception.login.NotFoundUserException;
import com.pda.utils.rabbitmq.dto.AlarmDto;
import com.pda.utils.rabbitmq.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@PropertySource("application-mq.properties")
public class UserMessageService {

    private final RabbitTemplate rabbitTemplate;
    private final MessageService messageService;
    private final UserRepository userRepository;

    @RabbitListener(queues = "${spring.rabbitmq.queue.name}")
    public void receiveMessage(AlarmDto alarmDto) {
        log.info("User Queue 내의 결과 값을 반환 받습니다 ");
        log.info("user id : " + alarmDto.getUserId());
        log.info("relalncing id : " + alarmDto.getRebalancingId());
        log.info("check : " + alarmDto.getCheck());
        log.info("check : " + alarmDto.getCheck());
        log.info("summary : " + alarmDto.getSummary());

        // TODO: 알림 Repository에 올리기
    }


}