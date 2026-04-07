package it.getinsight.module.shared.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Optional;

public enum ColorPalette {

    RED_LIGHT("#EF9A9A"),
    RED("#E53935"),
    RED_DARK("#B71C1C"),

    ORANGE_LIGHT("#FFCC80"),
    ORANGE("#FB8C00"),
    ORANGE_DARK("#E65100"),

    AMBER_LIGHT("#FFE082"),
    AMBER("#FFB300"),
    AMBER_DARK("#FF6F00"),

    YELLOW_LIGHT("#FFF59D"),
    YELLOW("#FDD835"),
    YELLOW_DARK("#F57F17"),

    LIME_LIGHT("#E6EE9C"),
    LIME("#C0CA33"),
    LIME_DARK("#827717"),

    GREEN_LIGHT("#A5D6A7"),
    GREEN("#43A047"),
    GREEN_DARK("#1B5E20"),

    TEAL_LIGHT("#80CBC4"),
    TEAL("#00897B"),
    TEAL_DARK("#004D40"),

    CYAN_LIGHT("#80DEEA"),
    CYAN("#00ACC1"),
    CYAN_DARK("#006064"),

    BLUE_LIGHT("#90CAF9"),
    BLUE("#1E88E5"),
    BLUE_DARK("#0D47A1"),

    INDIGO_LIGHT("#9FA8DA"),
    INDIGO("#3949AB"),
    INDIGO_DARK("#1A237E"),

    PURPLE_LIGHT("#CE93D8"),
    PURPLE("#8E24AA"),
    PURPLE_DARK("#4A148C"),

    PINK_LIGHT("#F48FB1"),
    PINK("#D81B60"),
    PINK_DARK("#880E4F");

    private final String hex;

    ColorPalette(String hex) {
        this.hex = hex;
    }

    public String hex() {
        return hex;
    }

    @JsonValue
    public String jsonValue() {
        return hex;
    }

    public static Optional<ColorPalette> fromHex(String hex) {
        return Arrays.stream(values())
            .filter(color -> color.hex.equals(hex))
            .findFirst();
    }

    public static Optional<ColorPalette> fromName(String name) {
        return Arrays.stream(values())
            .filter(color -> color.name().equals(name))
            .findFirst();
    }

    @JsonCreator
    public static ColorPalette fromJson(String value) {
        if (value == null) {
            return null;
        }
        String normalized = value.trim().toUpperCase();
        return fromHex(normalized)
            .or(() -> fromName(normalized))
            .orElseThrow(() -> new IllegalArgumentException("Invalid color: " + value));
    }
}
