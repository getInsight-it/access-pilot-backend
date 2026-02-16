package it.getinsight.module.client.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class ClientImportCountersDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1621103321732014452L;

    private long created;
    private long updated;
    private long deleted;
}
