package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AccessDTO {

    @JsonProperty("roles")
    private List<String> roles = null;

    @JsonProperty("verifyCaller")
    private Boolean verifyCaller = null;

    public AccessDTO roles(List<String> roles) {
        this.roles = roles;
        return this;
    }

    public AccessDTO addRolesItem(String rolesItem) {
        if (this.roles == null) {
            this.roles = new ArrayList<String>();
        }
        this.roles.add(rolesItem);
        return this;
    }


    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public AccessDTO verifyCaller(Boolean verifyCaller) {
        this.verifyCaller = verifyCaller;
        return this;
    }


    public Boolean isVerifyCaller() {
        return verifyCaller;
    }

    public void setVerifyCaller(Boolean verifyCaller) {
        this.verifyCaller = verifyCaller;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AccessDTO access = (AccessDTO) o;
        return Objects.equals(this.roles, access.roles) &&
                Objects.equals(this.verifyCaller, access.verifyCaller);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roles, verifyCaller);
    }

    @Override
    public String toString() {

        String sb = "class AccessDTO {\n" +
                "    roles: " + toIndentedString(roles) + "\n" +
                "    verifyCaller: " + toIndentedString(verifyCaller) + "\n" +
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

