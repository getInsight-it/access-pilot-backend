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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final LevelRepository levelRepository;

    private final RoleValidationService roleValidationService;
    private final RoleLevelPolicyService roleLevelPolicyService;

    public List<RoleResponseDTO> getAllRoles(String filter, Boolean hasParent) {
        return roleRepository.findAll(Specification.where(hasClientId(filter)).and(hasParent(hasParent))).stream()
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
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("client.id", ExampleMatcher.GenericPropertyMatcher::exact);

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
        var entity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        return roleResponseMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizeRoles(List<String> clientIds) {
        roleSynchronizationService.synchronizeRoles(clientIds);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserDTO> getOrImportApprovesByRoleId(Long id) {
        var roleEntity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        var roleParent = Optional.ofNullable(roleEntity.getRole()).orElseThrow(ROLE_NOT_FOUND_PARENT_ERROR::businessException);
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
            final var entity = roleRepository.findById(role.id()).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
            final var roleEntityParent = role.parentId() != null ? roleRepository.findById(role.parentId()).orElseThrow(ROLE_NOT_FOUND_PARENT_ERROR::businessException) : null;
            final var clientEntity = role.clientId() != null ? clientRepository.findById(role.clientId()).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException) : null;

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

        // Validar hierarquia antes de criar
        if (roleDTO.roleParent() != null && roleDTO.roleParent().id() != null) {
            var parentRole = roleRepository.findById(roleDTO.roleParent().id()).orElseThrow(ROLE_NOT_FOUND_PARENT_ERROR::businessException);
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
            roleSynchronizationService.synchronizeWithDatabase(roleMapper.toEntity(roleDTO));
        } catch (InfraException e) {
            log.info("Role already exists: {}", roleDTO.name());
            throw ROLE_ALREADY_EXISTS_ERROR.businessException(e);
        }

        return roleDTO;
    }

    private RoleRepresentationDTO mapToRepresentation(RoleDTO roleDTO) {
        return Optional.of(roleDTO)
            .map(roleRepresentationMapper::toRoleRepresentationDTO)
            .orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
    }

    public Long getTotalRoles() {
        return roleRepository.count();
    }

    public Long getTotalRoles(List<RoleEntity> roles) {
        return roles.stream().distinct().count();
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public RoleDTO update(Long id, RoleDTO roleDTO) {
        var entity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        if (requestRepository.countByStatusAndRole(RequestStatus.PENDING, entity) > 0) {
            throw ROLE_WITH_PENDING_REQUESTS_ERROR.businessException();
        }

        if (entity.getRole() != null && entity.getRole().getId() != null) {
            Long parentLevelId =  entity.getRole().getLevel() != null ? entity.getRole().getLevel().getId() : null;
            Long selfLevelId = roleDTO.levelId();

            roleLevelPolicyService.validateChildLevelAssignment(parentLevelId,selfLevelId);
        }

        if (StringUtils.isNotBlank(entity.getRoleExternalId())) {
            Optional.ofNullable(identityProviderService.getRole(entity.getClient().getClientUUID(), entity.getName()))
                .ifPresent(o -> identityProviderService.updateRole(entity.getClient().getClientUUID(), entity.getName(), RoleRepresentationDTO.builder()
                    .name(roleDTO.name())
                    .description(roleDTO.description())
                    .build()));
        }
        if (roleDTO.levelId() != null) {
            levelRepository.findById(roleDTO.levelId()).ifPresent(entity::setLevel);
        }
        if (roleDTO.roleParent() != null && roleDTO.roleParent().id() != null) {
            roleRepository.findById(roleDTO.roleParent().id()).ifPresent(entity::setRole);
        }

        entity.setName(roleDTO.name());
        entity.setDescription(roleDTO.description());
        entity.setLabel(roleDTO.label());
        entity.setIcon(roleDTO.icon());
        return roleMapper.toDto(roleRepository.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void delete(Long id) {
        var roleEntity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        try {
            identityProviderService.deleteRole(roleEntity.getClient().getClientUUID(), roleEntity.getName());
        } catch (InfraException e) {
            log.warn("Role already was deleted from IDP: {}", roleEntity.getName());
        }
        roleRepository.softDelete(id);
    }

}
