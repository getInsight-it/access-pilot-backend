package it.getinsight.module.role.service;


import it.getinsight.core.exception.InfraException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleFilterDTO;
import it.getinsight.module.role.dto.RoleResponseDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleFilterMapper;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.mapper.RoleRepresentationMapper;
import it.getinsight.module.role.mapper.RoleResponseMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.service.UserService;
import it.getinsight.utilitario.RetryUtils;
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
    private final KeycloakClient keycloakClient;
    private final UserService userService;
    private final RequestRepository requestRepository;
    private final LevelRepository levelRepository;

    public List<RoleResponseDTO> getAllRoles(String filter) {
        final var model = new RoleEntity();
        Optional.ofNullable(filter)
            .filter(StringUtils::isNotBlank)
            .ifPresent(o -> model.setClient(ClientEntity.builder().clientId(o).build()));
        final var matcher = ExampleMatcher
            .matchingAny()
            .withIgnoreNullValues()
            .withMatcher("client.clientId", ExampleMatcher.GenericPropertyMatcher::exact);

        final var example = Example.of(model, matcher);

        return roleRepository.findAll(example).stream()
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
            .withMatcher("key", ExampleMatcher.GenericPropertyMatcher::contains)
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
        var entity = roleRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return roleResponseMapper.toDto(entity);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizeRoles(List<String> clientIds) {
        var clients = keycloakClient.getClients().stream()
            .filter(client -> client.getAttributes().containsKey("acl.client.managed") && client.getAttributes().get("acl.client.managed").equals("true"))
            .filter(client -> clientIds.contains(client.getClientId()))
            .toList();
        for (ClientRepresentationDTO client : clients) {
            var roles = keycloakClient.getRolesByClientUUID(client.getId());
            var clientEntity = clientRepository.findByClientId(client.getClientId()).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);
            roles.forEach(role -> synchronizeRole(role, clientEntity, null, null));
        }
    }

    private void synchronizeRole(RoleRepresentationDTO role, ClientEntity clientEntity, LevelEntity levelEntity, RoleEntity roleParentEntity) {
        var roleOpAlreadySynchronized = roleRepository.findByRoleExternalId(role.id());
        var roleOpNotSynchronized = roleRepository.findByNameAndClient(role.name(), clientEntity);
        if (roleOpAlreadySynchronized.isPresent()) {
            saveUpdatesSynchronizedRole(role, clientEntity, levelEntity, roleParentEntity, roleOpAlreadySynchronized.get());
        } else if (roleOpNotSynchronized.isEmpty()) {
            saveNewRoleFromIDP(role, clientEntity, levelEntity);
        }
    }

    private void saveUpdatesSynchronizedRole(RoleRepresentationDTO role, ClientEntity clientEntity, LevelEntity levelEntity, RoleEntity roleParentEntity, RoleEntity roleEntity) {
        roleEntity.setDescription(role.description());
        roleEntity.setClient(clientEntity);
        roleEntity.setActive(true);
        roleEntity.setRoleExternalId(role.id());
        roleEntity.setName(role.name());
        roleEntity.setRole(roleParentEntity != null ? roleParentEntity : roleEntity.getRole());
        roleEntity.setLevel(levelEntity != null ? levelEntity : roleEntity.getLevel());
        roleRepository.save(roleEntity);
    }

    private void saveNewRoleFromIDP(RoleRepresentationDTO role, ClientEntity clientEntity, LevelEntity levelEntity) {
        var roleEntity = RoleEntity.builder()
            .roleExternalId(role.id())
            .name(role.name())
            .active(true)
            .level(levelEntity)
            .description(role.description())
            .client(clientEntity)
            .build();
        roleRepository.save(roleEntity);

    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserDTO> getOrImportApprovesByRoleId(Long id) {
        var roleEntity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        var roleParent = Optional.ofNullable(roleEntity.getRole()).orElseThrow(ROLE_NOT_FOUND_PARENT_ERROR::businessException);
        var role = keycloakClient.getRoleByNameAndClientUUID(roleParent.getName(), roleEntity.getClient().getClientUUID());
        return keycloakClient.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), role.name()).stream()
            .map(user ->
                userService.findOrImportByExternalId(user.id())
            )
            .toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateRoles(List<RoleDTO> roles) {
        for (RoleDTO role : roles) {
            final var entity = roleRepository.findById(role.id()).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
            roleMapper.fromDto(role, entity);
            final var roleEntityParent = role.roleParent() != null ? roleRepository.findById(role.roleParent().id()).orElseThrow(() -> new ResourceNotFoundException("Role parent not found")) : null;
            entity.setRole(roleEntityParent);
            roleRepository.save(entity);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RoleDTO createRole(RoleDTO roleDTO) {
        validateRoleInput(roleDTO);

        final var roleToCreate = mapToRepresentation(roleDTO);

        try {
            ensureRoleDoesNotExist(roleDTO);
            keycloakClient.createRole(roleDTO.client().clientUUID(), roleToCreate);
            synchronizeWithDatabase(roleDTO);
        } catch (InfraException e) {
            log.info("Role already exists: {}", roleDTO.name());
            throw ROLE_ALREADY_EXISTS_ERROR.businessException(e);
        }

        return roleDTO;
    }

    private void validateRoleInput(RoleDTO roleDTO) {
        if (roleDTO == null) {
            throw ROLE_NOT_FOUND_ERROR.businessException();
        }
        if (roleDTO.client() == null) {
            throw CLIENT_NOT_FOUND_ERROR.businessException();
        }
    }

    private RoleRepresentationDTO mapToRepresentation(RoleDTO roleDTO) {
        return Optional.of(roleDTO)
            .map(roleRepresentationMapper::toRoleRepresentationDTO)
            .orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
    }

    private void ensureRoleDoesNotExist(RoleDTO roleDTO) {
        try {
            final var role = keycloakClient.getRole(roleDTO.client().clientUUID(), roleDTO.name());
            if (role != null)
                throw ROLE_ALREADY_EXISTS_IDP_ERROR.businessException();
        } catch (InfraException | ResourceNotFoundException e) {
               log.info("Role not found: {}", roleDTO.name());
        }
    }

    private void synchronizeWithDatabase(RoleDTO roleDTO) {
        final var createdRole = RetryUtils.retryOn404(2, 500, () ->
            keycloakClient.getRole(roleDTO.client().clientUUID(), roleDTO.name())
        );

        final var level = roleDTO.levelId() != null
            ? levelRepository.findById(roleDTO.levelId()).orElseThrow(LEVEL_NOT_FOUND_ERROR::businessException)
            : null;

        final var client = clientRepository.findByClientId(roleDTO.client().clientId())
            .orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);

        final var parentRole = roleDTO.roleParent() != null
            ? roleRepository.findById(roleDTO.roleParent().id()).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException)
            : null;

        synchronizeRole(createdRole, client, level, parentRole);
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

        if (StringUtils.isNotBlank(entity.getRoleExternalId())) {
            Optional.ofNullable(keycloakClient.getRole(entity.getClient().getClientUUID(), entity.getName()))
                .ifPresent(o -> keycloakClient.updateRole(entity.getClient().getClientUUID(), entity.getName(), RoleRepresentationDTO.builder()
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
            keycloakClient.deleteRole(roleEntity.getClient().getClientUUID(), roleEntity.getName());
        } catch (InfraException e) {
            log.warn("Role already was deleted from IDP: {}", roleEntity.getName());
        }
        roleRepository.softDelete(id);
    }

}
