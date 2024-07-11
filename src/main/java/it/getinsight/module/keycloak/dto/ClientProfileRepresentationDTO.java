package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientProfileRepresentationDTO {
    @JsonProperty("name")
    private String name = null;

    @JsonProperty("description")
    private String description = null;

    @JsonProperty("executors")
    private List<ClientPolicyExecutorRepresentationDTO> executors = null;

    public ClientProfileRepresentationDTO name(String name) {
        this.name = name;
        return this;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ClientProfileRepresentationDTO description(String description) {
        this.description = description;
        return this;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ClientProfileRepresentationDTO executors(List<ClientPolicyExecutorRepresentationDTO> executors) {
        this.executors = executors;
        return this;
    }

    public ClientProfileRepresentationDTO addExecutorsItem(ClientPolicyExecutorRepresentationDTO executorsItem) {
        if (this.executors == null) {
            this.executors = new ArrayList<ClientPolicyExecutorRepresentationDTO>();
        }
        this.executors.add(executorsItem);
        return this;
    }


    public List<ClientPolicyExecutorRepresentationDTO> getExecutors() {
        return executors;
    }

    public void setExecutors(List<ClientPolicyExecutorRepresentationDTO> executors) {
        this.executors = executors;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientProfileRepresentationDTO clientProfileRepresentation = (ClientProfileRepresentationDTO) o;
        return Objects.equals(this.name, clientProfileRepresentation.name) &&
                Objects.equals(this.description, clientProfileRepresentation.description) &&
                Objects.equals(this.executors, clientProfileRepresentation.executors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, executors);
    }

    @Override
    public String toString() {

        String sb = "class ClientProfileRepresentationDTO {\n" +
                "    name: " + toIndentedString(name) + "\n" +
                "    description: " + toIndentedString(description) + "\n" +
                "    executors: " + toIndentedString(executors) + "\n" +
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

