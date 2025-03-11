package it.getinsight.module.keycloak.mapper;

import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserRepresentationMapper {


    default UserRepresentationDTO copy(UserRepresentationDTO source,  List<String> levelAttributes) {
        return source.withLevelAttributes(levelAttributes);
    }

}
