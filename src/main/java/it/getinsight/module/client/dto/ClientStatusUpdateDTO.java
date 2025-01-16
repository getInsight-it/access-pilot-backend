package it.getinsight.module.client.dto;

import lombok.Builder;

import java.io.Serializable;

@Builder
public record ClientStatusUpdateDTO(

    String status

) implements Serializable {}
