package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;


public class AddressClaimSetDTO {
    @JsonProperty("formattedAddress")
    private String formattedAddress = null;

    @JsonProperty("streetAddress")
    private String streetAddress = null;

    @JsonProperty("locality")
    private String locality = null;

    @JsonProperty("region")
    private String region = null;

    @JsonProperty("postalCode")
    private String postalCode = null;

    @JsonProperty("country")
    private String country = null;

    public AddressClaimSetDTO formattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
        return this;
    }


    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }

    public AddressClaimSetDTO streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }


    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public AddressClaimSetDTO locality(String locality) {
        this.locality = locality;
        return this;
    }


    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public AddressClaimSetDTO region(String region) {
        this.region = region;
        return this;
    }


    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public AddressClaimSetDTO postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }


    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public AddressClaimSetDTO country(String country) {
        this.country = country;
        return this;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AddressClaimSetDTO addressClaimSet = (AddressClaimSetDTO) o;
        return Objects.equals(this.formattedAddress, addressClaimSet.formattedAddress) &&
                Objects.equals(this.streetAddress, addressClaimSet.streetAddress) &&
                Objects.equals(this.locality, addressClaimSet.locality) &&
                Objects.equals(this.region, addressClaimSet.region) &&
                Objects.equals(this.postalCode, addressClaimSet.postalCode) &&
                Objects.equals(this.country, addressClaimSet.country);
    }

    @Override
    public int hashCode() {
        return Objects.hash(formattedAddress, streetAddress, locality, region, postalCode, country);
    }

    @Override
    public String toString() {

        String sb = "class AddressClaimSetDTO {\n" +
                "    formattedAddress: " + toIndentedString(formattedAddress) + "\n" +
                "    streetAddress: " + toIndentedString(streetAddress) + "\n" +
                "    locality: " + toIndentedString(locality) + "\n" +
                "    region: " + toIndentedString(region) + "\n" +
                "    postalCode: " + toIndentedString(postalCode) + "\n" +
                "    country: " + toIndentedString(country) + "\n" +
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

