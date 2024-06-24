package com.pda.notification.controller;


import com.pda.utils.rabbitmq.dto.NotificationDto;
import com.pda.notification.service.NotificationService;
import com.pda.utils.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/subscribe/{user_id}", produces = "text/event-stream")
    public SseEmitter subscribeNotifications(@PathVariable("user_id") int userId) {
//        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("subscribe user : " + userId);
        return notificationService.createEmitter(userId);
    }
}
