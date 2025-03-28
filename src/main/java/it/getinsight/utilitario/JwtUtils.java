package it.getinsight.utilitario;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Component
public class JwtUtils{

    private JwtUtils(){}


    public static List<String> extractRoles(Object value) {
        if (value instanceof Map) {
            Object roles = ((Map<?, ?>) value).get("roles");
            if (roles instanceof List) {
                return ((List<?>) roles).stream()
                    .filter(String.class::isInstance)
                    .map(String.class::cast)
                    .toList();
            }
        }
        return Collections.emptyList();
    }



}
