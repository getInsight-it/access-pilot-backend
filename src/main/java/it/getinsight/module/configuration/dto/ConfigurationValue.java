package it.getinsight.module.configuration.dto;

import java.io.Serializable;

public record ConfigurationValue(
        Integer minQuantity,
    Integer maxQuantity
) implements Serializable {}
