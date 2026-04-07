package it.getinsight.module.shared.service;

import it.getinsight.module.shared.dto.ColorUsageDTO;
import it.getinsight.module.shared.enums.ColorPalette;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.INVALID_COLOR_ERROR;

@Service
public class ColorPaletteService {

    private static final Pattern HEX_PATTERN = Pattern.compile("^#[0-9A-Fa-f]{6}$");

    public ColorPalette normalizeForPersistence(ColorPalette color) {
        return color;
    }

    public ColorPalette parseFromHex(String color) {
        if (StringUtils.isBlank(color)) {
            return null;
        }
        String normalized = color.trim().toUpperCase(Locale.ROOT);
        if (!HEX_PATTERN.matcher(normalized).matches()) {
            throw INVALID_COLOR_ERROR.bind(color).businessException();
        }
        return ColorPalette.fromHex(normalized)
            .orElseThrow(() -> INVALID_COLOR_ERROR.bind(color).businessException());
    }

    public java.util.List<ColorUsageDTO> toUsageList(Collection<String> usedColors) {
        Set<String> normalizedUsedColors = usedColors == null
            ? Set.of()
            : usedColors.stream()
            .filter(StringUtils::isNotBlank)
            .map(String::trim)
            .map(color -> color.toUpperCase(Locale.ROOT))
            .collect(Collectors.toSet());

        return java.util.Arrays.stream(ColorPalette.values())
            .map(color -> new ColorUsageDTO(color.hex(), normalizedUsedColors.contains(color.name())))
            .toList();
    }
}
