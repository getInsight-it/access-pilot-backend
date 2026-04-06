package it.getinsight.module.role.service;

import it.getinsight.module.role.dto.ApprovalPolicyDTO;
import it.getinsight.module.role.dto.ApprovalPolicyRoleDTO;
import it.getinsight.module.role.entity.ApprovalPolicyEntity;
import it.getinsight.module.role.entity.ApprovalPolicyRoleEntity;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import it.getinsight.module.role.repository.ApprovalPolicyRepository;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApprovalPolicyService {

    private final ApprovalPolicyRepository approvalPolicyRepository;
    private final RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public boolean isAutoApprovalEnabled(RoleEntity roleEntity) {
        return findPolicy(roleEntity, ApprovalPolicyType.AUTO_APPROVAL)
            .map(ApprovalPolicyEntity::getEnabled)
            .filter(Boolean.TRUE::equals)
            .isPresent();
    }

    @Transactional(readOnly = true)
    public Optional<ApprovalPolicyEntity> findEnabledLateralPolicy(RoleEntity roleEntity) {
        return findPolicy(roleEntity, ApprovalPolicyType.LATERAL_APPROVAL)
            .filter(policy -> Boolean.TRUE.equals(policy.getEnabled()));
    }

    @Transactional(readOnly = true)
    public List<ApprovalPolicyEntity> findEnabledLateralPoliciesByRoleIds(Collection<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return List.of();
        }
        return approvalPolicyRepository.findEnabledByTypeAndRoleIds(ApprovalPolicyType.LATERAL_APPROVAL, roleIds);
    }

    @Transactional(readOnly = true)
    public Optional<ApprovalPolicyEntity> findPolicy(RoleEntity roleEntity, ApprovalPolicyType type) {
        if (roleEntity == null || type == null) {
            return Optional.empty();
        }
        if (roleEntity.getId() != null) {
            return approvalPolicyRepository.findByRoleIdAndType(roleEntity.getId(), type);
        }
        return Optional.ofNullable(roleEntity.getApprovalPolicies())
            .orElse(List.of())
            .stream()
            .filter(policy -> type.equals(policy.getType()))
            .findFirst();
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public void syncPolicies(RoleEntity roleEntity, List<ApprovalPolicyDTO> inputPolicies) {
        var inputMap = new EnumMap<ApprovalPolicyType, ApprovalPolicyDTO>(ApprovalPolicyType.class);
        Optional.ofNullable(inputPolicies).orElse(List.of())
            .stream()
            .filter(Objects::nonNull)
            .filter(policy -> policy.type() != null)
            .forEach(policy -> inputMap.put(policy.type(), policy));

        var existingPolicies = roleEntity.getId() == null
            ? Optional.ofNullable(roleEntity.getApprovalPolicies()).orElse(List.of())
            : approvalPolicyRepository.findAllByRoleId(roleEntity.getId());

        var existingByType = new EnumMap<ApprovalPolicyType, ApprovalPolicyEntity>(ApprovalPolicyType.class);
        existingPolicies.stream()
            .filter(Objects::nonNull)
            .filter(policy -> policy.getType() != null)
            .forEach(policy -> existingByType.put(policy.getType(), policy));

        var normalized = new ArrayList<ApprovalPolicyEntity>();
        for (ApprovalPolicyType type : ApprovalPolicyType.values()) {
            var policyEntity = existingByType.getOrDefault(type, new ApprovalPolicyEntity());
            var policyInput = inputMap.get(type);
            applyPolicy(roleEntity, policyEntity, type, policyInput);
            normalized.add(policyEntity);
        }

        roleEntity.getApprovalPolicies().clear();
        roleEntity.getApprovalPolicies().addAll(normalized);
    }

    private void applyPolicy(RoleEntity roleEntity,
                             ApprovalPolicyEntity policyEntity,
                             ApprovalPolicyType type,
                             ApprovalPolicyDTO policyInput) {
        var enabled = policyInput != null && Boolean.TRUE.equals(policyInput.enabled());
        policyEntity.setRole(roleEntity);
        policyEntity.setType(type);
        policyEntity.setEnabled(enabled);
        policyEntity.setActive(true);

        if (type == ApprovalPolicyType.AUTO_APPROVAL) {
            policyEntity.getRoles().clear();
            return;
        }

        syncTargets(roleEntity, policyEntity, enabled, policyInput != null ? policyInput.roles() : List.of());
    }

    private void syncTargets(RoleEntity roleEntity,
                             ApprovalPolicyEntity policyEntity,
                             boolean policyEnabled,
                             List<ApprovalPolicyRoleDTO> targetInputs) {
        if (!policyEnabled) {
            policyEntity.getRoles().clear();
            return;
        }

        var currentParentId = roleEntity.getRole() != null ? roleEntity.getRole().getId() : null;
        if (currentParentId == null) {
            if (targetInputs != null && !targetInputs.isEmpty()) {
                throw LATERAL_POLICY_REQUIRES_PARENT_ERROR.businessException();
            }
            policyEntity.getRoles().clear();
            return;
        }

        var existingByTarget = new HashMap<Long, ApprovalPolicyRoleEntity>();
        Optional.ofNullable(policyEntity.getRoles()).orElse(List.of())
            .forEach(target -> {
                if (target != null && target.getRole() != null && target.getRole().getId() != null) {
                    existingByTarget.put(target.getRole().getId(), target);
                }
            });

        // Coletar IDs únicos para busca em batch (evita N+1)
        var targetRoleIds = Optional.ofNullable(targetInputs).orElse(List.of()).stream()
            .filter(Objects::nonNull)
            .map(ApprovalPolicyRoleDTO::roleId)
            .filter(Objects::nonNull)
            .distinct()
            .toList();

        // Buscar todos os roles em uma única query
        var targetRolesMap = roleRepository.findAllById(targetRoleIds).stream()
            .collect(Collectors.toMap(RoleEntity::getId, Function.identity()));

        var normalizedTargets = new ArrayList<ApprovalPolicyRoleEntity>();
        var visitedTargetIds = new HashSet<Long>();

        for (var targetInput : Optional.ofNullable(targetInputs).orElse(List.of())) {
            if (targetInput == null || targetInput.roleId() == null) {
                continue;
            }
            if (!visitedTargetIds.add(targetInput.roleId())) {
                continue;
            }

            var targetRole = targetRolesMap.get(targetInput.roleId());
            if (targetRole == null) {
                throw ROLE_NOT_FOUND_ERROR.resourceNotFoundException();
            }
            validateLateralTarget(roleEntity, targetRole);

            var targetEntity = existingByTarget.getOrDefault(targetInput.roleId(), new ApprovalPolicyRoleEntity());
            targetEntity.setPolicy(policyEntity);
            targetEntity.setRole(targetRole);
            targetEntity.setCanApprove(Boolean.TRUE.equals(targetInput.canApprove()));
            targetEntity.setCanReject(Boolean.TRUE.equals(targetInput.canReject()));
            targetEntity.setCanRevoke(Boolean.TRUE.equals(targetInput.canRevoke()));
            targetEntity.setActive(true);
            normalizedTargets.add(targetEntity);
        }

        policyEntity.getRoles().clear();
        policyEntity.getRoles().addAll(normalizedTargets);
    }

    private void validateLateralTarget(RoleEntity roleEntity, RoleEntity targetRole) {
        if (roleEntity == null || targetRole == null) {
            throw ROLE_NOT_FOUND_ERROR.businessException();
        }

        if (Objects.equals(roleEntity.getId(), targetRole.getId())) {
            throw LATERAL_TARGET_SELF_REFERENCE_ERROR.businessException();
        }
        if (!Boolean.TRUE.equals(targetRole.getActive())) {
            throw LATERAL_TARGET_INACTIVE_ERROR.businessException();
        }
        if (roleEntity.getClient() == null || targetRole.getClient() == null
            || !Objects.equals(roleEntity.getClient().getId(), targetRole.getClient().getId())) {
            throw LATERAL_TARGET_DIFFERENT_CLIENT_ERROR.businessException();
        }

        var roleLevelId = roleEntity.getLevel() != null ? roleEntity.getLevel().getId() : null;
        var targetLevelId = targetRole.getLevel() != null ? targetRole.getLevel().getId() : null;
        if (!Objects.equals(roleLevelId, targetLevelId)) {
            throw LATERAL_TARGET_DIFFERENT_LEVEL_ERROR.businessException();
        }

        var parentId = roleEntity.getRole() != null ? roleEntity.getRole().getId() : null;
        var targetParentId = targetRole.getRole() != null ? targetRole.getRole().getId() : null;
        if (parentId == null || targetParentId == null || !Objects.equals(parentId, targetParentId)) {
            throw LATERAL_TARGET_DIFFERENT_PARENT_ERROR.businessException();
        }
    }
}
