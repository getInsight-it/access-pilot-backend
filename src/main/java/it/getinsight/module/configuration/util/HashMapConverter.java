package it.getinsight.module.configuration.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.Map;

import static it.getinsight.message.MessageProperty.ERROR_READING_JSON;
import static it.getinsight.message.MessageProperty.ERROR_WRITING_JSON;


@Converter
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<String, Object> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw ERROR_WRITING_JSON.infraException();
        }
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData, new TypeReference<>() {});
        } catch (Exception e) {
            throw ERROR_READING_JSON.infraException();
        }
    }
}
