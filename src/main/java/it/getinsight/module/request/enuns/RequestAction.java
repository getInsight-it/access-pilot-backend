package it.getinsight.module.request.enuns;

import lombok.Getter;


@Getter
public enum RequestAction {

    REJECT("Rejeitar", RequestStatus.REJECTED),
    APPROVE("Aprovar", RequestStatus.APPROVED),
    CANCEL("Cancelar", RequestStatus.CANCELED),
    REVOKE("Revogar", RequestStatus.REVOKED);

    private final String description;
    private final RequestStatus targetStatus;

    RequestAction(String description, RequestStatus targetStatus) {
        this.description = description;
        this.targetStatus = targetStatus;
    }

}
