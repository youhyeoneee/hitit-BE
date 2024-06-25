package com.pda.notification.controller;


import com.pda.notification.jpa.Notification;
import com.pda.utils.api_utils.ApiUtils;
import com.pda.notification.service.NotificationService;
import com.pda.utils.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

import static com.pda.utils.api_utils.ApiUtils.success;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtTokenProvider jwtTokenProvider;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribeNotifications(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("subscribe user : " + userId);
        return notificationService.createEmitter(userId);
    }

    @GetMapping()
    public ApiUtils.ApiResult getNotifications(@RequestHeader("Authorization") String bearerToken) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        List<Notification> notifications = notificationService.findAllNotificationByUserId(userId);
        return success(notifications);
    }

    @PatchMapping(value = "/{id}")
    public ApiUtils.ApiResult readNotification(@RequestHeader("Authorization") String bearerToken,
                                               @PathVariable int id) {
        int userId = jwtTokenProvider.bearerToken2UserId(bearerToken);
        log.info("subscribe user : " + userId);
        Notification notification = notificationService.updateNotification(userId);
        return success(notification);
    }
}
