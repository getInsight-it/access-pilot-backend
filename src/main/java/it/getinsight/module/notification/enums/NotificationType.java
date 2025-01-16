package it.getinsight.module.notification.enums;

public enum NotificationType {

    EMAIL("email"),
    WEB("web");

    private final String value;

    NotificationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
