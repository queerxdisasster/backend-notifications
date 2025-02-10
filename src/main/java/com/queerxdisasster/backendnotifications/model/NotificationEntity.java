package com.queerxdisasster.backendnotifications.model;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
    private String title;
    private String body;
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime readAt;

    // Новые поля для запланированного уведомления
    private LocalDateTime beginDate;
    private LocalDateTime endDate;

    // Конструктор для создания нового уведомления с датами
    public NotificationEntity(String userId, String title, String body, LocalDateTime beginDate, LocalDateTime endDate) {
        this.userId = userId;
        this.title = title;
        this.body = body;
        this.status = "SCHEDULED";
        this.createdAt = LocalDateTime.now();
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}
