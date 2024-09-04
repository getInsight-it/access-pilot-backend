package it.getinsight.module.storage.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.storage.dto.StorageFileDTO;
import it.getinsight.module.storage.entity.StorageFileEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface StorageFileMapper extends BaseMapper<StorageFileEntity, StorageFileDTO>, BaseGenericObjectMapper<StorageFileDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(StorageFileDTO dto, @MappingTarget StorageFileEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<StorageFileDTO> toList(List<Object> value);


    @Mapping(
        target = "id",
        expression = "java(toLong(value, 0))"
    )
    @Mapping(
        target = "excluded",
        expression = "java(toBooleanFromString( value, 1, \"true\" ))"
    )
    @Mapping(
        target = "originalFilename",
        expression = "java(toString(value, 2))"
    )
    @Mapping(
        target = "filesize",
        expression = "java(toLong(value, 3))"
    )
    @Mapping(
        target = "mimeType",
        expression = "java(toString(value, 4))"
    )
    @Mapping(
        target = "bucket",
        expression = "java(toString(value, 5))"
    )
    @Mapping(
        target = "isPublic",
        expression = "java(toBooleanFromString( value, 6, \"true\" ))"
    )
    @Mapping(
        target = "ephemeral",
        expression = "java(toBooleanFromString( value, 7, \"true\" ))"
    )
    @Mapping(
        target = "downloadCount",
        expression = "java(toLong(value, 8))"
    )
    @Mapping(
        target = "fileId",
        expression = "java(java.util.UUID.fromString(toString(value, 9)))"
    )
    @Mapping(
        target = "requestId",
        expression = "java(toString(value, 10))"
    )
   StorageFileDTO toMap(Object value);
}
