package it.getinsight.module.email.dto;


import lombok.Builder;

import java.util.Map;

@Builder
public record EmailDTO(
     String to,
     String subject,
     String templateName,
     String text,
     Boolean isHtml,
     Map<String, Object> variables
){}










