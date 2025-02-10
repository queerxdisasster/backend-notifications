package com.queerxdisasster.backendnotifications.controller;


import com.queerxdisasster.backendnotifications.model.NotificationEntity;
import com.queerxdisasster.backendnotifications.service.B1ProducerService;
import com.queerxdisasster.backendnotifications.service.NotificationSchedulingService;
import com.queerxdisasster.backendnotifications.service.NotificationService;
import com.queerxdisasster.shared.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final NotificationSchedulingService schedulingService;
    private final B1ProducerService producer;

    @PostMapping
    public String createNotification(
            @RequestParam String userId,
            @RequestParam String title,
            @RequestParam(required = false) String body,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime beginDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        NotificationEntity entity = notificationService.createNotification(userId, title, body, beginDate, endDate);
        schedulingService.handleNewNotification(entity);

        return "Notification created with ID: " + entity.getId();
    }

    @PutMapping("/{notifId}/read")
    public String markRead(@PathVariable Long notifId, @RequestParam String userId) {
        notificationService.markRead(notifId, userId);
        return "Notification marked as read";
    }
}
