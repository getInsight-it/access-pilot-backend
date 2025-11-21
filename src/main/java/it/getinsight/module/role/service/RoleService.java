package it.getinsight.module.role.service;


import it.getinsight.core.exception.InfraException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleFilterDTO;
import it.getinsight.module.role.dto.RoleResponseDTO;
import it.getinsight.module.role.dto.RoleUpdateHierarchyDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleFilterMapper;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.mapper.RoleRepresentationMapper;
import it.getinsight.module.role.mapper.RoleResponseMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.service.UserService;
import it.getinsight.utilitario.PropertyPathConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.*;
import static it.getinsight.module.role.repository.specification.RoleSpecification.hasClientId;
import static it.getinsight.module.role.repository.specification.RoleSpecification.hasParent;


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
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final LevelRepository levelRepository;

    private final RoleValidationService roleValidationService;
    private final RoleLevelPolicyService roleLevelPolicyService;

    public List<RoleResponseDTO> getAllRoles(String filter, Boolean hasParent) {
        return roleRepository.findAll(hasClientId(filter).and(hasParent(hasParent))).stream()
            .map(roleResponseMapper::toDto)
            .toList();
    }

    public PageableResponseModel<RoleResponseDTO> getAllRolesPageable(PageableRequestModel<RoleFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(roleFilterMapper::toDto)
            .map(roleMapper::toEntity).orElse(new RoleEntity());

        final var matcher = ExampleMatcher
            .matchingAny()
            .withIgnoreNullValues()
            .withMatcher(PropertyPathConstants.Role.NAME, ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher(PropertyPathConstants.Role.CLIENT_ID, ExampleMatcher.GenericPropertyMatcher::exact);

        final var example = Example.of(model, matcher);

        final var page = roleRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(roleResponseMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<RoleResponseDTO> getAllRolesPageableByName(PageableRequestModel<String> configPage) {
        final var model = new RoleEntity();
        configPage.getFilter().ifPresent(model::setName);

        final var matcher = ExampleMatcher
            .matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase();

        final var example = Example.of(model, matcher);

        final var page = roleRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(roleResponseMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public RoleResponseDTO getById(Long id) {
        var entity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        return roleResponseMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizeRoles(List<String> clientIds) {
        roleSynchronizationService.synchronizeRoles(clientIds);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserDTO> getOrImportApprovesByRoleId(Long id) {
        var roleEntity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        var roleParent = Optional.ofNullable(roleEntity.getRole()).orElseThrow(ROLE_NOT_FOUND_PARENT_ERROR::resourceNotFoundException);
        var role = identityProviderService.getRoleByNameAndClientUUID(roleParent.getName(), roleEntity.getClient().getClientUUID());
        return identityProviderService.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), role.name()).stream()
            .map(user ->
                userService.findOrImportByExternalId(user.id())
            )
            .toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
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
            roleRepository.save(entity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
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
        var saved = roleRepository.save(entity);

        validateDescendantLevels(saved);
        return roleMapper.toDto(saved);
    }

    @Transactional(propagation = Propagation.REQUIRED)
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
}
