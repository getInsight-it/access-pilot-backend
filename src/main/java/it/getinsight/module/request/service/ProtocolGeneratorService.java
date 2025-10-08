package it.getinsight.module.request.service;

import it.getinsight.module.request.util.ProtocolUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por gerar códigos de protocolo únicos.
 * Aplica o princípio SRP (Single Responsibility Principle) centralizando
 * a lógica de geração de protocolos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProtocolGeneratorService {

    /**
     * Gera um código de protocolo único para um request.
     */
    public String generateUniqueProtocolCode() {
        String protocolCode = ProtocolUtil.generateUniqueProtocolCode();
        log.debug("Generated protocol code: {}", protocolCode);
        return protocolCode;
    }
}
