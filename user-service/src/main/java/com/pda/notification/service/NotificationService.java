package com.pda.notification.service;


import com.pda.utils.exception.login.NotFoundUserException;
import com.pda.utils.rabbitmq.dto.NotificationDto;
import com.pda.notification.jpa.Notification;
import com.pda.notification.jpa.NotificationRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    private final Map<Integer, SseEmitter> userEmitters = new ConcurrentHashMap<>();
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final NotificationRepository notificationRepository;

    public SseEmitter createEmitter(int userId) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        userEmitters.put(userId, emitter);
        log.info("create Emmitter " + userId);
        emitter.onCompletion(() -> userEmitters.remove(userId));
        emitter.onTimeout(() -> userEmitters.remove(userId));
        emitter.onError((e) -> userEmitters.remove(userId));

        return emitter;
    }

    @Transactional
    public void sendNotification(NotificationDto notificationDto) {
        Notification notification = new Notification(
                notificationDto.getUserId(),
                notificationDto.getRebalancingId(),
                notificationDto.getChecked(),
                notificationDto.getSummary()
        );

        // 알림 DB에 저장
        notificationRepository.save(notification);
        // TODO: 잘 저장되었는지 확인 로직

        // 클라이언트에게 전송
        sendRealTimeNotification(notification);
    }

    private void sendRealTimeNotification(Notification notification) {
        SseEmitter emitter = userEmitters.get(notification.getUserId());

        if (emitter != null) {
            executorService.execute(() -> {
                try {
                    emitter.send(SseEmitter.event().name("notification").data(notification));
                } catch (Exception e) {
                    log.error(e.getMessage());
                    userEmitters.remove(notification.getUserId());
                }
            });
        } else {
            log.info("emitter " + notification.getUserId()  + " is null");
        }
    }

    public List<Notification> findAllNotificationByUserId(int userId) {
        return notificationRepository.findAllByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public Notification setCheckdNotification(int notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NoSuchElementException(notificationId + "번 알림이 존재하지 않습니다."));

        if (notification.getChecked() != null) {
            notification.setChecked(true);
        }

        return notification;
    }
}