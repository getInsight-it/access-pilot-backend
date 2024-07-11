package it.getinsight.module.solicitacao.mapper;

import it.getinsight.core.dynamicquery.model.mapper.BaseGenericObjectMapper;
import it.getinsight.core.model.mapper.BaseMapper;
import it.getinsight.module.solicitacao.dto.SolicitacaoDTO;
import it.getinsight.module.solicitacao.entity.SolicitacaoEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SolicitacaoMapper extends BaseMapper<SolicitacaoEntity, SolicitacaoDTO>, BaseGenericObjectMapper<SolicitacaoDTO> {

    @InheritInverseConfiguration(name = "toDto")
    void fromDto(SolicitacaoDTO dto, @MappingTarget SolicitacaoEntity entity);

    @InheritInverseConfiguration(name = "toMap")
    List<SolicitacaoDTO> toList(List<Object> value);


    @Mapping(
        target = "id",
        expression = "java(toLong( value, 0 ))"
    )
    @Mapping(
        target = "status",
        expression = "java(SolicitacaoStatus.valueOf(toString(value, 1 )))"
    )
    @Mapping(
        target = "userId",
        expression = "java(toString(value, 2))"
    )
    @Mapping(
        target = "roleId",
        expression = "java(toString(value, 3))"
    )
    SolicitacaoDTO toMap(Object value);


}
