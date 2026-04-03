package it.getinsight.module.role.service;

import it.getinsight.core.exception.InfraException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.dto.UserRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.level.service.ItemService;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleFilterDTO;
import it.getinsight.module.role.dto.RoleResponseDTO;
import it.getinsight.module.role.dto.RoleUpdateHierarchyDTO;
import it.getinsight.module.role.entity.ApprovalPolicyRoleEntity;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleFilterMapper;
import it.getinsight.module.role.mapper.ApprovalPolicyMapper;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.mapper.RoleRepresentationMapper;
import it.getinsight.module.role.mapper.RoleResponseMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.service.ScopeRef;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.*;
import static it.getinsight.module.role.repository.specification.RoleSpecification.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {

    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final RoleMapper roleMapper;
    private final RoleResponseMapper roleResponseMapper;
    private final RoleRepresentationMapper roleRepresentationMapper;
    private final RoleFilterMapper roleFilterMapper;
    private final IdentityProviderService identityProviderService;
    private final RoleSynchronizationService roleSynchronizationService;
    private final ApprovalPolicyService approvalPolicyService;
    private final ApprovalPolicyMapper approvalPolicyMapper;
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final LevelRepository levelRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final KeycloakProperties keycloakProperties;

    private final RoleValidationService roleValidationService;
    private final RoleLevelPolicyService roleLevelPolicyService;
    private static final String ADMIN_ROLE_NAME = "ADMIN";

    public List<RoleResponseDTO> getAllRoles(String filter, Boolean hasParent) {
        return roleRepository.findAll(hasClientId(filter).and(hasParent(hasParent))).stream()
            .map(this::toRoleResponseDto)
            .toList();
    }

    public PageableResponseModel<RoleResponseDTO> getAllRolesPageable(PageableRequestModel<RoleFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(roleFilterMapper::toDto)
            .map(roleMapper::toEntity).orElse(new RoleEntity());

        final var clientId = model.getClient() != null ? model.getClient().getClientId() : null;

        final var spec = hasClientId(clientId)
            .and(Specification.anyOf(
                nameContains(model.getName()),
                labelContains(model.getLabel()),
                descriptionContains(model.getDescription())
            ));

        final var page = roleRepository.findAll(spec, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(
            page.getContent().stream().map(this::toRoleResponseDto).toList(),
            page.getTotalElements()
        );
    }

    public PageableResponseModel<RoleResponseDTO> getAllRolesPageableByName(PageableRequestModel<String> configPage) {
        final var model = new RoleEntity();
        configPage.getFilter().ifPresent(model::setName);

        final var spec = nameContains(model.getName());

        final var page = roleRepository.findAll(spec, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(
            page.getContent().stream().map(this::toRoleResponseDto).toList(),
            page.getTotalElements()
        );
    }

    public RoleResponseDTO getById(Long id) {
        var entity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        return toRoleResponseDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "securityScopes", allEntries = true)
    public void synchronizeRoles(List<String> clientIds) {
        roleSynchronizationService.synchronizeRoles(clientIds);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserDTO> getOrImportApprovesByRoleId(Long id) {
        var requestedRole = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        return unionApprovers(
            resolveApproversByHierarchy(requestedRole),
            resolveApproversByLateralPolicy(
                requestedRole,
                null,
                target -> Boolean.TRUE.equals(target.getCanApprove()) || Boolean.TRUE.equals(target.getCanReject())
            )
        );
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserDTO> getOrImportApprovesByRequest(RequestEntity requestEntity) {
        if (requestEntity == null || requestEntity.getRole() == null) {
            return List.of();
        }
        return unionApprovers(
            resolveApproversByHierarchy(requestEntity.getRole(), requestEntity),
            resolveApproversByLateralPolicy(
                requestEntity.getRole(),
                requestEntity,
                target -> Boolean.TRUE.equals(target.getCanApprove()) || Boolean.TRUE.equals(target.getCanReject())
            )
        );
    }

    private List<UserDTO> resolveApproversByHierarchy(RoleEntity requestedRole) {
        return resolveApproversByHierarchy(requestedRole, null);
    }

    private List<UserDTO> resolveApproversByHierarchy(RoleEntity requestedRole, RequestEntity requestEntity) {
        if (requestedRole.getRole() == null) {
            return findFallbackApprovers();
        }

        var parentApprovers = findEligibleApproversByRole(requestedRole.getRole(), requestEntity);
        if (!parentApprovers.isEmpty()) {
            return parentApprovers;
        }

        if (requestedRole.getRole().getRole() != null) {
            return List.of();
        }

        return findFallbackApprovers();
    }

    public boolean isFallbackScenario(RequestEntity requestEntity) {
        if (requestEntity == null || requestEntity.getRole() == null) {
            log.debug("Fallback scenario=false: request or role is null");
            return false;
        }
        if (!resolveApproversByLateralPolicy(
            requestEntity.getRole(),
            requestEntity,
            target -> Boolean.TRUE.equals(target.getCanApprove()) || Boolean.TRUE.equals(target.getCanReject())
        ).isEmpty()) {
            log.debug("Fallback scenario=false: lateral approvers found for requestId={}", requestEntity.getId());
            return false;
        }
        var requestedRole = requestEntity.getRole();
        if (requestedRole.getRole() == null) {
            log.debug("Fallback scenario=true: requested role has no parent for requestId={}", requestEntity.getId());
            return true;
        }
        if (requestedRole.getRole().getRole() != null) {
            log.debug("Fallback scenario=false: requested role parent has parent for requestId={}", requestEntity.getId());
            return false;
        }
        var fallback = findEligibleApproversByRole(requestedRole.getRole(), requestEntity).isEmpty();
        log.debug("Fallback scenario={} for requestId={} (parent approvers empty={})", fallback, requestEntity.getId(), fallback);
        return fallback;
    }

    private List<UserDTO> findEligibleApproversByRole(RoleEntity approverRole, RequestEntity requestEntity) {
        if (approverRole == null || approverRole.getClient() == null) {
            return List.of();
        }
        return identityProviderService.getUsersByClientUUIDAndRoleName(approverRole.getClient().getClientUUID(), approverRole.getName()).stream()
            .filter(user -> isEligibleApproverForRequest(user, approverRole, requestEntity))
            .map(user -> userService.findOrImportByExternalId(user.id()))
            .toList();
    }

    private List<UserDTO> resolveApproversByLateralPolicy(
        RoleEntity requestedRole,
        RequestEntity requestEntity,
        Predicate<ApprovalPolicyRoleEntity> targetPredicate
    ) {
        var lateralPolicy = approvalPolicyService.findEnabledLateralPolicy(requestedRole).orElse(null);
        if (lateralPolicy == null || lateralPolicy.getRoles() == null || lateralPolicy.getRoles().isEmpty()) {
            return List.of();
        }

        return lateralPolicy.getRoles().stream()
            .filter(target -> Boolean.TRUE.equals(target.getActive()))
            .filter(targetPredicate)
            .map(ApprovalPolicyRoleEntity::getRole)
            .filter(Objects::nonNull)
            .flatMap(targetRole -> findEligibleApproversByRole(targetRole, requestEntity).stream())
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(UserDTO::id, user -> user, (left, right) -> left),
                map -> List.copyOf(map.values())
            ));
    }

    private List<UserDTO> unionApprovers(List<UserDTO> first, List<UserDTO> second) {
        return java.util.stream.Stream.concat(
                Optional.ofNullable(first).orElse(List.of()).stream(),
                Optional.ofNullable(second).orElse(List.of()).stream()
            )
            .collect(Collectors.collectingAndThen(
                Collectors.toMap(UserDTO::id, user -> user, (left, right) -> left),
                map -> List.copyOf(map.values())
            ));
    }

    private boolean isEligibleApproverForRequest(UserRepresentationDTO user, RoleEntity approverRole, RequestEntity requestEntity) {
        if (requestEntity == null || requestEntity.getLevel() == null || StringUtils.isBlank(requestEntity.getCodeItem())) {
            return true;
        }
        if (approverRole.getLevel() == null) {
            return true;
        }
        return getUserLevelScopes(user).stream().anyMatch(scope -> matchesScopeForRequest(scope, approverRole, requestEntity));
    }

    private List<ScopeRef> getUserLevelScopes(UserRepresentationDTO user) {
        if (user == null || user.attributes() == null) {
            return List.of();
        }
        var rawScopes = user.attributes().get("levelAttributes");
        if (rawScopes == null || rawScopes.isEmpty()) {
            return List.of();
        }
        return rawScopes.stream().map(ScopeRef::parse).flatMap(Optional::stream).toList();
    }

    private boolean matchesScopeForRequest(ScopeRef scope, RoleEntity approverRole, RequestEntity requestEntity) {
        if (!Objects.equals(scope.clientId(), approverRole.getClient().getId())) {
            return false;
        }
        if (!Objects.equals(scope.roleId(), approverRole.getId())) {
            return false;
        }
        if (!Objects.equals(scope.levelId(), approverRole.getLevel().getId())) {
            return false;
        }
        if (Objects.equals(requestEntity.getLevel().getId(), approverRole.getLevel().getId())) {
            return Objects.equals(scope.codeItem(), requestEntity.getCodeItem());
        }
        return isChildItemFromScopeParent(scope.codeItem(), requestEntity);
    }

    private boolean isChildItemFromScopeParent(String parentCodeItem, RequestEntity requestEntity) {
        if (StringUtils.isBlank(parentCodeItem)) {
            return false;
        }
        try {
            if (LevelType.EXTERNAL.equals(requestEntity.getLevel().getType())) {
                return itemService.getAllSubitemCodes(requestEntity.getLevel().getId(), parentCodeItem).contains(requestEntity.getCodeItem());
            }
            return itemRepository.findAllByLevelIdAndParentExternalCode(requestEntity.getLevel().getId(), parentCodeItem).stream()
                .anyMatch(item -> Objects.equals(item.getExternalCode(), requestEntity.getCodeItem()));
        } catch (Exception e) {
            log.warn("Error validating scope hierarchy for request {}", requestEntity.getId(), e);
            return false;
        }
    }

    private List<UserDTO> findFallbackApprovers() {
        var accessPilotClient = clientRepository.findByClientId(keycloakProperties.getClientId()).orElse(null);
        if (accessPilotClient == null || StringUtils.isBlank(accessPilotClient.getClientUUID())) {
            log.debug("No fallback approvers: accesspilot client not found or missing UUID. clientId={}", keycloakProperties.getClientId());
            return List.of();
        }
        var users = identityProviderService.getUsersByClientUUIDAndRoleName(accessPilotClient.getClientUUID(), ADMIN_ROLE_NAME);
        log.debug("Fallback approvers fetched from IDP: {} user(s) for clientUUID={}", users.size(), accessPilotClient.getClientUUID());
        return users.stream()
            .map(user -> userService.findOrImportByExternalId(user.id()))
            .toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "securityScopes", allEntries = true)
    public void updateHierarchyRoles(List<RoleUpdateHierarchyDTO> roles) {
        for (RoleUpdateHierarchyDTO role : roles) {
            final var entity = roleRepository.findById(role.id()).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
            final var roleEntityParent = role.parentId() != null ? roleRepository.findById(role.parentId()).orElseThrow(ROLE_NOT_FOUND_PARENT_ERROR::resourceNotFoundException) : null;
            final var clientEntity = role.clientId() != null ? clientRepository.findById(role.clientId()).orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException) : null;

            if (roleEntityParent != null) {
                Long parentLevelId = roleEntityParent.getLevel() != null ? roleEntityParent.getLevel().getId() : null;
                Long selfLevelId = entity.getLevel() != null ? entity.getLevel().getId() : null;

                roleLevelPolicyService.validateChildLevelAssignment(parentLevelId,selfLevelId);
            }

            entity.setRole(roleEntityParent);
            entity.setClient(clientEntity);
            approvalPolicyService.syncPolicies(entity, approvalPolicyMapper.toRequestDtoList(entity.getApprovalPolicies()));
            roleRepository.save(entity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "securityScopes", allEntries = true)
    public RoleDTO createRole(RoleDTO roleDTO) {
        roleValidationService.validateRoleInput(roleDTO);


        if (roleDTO.roleParent() != null && roleDTO.roleParent().id() != null) {
            var parentRole = roleRepository.findById(roleDTO.roleParent().id()).orElseThrow(ROLE_NOT_FOUND_PARENT_ERROR::resourceNotFoundException);
            Long parentLevelId = parentRole.getLevel() != null ? parentRole.getLevel().getId() : null;
            Long childLevelId = roleDTO.levelId();

            roleLevelPolicyService.validateChildLevelAssignment(parentLevelId, childLevelId);
        }

        final var roleToCreate = mapToRepresentation(roleDTO);

        try {
            roleValidationService.ensureRoleDoesNotExistInIDP(roleDTO);
            identityProviderService.createRole(roleDTO.client().clientUUID(), roleToCreate);
            var roleEntity = roleMapper.toEntity(roleDTO);
            roleEntity.setLabel(roleDTO.label());
            roleEntity.setIcon(roleDTO.icon());
            if (roleDTO.levelId() != null) {
                levelRepository.findById(roleDTO.levelId()).ifPresent(roleEntity::setLevel);
            }
            roleSynchronizationService.synchronizeWithDatabase(roleEntity);
            var persistedRole = roleRepository.findByNameAndClient(roleDTO.name(), roleEntity.getClient())
                .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
            approvalPolicyService.syncPolicies(persistedRole, roleDTO.approvalPolicies());
            roleRepository.save(persistedRole);
        } catch (InfraException e) {
            log.info("Role already exists: {}", roleDTO.name());
            throw ROLE_ALREADY_EXISTS_ERROR.businessException(e);
        }

        return roleDTO;
    }

    private RoleRepresentationDTO mapToRepresentation(RoleDTO roleDTO) {
        return Optional.of(roleDTO)
            .map(roleRepresentationMapper::toRoleRepresentationDTO)
            .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
    }

    public Long getTotalRoles() {
        return roleRepository.count();
    }

    public Long getTotalRoles(List<RoleEntity> roles) {
        return roles.stream().distinct().count();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "securityScopes", allEntries = true)
    public RoleDTO update(Long id, RoleDTO roleDTO) {
        var entity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        if (requestRepository.countByStatusAndRole(RequestStatus.PENDING, entity) > 0) {
            throw ROLE_WITH_PENDING_REQUESTS_ERROR.businessException();
        }

        Long newLevelId = roleDTO.levelId();
        if (entity.getRole() != null && entity.getRole().getId() != null) {
            Long parentLevelId = entity.getRole().getLevel() != null ? entity.getRole().getLevel().getId() : null;
            roleLevelPolicyService.validateChildLevelAssignment(parentLevelId, newLevelId);
        }

        if (StringUtils.isNotBlank(entity.getRoleExternalId())) {
            Optional.ofNullable(identityProviderService.getRole(entity.getClient().getClientUUID(), entity.getName()))
                .ifPresent(o -> identityProviderService.updateRole(entity.getClient().getClientUUID(), entity.getName(), RoleRepresentationDTO.builder()
                    .name(roleDTO.name())
                    .description(roleDTO.description())
                    .build()));
        }
        if (newLevelId != null) {
            levelRepository.findById(newLevelId).ifPresent(entity::setLevel);
        } else {
            entity.setLevel(null);
        }
        if (roleDTO.roleParent() != null && roleDTO.roleParent().id() != null) {
            roleRepository.findById(roleDTO.roleParent().id()).ifPresent(entity::setRole);
        }

        entity.setName(roleDTO.name());
        entity.setDescription(roleDTO.description());
        entity.setLabel(roleDTO.label());
        entity.setIcon(roleDTO.icon());
        approvalPolicyService.syncPolicies(entity, roleDTO.approvalPolicies());
        var saved = roleRepository.save(entity);

        validateDescendantLevels(saved);
        return toRoleDto(saved);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @CacheEvict(value = "securityScopes", allEntries = true)
    public void delete(Long id) {
        var roleEntity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        try {
            identityProviderService.deleteRole(roleEntity.getClient().getClientUUID(), roleEntity.getName());
        } catch (InfraException e) {
            log.warn("Role already was deleted from IDP: {}", roleEntity.getName());
        }
        roleRepository.softDelete(id);
    }

    private void validateDescendantLevels(RoleEntity parentRole) {
        Long parentLevelId = parentRole.getLevel() != null ? parentRole.getLevel().getId() : null;
        Long clientId = parentRole.getClient() != null ? parentRole.getClient().getId() : null;
        var descendants = roleRepository.findDescendantRoles(parentRole.getId(), clientId);
        for (RoleEntity child : descendants) {
            Long childLevelId = child.getLevel() != null ? child.getLevel().getId() : null;
            roleLevelPolicyService.validateChildLevelAssignment(parentLevelId, childLevelId);
        }
    }

    private RoleResponseDTO toRoleResponseDto(RoleEntity entity) {
        var base = roleResponseMapper.toDto(entity);
        return RoleResponseDTO.builder()
            .id(base.id())
            .roleExternalId(base.roleExternalId())
            .name(base.name())
            .label(base.label())
            .icon(base.icon())
            .description(base.description())
            .roleParent(base.roleParent())
            .client(base.client())
            .level(base.level())
            .approvalPolicies(approvalPolicyMapper.toDtoList(entity.getApprovalPolicies()))
            .build();
    }

    private RoleDTO toRoleDto(RoleEntity entity) {
        var base = roleMapper.toDto(entity);
        return RoleDTO.builder()
            .id(base.id())
            .roleExternalId(base.roleExternalId())
            .name(base.name())
            .label(base.label())
            .icon(base.icon())
            .description(base.description())
            .roleParent(base.roleParent())
            .client(base.client())
            .levelId(base.levelId())
            .approvalPolicies(approvalPolicyMapper.toRequestDtoList(entity.getApprovalPolicies()))
            .build();
    }
}
