package it.getinsight.module.request.repository.specification;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestEntitySpecificationFilter implements Serializable {

    private static final long serialVersionUID = 5287296228628658948L;

    private Long id;

    private UUID uuid;

    private String protocolCode;

    private RequestStatus status;

    private String description;

    private String finalReason;

    private List<RoleEntity> roles;

    private UserEntity requestingUser;

    private UserEntity approvingUser;

    public static RequestEntitySpecificationFilter of(RequestEntity entity) {
        return RequestEntitySpecificationFilter.builder()
            .status(entity.getStatus())
            .roles(entity.getRole() != null ? Collections.singletonList(entity.getRole()) : List.of() )
            .requestingUser(entity.getRequestingUser())
            .approvingUser(entity.getApprovingUser())
            .description(entity.getDescription())
            .finalReason(entity.getFinalReason())
            .protocolCode(entity.getProtocolCode())
            .uuid(entity.getUuid())
            .id(entity.getId())
            .build();
    }
}
