package it.getinsight.module.configuration.converter;

import it.getinsight.module.configuration.enums.FileExtensionType;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Converter(autoApply = false)
public class FileExtensionTypeSetConverter implements AttributeConverter<Set<FileExtensionType>, String> {

    @Override
    public String convertToDatabaseColumn(Set<FileExtensionType> attribute) {
        if (attribute == null || attribute.isEmpty()) return null;
        return attribute.stream()
                .map(Enum::name)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<FileExtensionType> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank()) return Set.of();
        return Arrays.stream(dbData.split(","))
                .map(String::trim)
                .map(String::toUpperCase)
                .map(FileExtensionType::valueOf)
                .collect(Collectors.toSet());
    }
}
