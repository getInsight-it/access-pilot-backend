package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class RolePolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("roles")
    private List<RoleDefinitionDTO> roles = null;

    public RolePolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public RolePolicyRepresentationDTO roles(List<RoleDefinitionDTO> roles) {
        this.roles = roles;
        return this;
    }

    public RolePolicyRepresentationDTO addRolesItem(RoleDefinitionDTO rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<RoleDefinitionDTO>();
        }
        this.roles.add(rolesItem);
        return this;
    }


    public List<RoleDefinitionDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDefinitionDTO> roles) {
        this.roles = roles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RolePolicyRepresentationDTO rolePolicyRepresentation = (RolePolicyRepresentationDTO) o;
        return Objects.equals(this.type, rolePolicyRepresentation.type) &&
                Objects.equals(this.roles, rolePolicyRepresentation.roles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, roles);
    }

    @Override
    public String toString() {

        String sb = "class RolePolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    roles: " + toIndentedString(roles) + "\n" +
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

