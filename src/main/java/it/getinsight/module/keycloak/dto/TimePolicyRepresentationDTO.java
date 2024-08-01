package it.getinsight.module.keycloak.dto;

public record TimePolicyRepresentationDTO(String type, Integer notBefore, Integer notOnOrAfter, Integer dayMonth,
                                          Integer dayMonthEnd, Integer month, Integer monthEnd, Integer year,
                                          Integer yearEnd, Integer hour, Integer hourEnd, Integer minute,
                                          Integer minuteEnd) {
}
