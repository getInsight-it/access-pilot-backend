package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AuthenticationExecutionRepresentationDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("flowId")
    private String flowId = null;

    @JsonProperty("parentFlow")
    private String parentFlow = null;

    public AuthenticationExecutionRepresentationDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public AuthenticationExecutionRepresentationDTO flowId(String flowId) {
        this.flowId = flowId;
        return this;
    }


    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public AuthenticationExecutionRepresentationDTO parentFlow(String parentFlow) {
        this.parentFlow = parentFlow;
        return this;
    }


    public String getParentFlow() {
        return parentFlow;
    }

    public void setParentFlow(String parentFlow) {
        this.parentFlow = parentFlow;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthenticationExecutionRepresentationDTO authenticationExecutionRepresentation = (AuthenticationExecutionRepresentationDTO) o;
        return Objects.equals(this.id, authenticationExecutionRepresentation.id) &&
                Objects.equals(this.flowId, authenticationExecutionRepresentation.flowId) &&
                Objects.equals(this.parentFlow, authenticationExecutionRepresentation.parentFlow);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, flowId, parentFlow);
    }

    @Override
    public String toString() {

        String sb = "class AuthenticationExecutionRepresentationDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    flowId: " + toIndentedString(flowId) + "\n" +
                "    parentFlow: " + toIndentedString(parentFlow) + "\n" +
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

