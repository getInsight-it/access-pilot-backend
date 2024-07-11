package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AuthorizationDTO {
    @JsonProperty("permissions")
    private List<PermissionDTO> permissions = null;

    public AuthorizationDTO permissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
        return this;
    }

    public AuthorizationDTO addPermissionsItem(PermissionDTO permissionsItem) {
        if (this.permissions == null) {
            this.permissions = new ArrayList<PermissionDTO>();
        }
        this.permissions.add(permissionsItem);
        return this;
    }


    public List<PermissionDTO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDTO> permissions) {
        this.permissions = permissions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizationDTO authorization = (AuthorizationDTO) o;
        return Objects.equals(this.permissions, authorization.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(permissions);
    }

    @Override
    public String toString() {

        String sb = "class AuthorizationDTO {\n" +
                "    permissions: " + toIndentedString(permissions) + "\n" +
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

