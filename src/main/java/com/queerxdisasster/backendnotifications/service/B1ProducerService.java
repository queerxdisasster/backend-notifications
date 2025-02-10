package com.queerxdisasster.backendnotifications.service;

import com.queerxdisasster.shared.HistoryResponseEvent;
import com.queerxdisasster.shared.NotificationEvent;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteMessaging;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class B1ProducerService {

    private static final String NOTIFICATION_TOPIC = "notifications";
    private static final String HISTORY_RESPONSE_TOPIC = "historyResponseTopic";

    private final IgniteMessaging messaging;

    @Autowired
    public B1ProducerService(Ignite ignite) {
        this.messaging = ignite.message();
    }

    public void sendNotificationEvent(NotificationEvent event) {
        System.out.println("Sending notification event: " + event);
        messaging.send(NOTIFICATION_TOPIC, event);
    }

    public void sendHistoryResponse(HistoryResponseEvent event) {
        messaging.send(HISTORY_RESPONSE_TOPIC, event);
    }
}
