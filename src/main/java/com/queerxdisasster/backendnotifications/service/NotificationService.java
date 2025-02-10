package com.queerxdisasster.backendnotifications.service;

import com.queerxdisasster.backendnotifications.model.NotificationDto;
import com.queerxdisasster.backendnotifications.model.NotificationEntity;
import com.queerxdisasster.backendnotifications.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository repo;

    public NotificationEntity createNotification(String userId, String title, String body, LocalDateTime beginDate, LocalDateTime endDate) {
        validateDates(beginDate, endDate);
        NotificationEntity entity = new NotificationEntity(userId, title, body, beginDate, endDate);
        return repo.save(entity);
    }

    public NotificationEntity save(NotificationEntity entity) {
        return repo.save(entity);
    }

    public void markRead(Long notificationId, String userId) {
        NotificationEntity entity = repo.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Not found"));
        if (!entity.getUserId().equals(userId)) {
            throw new RuntimeException("User mismatch");
        }
        entity.setStatus("READ");
        entity.setReadAt(LocalDateTime.now());
        repo.save(entity);
    }

    public List<NotificationDto> getNotifications(String userId) {
        List<NotificationEntity> list = repo.findNotificationsByUserId(userId);
        return convertToDtoList(list);
    }

    public List<NotificationDto> getLastN(String userId, int limit) {
        List<NotificationEntity> list = repo.findNotificationsByUserIdLimited(
                userId, PageRequest.of(0, limit)
        );
        return convertToDtoList(list);
    }

    public List<NotificationEntity> findNotificationsForDateRange(LocalDateTime start, LocalDateTime end) {
        return repo.findNotificationsForDateRange(start, end);
    }

    private List<NotificationDto> convertToDtoList(List<NotificationEntity> entities) {
        return entities.stream()
                .map(e -> new NotificationDto(
                        e.getId(),
                        e.getUserId(),
                        e.getTitle(),
                        e.getBody(),
                        e.getStatus(),
                        e.getCreatedAt(),
                        e.getReadAt(),
                        e.getBeginDate(),
                        e.getEndDate()))
                .collect(Collectors.toList());
    }

    private void validateDates(LocalDateTime beginDate, LocalDateTime endDate) {
        if (beginDate != null && endDate != null && beginDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Begin date must be before end date");
        }
    }
}
