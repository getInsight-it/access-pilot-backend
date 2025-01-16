package it.getinsight.module.notification.service;

import it.getinsight.module.notification.dto.Notification;

import java.util.ArrayList;
import java.util.List;


public interface NotificationSender {

    default List<Notification> send(List<Notification> notifications) {
        ArrayList<Notification> notificationsSent = new ArrayList<>();
        for (Notification notification : notifications) {
            notificationsSent.add(send(notification));
        }
        return notificationsSent;
    }
    Notification send(Notification notification);
    boolean isSupported();

}
