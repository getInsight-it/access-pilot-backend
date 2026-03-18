package it.getinsight.core.queue;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * DTO padrão para mensagens de fila.
 * Compatível com a estrutura esperada da biblioteca CALI.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QueueMessageDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * Identificador do objeto a ser processado (ex: ID da entidade).
     */
    private String objectIdentifier;

    /**
     * Nome do bean Spring que deve processar esta mensagem.
     */
    private String listenerBeanName;

    /**
     * Se true, envia para Dead Letter Queue em caso de erro.
     */
    private Boolean dlq;

    /**
     * ID do usuário para reconstrução do contexto de auditoria.
     * Nota: campo extra não presente na CALI original.
     */
    private String userId;
}
