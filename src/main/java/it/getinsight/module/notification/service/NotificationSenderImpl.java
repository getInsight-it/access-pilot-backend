package it.getinsight.module.notification.service;

import it.getinsight.module.notification.dto.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationSenderImpl implements NotificationSender {

    private final List<NotificationSender> notificationServices;

    @Override
    public Notification send(Notification notification) {
        for (NotificationSender notificationService : notificationServices) {
            if (notificationService.isSupported()) {
                notificationService.send(notification);
            }
        }
        return null;
    }

    @Override
    public boolean isSupported() {
        return false;
    }
}
