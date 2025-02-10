package com.queerxdisasster.backendnotifications.service;

import com.queerxdisasster.handlers.ReadStatusEventHandler;
import com.queerxdisasster.handlers.HistoryRequestEventHandler;
import com.queerxdisasster.listeners.HistoryRequestEventListener;
import com.queerxdisasster.listeners.ReadStatusEventListener;
import com.queerxdisasster.shared.HistoryRequestEvent;
import com.queerxdisasster.shared.ReadStatusEvent;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteMessaging;
import org.apache.ignite.cluster.ClusterGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * B1ConsumerService:
 *  - Реализует 2 интерфейса: ReadStatusEventHandler и HistoryRequestEventHandler
 *  - Это позволяет "this" передавать в листенеры (не тянем B1ConsumerService в helper).
 */
@Service
public class B1ConsumerService implements ReadStatusEventHandler, HistoryRequestEventHandler {

    private static final String READ_STATUS_TOPIC = "readStatusTopic";
    private static final String HISTORY_REQUEST_TOPIC = "historyRequestTopic";

    @Autowired
    private Ignite ignite;

    @Autowired
    private transient NotificationService notificationService;

    @PostConstruct
    public void initListeners() {
        ClusterGroup b1Group = ignite.cluster().forAttribute("ROLE", "b1");
        ClusterGroup oldestB1 = b1Group.forOldest();

        IgniteMessaging messaging = ignite.message(oldestB1);

        // Регистрируем листенер для read-status
        messaging.remoteListen(READ_STATUS_TOPIC, new ReadStatusEventListener(this));

        // Регистрируем листенер для history-request
        messaging.remoteListen(HISTORY_REQUEST_TOPIC, new HistoryRequestEventListener(this));
    }

    // Реализация ReadStatusEventHandler:
    @Override
    public void handleReadStatus(ReadStatusEvent event) {
        System.out.println("B1ConsumerService handling readStatus: " + event);
        // Пример логики
        notificationService.markRead(event.getNotificationId(), event.getUserId());
    }

    // Реализация HistoryRequestEventHandler:
    @Override
    public void handleHistoryRequest(HistoryRequestEvent event) {
        System.out.println("B1ConsumerService handling historyRequest: " + event);
        // ... логика
    }
}
