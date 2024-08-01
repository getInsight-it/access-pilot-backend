package it.getinsight.module.request.enuns;

public enum RequestStatus {

    CREATED,
    IN_PROGRESS,
    WAINING_FOR_ROLES_CONFIRMATION,
    REJECTED,
    APPROVED,
    ROLES_CONFIRMED,
    ROLES_ASSIGNED,
    ROLES_NOT_FOUND,
    ROLES_NOT_ASSIGNED,
    COMPLETED,
    CANCELED, APPROVES_SENT;


    public static RequestStatus fromString(String status) {
        if (isValid(status)) {
            return RequestStatus.valueOf(status.toUpperCase());
        }
        return null;
    }


    public static boolean isValid(String status) {
        for (RequestStatus RequestStatus : RequestStatus.values()) {
            if (RequestStatus.name().equalsIgnoreCase(status)) {
                return true;
            }
        }
        return false;
    }
}
