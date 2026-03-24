package it.getinsight.core.queue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueMessageDTO<T> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String messageId;
    private Boolean dlq;
    private String userId;
    private T payload;
}
