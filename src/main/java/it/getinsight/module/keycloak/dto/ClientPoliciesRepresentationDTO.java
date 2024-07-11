package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientPoliciesRepresentationDTO {
    @JsonProperty("policies")
    private List<Object> policies = null;

    public ClientPoliciesRepresentationDTO policies(List<Object> policies) {
        this.policies = policies;
        return this;
    }

    public ClientPoliciesRepresentationDTO addPoliciesItem(Object policiesItem) {
        if (this.policies == null) {
            this.policies = new ArrayList<Object>();
        }
        this.policies.add(policiesItem);
        return this;
    }


    public List<Object> getPolicies() {
        return policies;
    }

    public void setPolicies(List<Object> policies) {
        this.policies = policies;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientPoliciesRepresentationDTO clientPoliciesRepresentation = (ClientPoliciesRepresentationDTO) o;
        return Objects.equals(this.policies, clientPoliciesRepresentation.policies);
    }

    @Override
    public int hashCode() {
        return Objects.hash(policies);
    }

    @Override
    public String toString() {

        String sb = "class ClientPoliciesRepresentationDTO {\n" +
                "    policies: " + toIndentedString(policies) + "\n" +
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

