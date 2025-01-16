package it.getinsight.module.client.entity;

import lombok.Getter;

@Getter
public enum ClientStatus {

    PUBLISHED("publicado"),
    UNPUBLISHED("Não publicado");

    private final String description;

    ClientStatus(String description) {
        this.description = description;
    }


}
