package it.getinsight.module.invitation.service;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class InvitationProtocolCodeService {

    public String generateProtocolCode() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String uniqueId = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "INV-%s-%s".formatted(timestamp, uniqueId);
    }
}

