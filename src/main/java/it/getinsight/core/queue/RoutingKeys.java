package it.getinsight.core.queue;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RoutingKeys {

    
    public static final String REQUEST_CREATED = "request.created";
    public static final String REQUEST_STATUS_CHANGED = "request.status.changed";

    
    public static final String NOTIFICATION_REQUEST_CREATED_APPROVER = "request.created.approver";
    public static final String NOTIFICATION_REQUEST_CREATED_CONFIRMATION = "request.created.confirmation";
    public static final String NOTIFICATION_REQUEST_STATUS_CHANGED = "request.status.changed";

    
    public static final String NOTIFY_EMAIL = "notify.email";
    public static final String NOTIFY_WEB = "notify.web";
}
