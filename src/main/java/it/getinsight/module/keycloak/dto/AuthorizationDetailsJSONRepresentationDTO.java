package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class AuthorizationDetailsJSONRepresentationDTO {
    @JsonProperty("type")
    private String type = null;

    @JsonProperty("locations")
    private List<String> locations = null;

    @JsonProperty("actions")
    private List<String> actions = null;

    @JsonProperty("datatypes")
    private List<String> datatypes = null;

    @JsonProperty("identifier")
    private String identifier = null;

    @JsonProperty("privileges")
    private List<String> privileges = null;

    @JsonProperty("customData")
    private Map<String, Object> customData = null;

    @JsonProperty("scopeNameFromCustomData")
    private String scopeNameFromCustomData = null;

    @JsonProperty("dynamicScopeParamFromCustomData")
    private String dynamicScopeParamFromCustomData = null;

    public AuthorizationDetailsJSONRepresentationDTO type(String type) {
        this.type = type;
        return this;
    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public AuthorizationDetailsJSONRepresentationDTO locations(List<String> locations) {
        this.locations = locations;
        return this;
    }

    public AuthorizationDetailsJSONRepresentationDTO addLocationsItem(String locationsItem) {
        if (this.locations == null) {
            this.locations = new ArrayList<String>();
        }
        this.locations.add(locationsItem);
        return this;
    }


    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public AuthorizationDetailsJSONRepresentationDTO actions(List<String> actions) {
        this.actions = actions;
        return this;
    }

    public AuthorizationDetailsJSONRepresentationDTO addActionsItem(String actionsItem) {
        if (this.actions == null) {
            this.actions = new ArrayList<String>();
        }
        this.actions.add(actionsItem);
        return this;
    }


    public List<String> getActions() {
        return actions;
    }

    public void setActions(List<String> actions) {
        this.actions = actions;
    }

    public AuthorizationDetailsJSONRepresentationDTO datatypes(List<String> datatypes) {
        this.datatypes = datatypes;
        return this;
    }

    public AuthorizationDetailsJSONRepresentationDTO addDatatypesItem(String datatypesItem) {
        if (this.datatypes == null) {
            this.datatypes = new ArrayList<String>();
        }
        this.datatypes.add(datatypesItem);
        return this;
    }


    public List<String> getDatatypes() {
        return datatypes;
    }

    public void setDatatypes(List<String> datatypes) {
        this.datatypes = datatypes;
    }

    public AuthorizationDetailsJSONRepresentationDTO identifier(String identifier) {
        this.identifier = identifier;
        return this;
    }


    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public AuthorizationDetailsJSONRepresentationDTO privileges(List<String> privileges) {
        this.privileges = privileges;
        return this;
    }

    public AuthorizationDetailsJSONRepresentationDTO addPrivilegesItem(String privilegesItem) {
        if (this.privileges == null) {
            this.privileges = new ArrayList<String>();
        }
        this.privileges.add(privilegesItem);
        return this;
    }


    public List<String> getPrivileges() {
        return privileges;
    }

    public void setPrivileges(List<String> privileges) {
        this.privileges = privileges;
    }

    public AuthorizationDetailsJSONRepresentationDTO customData(Map<String, Object> customData) {
        this.customData = customData;
        return this;
    }

    public AuthorizationDetailsJSONRepresentationDTO putCustomDataItem(String key, Object customDataItem) {
        if (this.customData == null) {
            this.customData = null;
        }
        this.customData.put(key, customDataItem);
        return this;
    }


    public Map<String, Object> getCustomData() {
        return customData;
    }

    public void setCustomData(Map<String, Object> customData) {
        this.customData = customData;
    }

    public AuthorizationDetailsJSONRepresentationDTO scopeNameFromCustomData(String scopeNameFromCustomData) {
        this.scopeNameFromCustomData = scopeNameFromCustomData;
        return this;
    }


    public String getScopeNameFromCustomData() {
        return scopeNameFromCustomData;
    }

    public void setScopeNameFromCustomData(String scopeNameFromCustomData) {
        this.scopeNameFromCustomData = scopeNameFromCustomData;
    }

    public AuthorizationDetailsJSONRepresentationDTO dynamicScopeParamFromCustomData(String dynamicScopeParamFromCustomData) {
        this.dynamicScopeParamFromCustomData = dynamicScopeParamFromCustomData;
        return this;
    }


    public String getDynamicScopeParamFromCustomData() {
        return dynamicScopeParamFromCustomData;
    }

    public void setDynamicScopeParamFromCustomData(String dynamicScopeParamFromCustomData) {
        this.dynamicScopeParamFromCustomData = dynamicScopeParamFromCustomData;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AuthorizationDetailsJSONRepresentationDTO authorizationDetailsJSONRepresentation = (AuthorizationDetailsJSONRepresentationDTO) o;
        return Objects.equals(this.type, authorizationDetailsJSONRepresentation.type) &&
                Objects.equals(this.locations, authorizationDetailsJSONRepresentation.locations) &&
                Objects.equals(this.actions, authorizationDetailsJSONRepresentation.actions) &&
                Objects.equals(this.datatypes, authorizationDetailsJSONRepresentation.datatypes) &&
                Objects.equals(this.identifier, authorizationDetailsJSONRepresentation.identifier) &&
                Objects.equals(this.privileges, authorizationDetailsJSONRepresentation.privileges) &&
                Objects.equals(this.customData, authorizationDetailsJSONRepresentation.customData) &&
                Objects.equals(this.scopeNameFromCustomData, authorizationDetailsJSONRepresentation.scopeNameFromCustomData) &&
                Objects.equals(this.dynamicScopeParamFromCustomData, authorizationDetailsJSONRepresentation.dynamicScopeParamFromCustomData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, locations, actions, datatypes, identifier, privileges, customData, scopeNameFromCustomData, dynamicScopeParamFromCustomData);
    }

    @Override
    public String toString() {

        String sb = "class AuthorizationDetailsJSONRepresentationDTO {\n" +
                "    type: " + toIndentedString(type) + "\n" +
                "    locations: " + toIndentedString(locations) + "\n" +
                "    actions: " + toIndentedString(actions) + "\n" +
                "    datatypes: " + toIndentedString(datatypes) + "\n" +
                "    identifier: " + toIndentedString(identifier) + "\n" +
                "    privileges: " + toIndentedString(privileges) + "\n" +
                "    customData: " + toIndentedString(customData) + "\n" +
                "    scopeNameFromCustomData: " + toIndentedString(scopeNameFromCustomData) + "\n" +
                "    dynamicScopeParamFromCustomData: " + toIndentedString(dynamicScopeParamFromCustomData) + "\n" +
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

