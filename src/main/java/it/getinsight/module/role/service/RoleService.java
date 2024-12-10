package it.getinsight.module.role.service;


import feign.FeignException;
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
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.dto.RoleFilterDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleFilterMapper;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.mapper.RoleRepresentationMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
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
    private final RoleRepresentationMapper roleRepresentationMapper;
    private final RoleFilterMapper roleFilterMapper;
    private final KeycloakClient keycloakClient;
    private final UserService userService;

    public List<RoleDTO> getAllRoles(String filter) {
        final var model = new RoleEntity();
        Optional.ofNullable(filter)
            .filter(StringUtils::isNotBlank)
            .ifPresent(o -> model.setClient(ClientEntity.builder().clientId(o).build()));
        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("client.clientId", ExampleMatcher.GenericPropertyMatcher::exact);

        final var example = Example.of(model, matcher);

        return roleRepository.findAll(example).stream()
            .map(roleMapper::toDto)
            .toList();
    }

    public PageableResponseModel<RoleDTO> getAllRolesPageable(PageableRequestModel<RoleFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(roleFilterMapper::toDto)
            .map(roleMapper::toEntity).orElse(new RoleEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("client.id", ExampleMatcher.GenericPropertyMatcher::exact);

        final var example = Example.of(model, matcher);

        final var page = roleRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(roleMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<RoleDTO> getAllRolesPageableByName(PageableRequestModel<String> configPage) {
        final var model = new RoleEntity();
        configPage.getFilter().ifPresent(model::setName);

        final var matcher = ExampleMatcher
            .matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase();

        final var example = Example.of(model, matcher);

        final var page = roleRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(roleMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public RoleDTO getById(Long id) {
        var entity = roleRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return roleMapper.toDto(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizeRoles(List<String> clientIds) {
        var clients = keycloakClient.getClients().stream()
            .filter(client -> client.attributes().containsKey("acl.client.managed") && client.attributes().get("acl.client.managed").equals("true"))
            .filter(client -> clientIds.contains(client.clientId()))
            .toList();
        for (ClientRepresentationDTO client : clients) {
            var roles = keycloakClient.getRolesByClientUUID(client.id());
            var clientEntity = clientRepository.findByClientId(client.clientId()).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException);
            roles.forEach(role -> synchronizeRole(role, clientEntity));
        }
    }

    private void synchronizeRole(RoleRepresentationDTO role, ClientEntity clientEntity) {
        var roleOpSynchronized = roleRepository.findByRoleExternalId(role.id());
        var roleOpNotSynchronized = roleRepository.findByNameAndClient(role.name(), clientEntity);
        if (roleOpSynchronized.isPresent()) {
             var roleEntity = roleOpSynchronized.get();
             roleEntity.setDescription(role.description());
             roleEntity.setClient(clientEntity);
             roleEntity.setRoleExternalId(role.id());
             roleEntity.setName(role.name());
             roleRepository.save(roleEntity);
        }else if (roleOpNotSynchronized.isEmpty()) {
            var roleEntity = RoleEntity.builder()
                .roleExternalId(role.id())
                .name(role.name())
                .description(role.description())
                .client(clientEntity)
                .build();
            roleRepository.save(roleEntity);
        }
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
        if (roleDTO == null) throw ROLE_NOT_FOUND_ERROR.businessException();

        final var newRoleRepresentationDTO = Optional.of(roleDTO)
            .map(roleRepresentationMapper::toRoleRepresentationDTO).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);

        if (roleDTO.client() == null) throw CLIENT_NOT_FOUND_ERROR.businessException();
        try {
            keycloakClient.createRole(roleDTO.client().clientUUID(), newRoleRepresentationDTO);
            final var roleRepresentationDTO = keycloakClient.getRole(roleDTO.client().clientUUID(), roleDTO.name());
            synchronizeRole(roleRepresentationDTO, clientRepository.findByClientId(roleDTO.client().clientId()).orElseThrow(CLIENT_NOT_FOUND_ERROR::businessException));
        }catch (InfraException e) {
            if(e.getCause() instanceof FeignException && ((FeignException) e.getCause()).status() == HttpStatus.CONFLICT.value()) {
                throw ROLE_ALREADY_EXISTS_ERROR.businessException();
            }
            throw e;
        }
        return roleDTO;
    }

    public Long getTotalRoles() {
        return roleRepository.count();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public RoleDTO update(Long id, RoleDTO roleDTO) {
        var entity = roleRepository.findById(id).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        if (StringUtils.isNotBlank(entity.getRoleExternalId())) {
            Optional.ofNullable(keycloakClient.getRole(entity.getClient().getClientUUID(), entity.getName()))
                .ifPresent((o) -> keycloakClient.updateRole(entity.getClient().getClientUUID(), entity.getName(), RoleRepresentationDTO.builder()
                    .name(roleDTO.name())
                    .description(roleDTO.description())
                    .build()));
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
        keycloakClient.deleteRole(roleEntity.getClient().getClientUUID(), roleEntity.getName());
        roleRepository.deleteById(id);
    }

}
