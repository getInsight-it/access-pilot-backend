package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class UserPolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("users")
    private List<String> users = null;

    public UserPolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UserPolicyRepresentationDTO users(List<String> users) {
        this.users = users;
        return this;
    }

    public UserPolicyRepresentationDTO addUsersItem(String usersItem) {
        if (this.users == null) {
            this.users = new ArrayList<String>();
        }
        this.users.add(usersItem);
        return this;
    }


    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserPolicyRepresentationDTO userPolicyRepresentation = (UserPolicyRepresentationDTO) o;
        return Objects.equals(this.type, userPolicyRepresentation.type) &&
                Objects.equals(this.users, userPolicyRepresentation.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, users);
    }

    @Override
    public String toString() {

        String sb = "class UserPolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    users: " + toIndentedString(users) + "\n" +
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

