package it.getinsight.module.email.dto;


import lombok.Builder;

import java.io.Serializable;
import java.util.Map;

@Builder
public record EmailDTO(
     Long id,
     String from,
     String to,
     String subject,
     String templateName,
     Long userId,
     String status,
     String content,
     Boolean isHtml,
     Map<String, Object> variables
) implements Serializable {}










