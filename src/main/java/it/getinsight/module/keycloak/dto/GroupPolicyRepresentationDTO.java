package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class GroupPolicyRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("groupsClaim")
    private String groupsClaim = null;

    @JsonProperty("groups")
    private List<GroupDefinitionDTO> groups = null;

    public GroupPolicyRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public GroupPolicyRepresentationDTO groupsClaim(String groupsClaim) {
        this.groupsClaim = groupsClaim;
        return this;
    }


    public String getGroupsClaim() {
        return groupsClaim;
    }

    public void setGroupsClaim(String groupsClaim) {
        this.groupsClaim = groupsClaim;
    }

    public GroupPolicyRepresentationDTO groups(List<GroupDefinitionDTO> groups) {
        this.groups = groups;
        return this;
    }

    public GroupPolicyRepresentationDTO addGroupsItem(GroupDefinitionDTO groupsItem) {
        if (this.groups == null) {
            this.groups = new ArrayList<GroupDefinitionDTO>();
        }
        this.groups.add(groupsItem);
        return this;
    }


    public List<GroupDefinitionDTO> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDefinitionDTO> groups) {
        this.groups = groups;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupPolicyRepresentationDTO groupPolicyRepresentation = (GroupPolicyRepresentationDTO) o;
        return Objects.equals(this.type, groupPolicyRepresentation.type) &&
                Objects.equals(this.groupsClaim, groupPolicyRepresentation.groupsClaim) &&
                Objects.equals(this.groups, groupPolicyRepresentation.groups);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, groupsClaim, groups);
    }

    @Override
    public String toString() {

        String sb = "class GroupPolicyRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    groupsClaim: " + toIndentedString(groupsClaim) + "\n" +
                "    groups: " + toIndentedString(groups) + "\n" +
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

