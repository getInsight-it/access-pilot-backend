package it.getinsight.module.request.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class ProtocolUtil {

    public static String generateUniqueProtocolCode() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String timestamp = LocalDateTime.now().format(formatter);
        String uniqueID = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return "REQ-%s-%s" .formatted(timestamp, uniqueID);
    }

}
