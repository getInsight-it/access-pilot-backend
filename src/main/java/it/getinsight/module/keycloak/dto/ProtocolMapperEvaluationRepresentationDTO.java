package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class ProtocolMapperEvaluationRepresentationDTO {
    @JsonProperty("mapperId")
    private String mapperId = null;

    @JsonProperty("mapperName")
    private String mapperName = null;

    @JsonProperty("containerId")
    private String containerId = null;

    @JsonProperty("containerName")
    private String containerName = null;

    @JsonProperty("containerType")
    private String containerType = null;

    @JsonProperty("protocolMapper")
    private String protocolMapper = null;

    public ProtocolMapperEvaluationRepresentationDTO mapperId(String mapperId) {
        this.mapperId = mapperId;
        return this;
    }


    public String getMapperId() {
        return mapperId;
    }

    public void setMapperId(String mapperId) {
        this.mapperId = mapperId;
    }

    public ProtocolMapperEvaluationRepresentationDTO mapperName(String mapperName) {
        this.mapperName = mapperName;
        return this;
    }


    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public ProtocolMapperEvaluationRepresentationDTO containerId(String containerId) {
        this.containerId = containerId;
        return this;
    }


    public String getContainerId() {
        return containerId;
    }

    public void setContainerId(String containerId) {
        this.containerId = containerId;
    }

    public ProtocolMapperEvaluationRepresentationDTO containerName(String containerName) {
        this.containerName = containerName;
        return this;
    }


    public String getContainerName() {
        return containerName;
    }

    public void setContainerName(String containerName) {
        this.containerName = containerName;
    }

    public ProtocolMapperEvaluationRepresentationDTO containerType(String containerType) {
        this.containerType = containerType;
        return this;
    }


    public String getContainerType() {
        return containerType;
    }

    public void setContainerType(String containerType) {
        this.containerType = containerType;
    }

    public ProtocolMapperEvaluationRepresentationDTO protocolMapper(String protocolMapper) {
        this.protocolMapper = protocolMapper;
        return this;
    }


    public String getProtocolMapper() {
        return protocolMapper;
    }

    public void setProtocolMapper(String protocolMapper) {
        this.protocolMapper = protocolMapper;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ProtocolMapperEvaluationRepresentationDTO protocolMapperEvaluationRepresentation = (ProtocolMapperEvaluationRepresentationDTO) o;
        return Objects.equals(this.mapperId, protocolMapperEvaluationRepresentation.mapperId) &&
                Objects.equals(this.mapperName, protocolMapperEvaluationRepresentation.mapperName) &&
                Objects.equals(this.containerId, protocolMapperEvaluationRepresentation.containerId) &&
                Objects.equals(this.containerName, protocolMapperEvaluationRepresentation.containerName) &&
                Objects.equals(this.containerType, protocolMapperEvaluationRepresentation.containerType) &&
                Objects.equals(this.protocolMapper, protocolMapperEvaluationRepresentation.protocolMapper);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mapperId, mapperName, containerId, containerName, containerType, protocolMapper);
    }

    @Override
    public String toString() {

        String sb = "class ProtocolMapperEvaluationRepresentationDTO {\n" +
                "    mapperId: " + toIndentedString(mapperId) + "\n" +
                "    mapperName: " + toIndentedString(mapperName) + "\n" +
                "    containerId: " + toIndentedString(containerId) + "\n" +
                "    containerName: " + toIndentedString(containerName) + "\n" +
                "    containerType: " + toIndentedString(containerType) + "\n" +
                "    protocolMapper: " + toIndentedString(protocolMapper) + "\n" +
                "}";
        return sb;
    }


    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}

