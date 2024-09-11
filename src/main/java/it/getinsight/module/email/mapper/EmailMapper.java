package it.getinsight.module.email.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.entity.EmailSentEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailMapper extends BaseMapper<EmailSentEntity, EmailDTO>, BaseGenericObjectMapper<EmailDTO> {

    @Mapping(target = "userId", source = "user.id")
    EmailDTO toDto(EmailSentEntity emailSentEntity);

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(EmailDTO dto, @MappingTarget EmailSentEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<EmailDTO> toList(List<Object> value);


    @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
    @Mapping(
        target = "to",
        expression = "java(toString( value, 1 ))"
    )
    @Mapping(
        target = "from",
        expression = "java(toString( value, 2 ))"
    )
    @Mapping(
        target = "subject",
        expression = "java(toString( value, 3 ))"
    )
    @Mapping(
        target = "isHtml",
        expression = "java(toBooleanFromString( value, 4, \"true\" ))"
    )
    @Mapping(
        target = "content",
        expression = "java(toString( value, 5 ))"
    )
    @Mapping(
        target = "userId",
        expression = "java(toLong( value, 6 ))"
    )
   EmailDTO toMap(Object value);
}
