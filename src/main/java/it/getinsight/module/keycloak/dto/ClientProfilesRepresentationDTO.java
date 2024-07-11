package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class ClientProfilesRepresentationDTO {
    @JsonProperty("profiles")
    private List<ClientProfileRepresentationDTO> profiles = null;

    @JsonProperty("globalProfiles")
    private List<ClientProfileRepresentationDTO> globalProfiles = null;

    public ClientProfilesRepresentationDTO profiles(List<ClientProfileRepresentationDTO> profiles) {
        this.profiles = profiles;
        return this;
    }

    public ClientProfilesRepresentationDTO addProfilesItem(ClientProfileRepresentationDTO profilesItem) {
        if (this.profiles == null) {
            this.profiles = new ArrayList<ClientProfileRepresentationDTO>();
        }
        this.profiles.add(profilesItem);
        return this;
    }


    public List<ClientProfileRepresentationDTO> getProfiles() {
        return profiles;
    }

    public void setProfiles(List<ClientProfileRepresentationDTO> profiles) {
        this.profiles = profiles;
    }

    public ClientProfilesRepresentationDTO globalProfiles(List<ClientProfileRepresentationDTO> globalProfiles) {
        this.globalProfiles = globalProfiles;
        return this;
    }

    public ClientProfilesRepresentationDTO addGlobalProfilesItem(ClientProfileRepresentationDTO globalProfilesItem) {
        if (this.globalProfiles == null) {
            this.globalProfiles = new ArrayList<ClientProfileRepresentationDTO>();
        }
        this.globalProfiles.add(globalProfilesItem);
        return this;
    }


    public List<ClientProfileRepresentationDTO> getGlobalProfiles() {
        return globalProfiles;
    }

    public void setGlobalProfiles(List<ClientProfileRepresentationDTO> globalProfiles) {
        this.globalProfiles = globalProfiles;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ClientProfilesRepresentationDTO clientProfilesRepresentation = (ClientProfilesRepresentationDTO) o;
        return Objects.equals(this.profiles, clientProfilesRepresentation.profiles) &&
                Objects.equals(this.globalProfiles, clientProfilesRepresentation.globalProfiles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(profiles, globalProfiles);
    }

    @Override
    public String toString() {

        String sb = "class ClientProfilesRepresentationDTO {\n" +
                "    profiles: " + toIndentedString(profiles) + "\n" +
                "    globalProfiles: " + toIndentedString(globalProfiles) + "\n" +
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

