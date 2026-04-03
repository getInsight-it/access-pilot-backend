package it.getinsight.module.role.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import lombok.Builder;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Builder
public record ApprovalPolicyResponseDTO(
    Long id,
    ApprovalPolicyType type,
    Boolean enabled,
    @JsonAlias("targetRoles")
    List<ApprovalPolicyRoleResponseDTO> roles
) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @JsonProperty("targetRoles")
    public List<ApprovalPolicyRoleResponseDTO> targetRoles() {
        return roles;
    }
}
