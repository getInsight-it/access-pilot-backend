package it.getinsight.module.keycloak.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Objects;


public class RefreshTokenDTO {

    @JsonProperty("category")
    private CategoryEnum category = null;

    public RefreshTokenDTO category(CategoryEnum category) {
        this.category = category;
        return this;
    }


    public CategoryEnum getCategory() {
        return category;
    }

    public void setCategory(CategoryEnum category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RefreshTokenDTO refreshToken = (RefreshTokenDTO) o;
        return Objects.equals(this.category, refreshToken.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(category);
    }

    @Override
    public String toString() {

        String sb = "class RefreshTokenDTO {\n" +
                "    category: " + toIndentedString(category) + "\n" +
                "}";
        return sb;
    }

    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }


    public enum CategoryEnum {
        INTERNAL("INTERNAL"),

        ACCESS("ACCESS"),

        ID("ID"),

        ADMIN("ADMIN"),

        USERINFO("USERINFO"),

        LOGOUT("LOGOUT"),

        AUTHORIZATION_RESPONSE("AUTHORIZATION_RESPONSE");

        private final String value;

        CategoryEnum(String value) {
            this.value = value;
        }

        @JsonCreator
        public static CategoryEnum fromValue(String text) {
            for (CategoryEnum b : CategoryEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }
    }
}

