package it.getinsight.module.request.service;

import it.getinsight.module.request.util.ProtocolUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProtocolGeneratorService {


    public String generateUniqueProtocolCode() {
        String protocolCode = ProtocolUtil.generateUniqueProtocolCode();
        log.debug("Generated protocol code: {}", protocolCode);
        return protocolCode;
    }
}
