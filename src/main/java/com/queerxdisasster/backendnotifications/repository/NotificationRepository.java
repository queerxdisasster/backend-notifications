package com.queerxdisasster.backendnotifications.repository;

import com.queerxdisasster.backendnotifications.model.NotificationEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    @Query("select n from NotificationEntity n where n.userId = :userId order by n.createdAt desc")
    List<NotificationEntity> findNotificationsByUserId(String userId);

    @Query("select n from NotificationEntity n where n.userId = :userId order by n.createdAt desc")
    List<NotificationEntity> findNotificationsByUserIdLimited(String userId, Pageable pageable);

    @Query("select n from NotificationEntity n where n.beginDate >= :start and n.beginDate < :end")
    List<NotificationEntity> findNotificationsForDateRange(LocalDateTime start, LocalDateTime end);
}