package it.getinsight.module.storage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável exclusivamente por políticas de buckets.
 * Aplica SRP de forma agressiva - apenas políticas de buckets.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class BucketPolicyService {

    private static final String PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET = "private-getinsight-accesspilot-docs";
    private static final String PUBLIC_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET = "public-getinsight-accesspilot-docs";

    /**
     * Determina o bucket apropriado baseado no tipo de arquivo e contexto.
     */
    public String determineBucket(String fileType, boolean isPublic, String context) {
        if (isPublic) {
            log.debug("Using public bucket for file type: {}", fileType);
            return PUBLIC_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
        }
        
        log.debug("Using private bucket for file type: {} in context: {}", fileType, context);
        return PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
    }

    /**
     * Obtém o bucket padrão para documentos privados.
     */
    public String getDefaultPrivateBucket() {
        return PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
    }

    /**
     * Obtém o bucket padrão para documentos públicos.
     */
    public String getDefaultPublicBucket() {
        return PUBLIC_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET;
    }
}
