package it.getinsight.module.request.queue.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEventPayloadDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String eventType;
    @Builder.Default
    private String version = "v1";

    private Long requestId;
    private Long requesterId;
    private Long roleId;
    private String status;
    private String protocolCode;
}
