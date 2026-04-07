package it.getinsight.module.shared.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class NativeStringMapper implements BaseGenericObjectMapper<String> {

    @Override
    public List<String> toList(List<Object> value) {
        return value.stream()
            .map(this::toMap)
            .filter(Objects::nonNull)
            .toList();
    }

    @Override
    public String toMap(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Object[] row) {
            if (row.length == 0 || row[0] == null) {
                return null;
            }
            return row[0].toString();
        }
        return value.toString();
    }
}
