package com.queerxdisasster.backendnotifications.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class NotificationDto {
    private Long id;
    private String userId;
    private String title;
    private String body;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime readAt;
    private LocalDateTime beginDate;
    private LocalDateTime endDate;
}
