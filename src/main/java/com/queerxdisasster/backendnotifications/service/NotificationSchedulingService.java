package com.queerxdisasster.backendnotifications.service;

import com.queerxdisasster.backendnotifications.model.NotificationEntity;
import com.queerxdisasster.shared.NotificationEvent;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.cron.Cron;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationSchedulingService {
    private final NotificationService notificationService;
    private final B1ProducerService producer;
    private final JobScheduler jobScheduler;

    @Autowired
    public NotificationSchedulingService(
            NotificationService notificationService,
            B1ProducerService producer,
            JobScheduler jobScheduler) {
        this.notificationService = notificationService;
        this.producer = producer;
        this.jobScheduler = jobScheduler;
    }

    public void setupDailyScheduler() {
        jobScheduler.scheduleRecurrently(
                Cron.daily(),
                () -> scheduleDailyNotifications()
        );
    }

    public void scheduleDailyNotifications() {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
        LocalDateTime tomorrow = today.plusDays(1);

        List<NotificationEntity> notifications = notificationService.findNotificationsForDateRange(today, tomorrow);

        for (NotificationEntity notification : notifications) {
            scheduleNotification(notification);
        }
    }

    public void handleNewNotification(NotificationEntity notification) {
        LocalDateTime now = LocalDateTime.now();

        if (notification.getBeginDate() == null || notification.getBeginDate().isBefore(now.plusMinutes(1))) {
            jobScheduler.enqueue(() -> executeNotificationSending(notification));
        } else if (notification.getBeginDate().toLocalDate().equals(now.toLocalDate())) {
            scheduleNotification(notification);
        }
    }

    protected void scheduleNotification(NotificationEntity notification) {
        jobScheduler.schedule(
                notification.getBeginDate(),
                () -> executeNotificationSending(notification)
        );
    }

    // This method needs to be public for JobRunr to access it
    public void executeNotificationSending(NotificationEntity notification) {
        if (notification.getEndDate() == null || LocalDateTime.now().isBefore(notification.getEndDate())) {
            NotificationEvent event = new NotificationEvent(
                    notification.getId(),
                    notification.getUserId(),
                    notification.getTitle(),
                    notification.getBody(),
                    notification.getStatus()
            );
            producer.sendNotificationEvent(event);

            notification.setStatus("SENT");
            notificationService.save(notification);
        }
    }
}