package it.getinsight.module.request.enuns;

import it.getinsight.core.message.CoreMessageSource;
import lombok.Getter;

@Getter
public enum RequestStatus {

    CREATED("request.status.created"),
    PENDING("request.status.pending"),
    REJECTED("request.status.rejected"),
    APPROVED("request.status.approved"),
    CANCELED("request.status.canceled");


    private String key;

    RequestStatus(String key) {
        this.key = key;
    }

    public String getDescription() {
        return CoreMessageSource.get().message(this.key) ;
    }

}
