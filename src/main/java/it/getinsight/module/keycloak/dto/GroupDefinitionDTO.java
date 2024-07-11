package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class GroupDefinitionDTO {
    @JsonProperty("id")
    private String id = null;

    @JsonProperty("path")
    private String path = null;

    @JsonProperty("extendChildren")
    private Boolean extendChildren = null;

    public GroupDefinitionDTO id(String id) {
        this.id = id;
        return this;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public GroupDefinitionDTO path(String path) {
        this.path = path;
        return this;
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public GroupDefinitionDTO extendChildren(Boolean extendChildren) {
        this.extendChildren = extendChildren;
        return this;
    }


    public Boolean isExtendChildren() {
        return extendChildren;
    }

    public void setExtendChildren(Boolean extendChildren) {
        this.extendChildren = extendChildren;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GroupDefinitionDTO groupDefinition = (GroupDefinitionDTO) o;
        return Objects.equals(this.id, groupDefinition.id) &&
                Objects.equals(this.path, groupDefinition.path) &&
                Objects.equals(this.extendChildren, groupDefinition.extendChildren);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, path, extendChildren);
    }

    @Override
    public String toString() {

        String sb = "class GroupDefinitionDTO {\n" +
                "    id: " + toIndentedString(id) + "\n" +
                "    path: " + toIndentedString(path) + "\n" +
                "    extendChildren: " + toIndentedString(extendChildren) + "\n" +
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

