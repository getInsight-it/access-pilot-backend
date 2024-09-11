package it.getinsight.module.role.service;


import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final RoleMapper roleMapper;
    private final KeycloakClient keycloakClient;
    private final UserService userService;

    private static final String NAME_QUERY_FIND_ALL_ROLES = "find-all-roles";

    public List<RoleDTO> getAllRolesDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return roleRepository.findAllNative(NAME_QUERY_FIND_ALL_ROLES, parameters, roleMapper);
    }

    public PageableResponseModel<RoleDTO> getAllRolesPageable(PageableRequestModel<RoleDTO> configPage) {
        final var model = new RoleEntity();
        configPage
            .getFilter()
            .ifPresent( o -> {
                model.setName(o.name());
                model.setClient(new ClientEntity());
                model.getClient().setId(o.idClient());
            });

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
            var clientEntity = clientRepository.findByClientId(client.clientId()).orElseThrow(() -> new ResourceNotFoundException("Client not found"));
            for (RoleRepresentationDTO role : roles) {
                var entity = roleRepository.findByNameAndClient(role.name(), clientEntity).orElseGet(() -> {
                    var newEntity = new RoleEntity();
                    newEntity.setName(role.name());
                    newEntity.setClient(clientEntity);
                    newEntity.setRoleExternalId(role.id());
                    newEntity.setDescription(role.description());
                    return roleRepository.save(newEntity);
                });
                entity.setDescription(role.description());
                entity.setClient(clientEntity);
                entity.setRoleExternalId(role.id());
                entity.setName(role.name());
                roleRepository.save(entity);
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<UserDTO> getOrImportApprovesByRoleId(Long id) {
        var roleEntity = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
        var roleParent = Optional.ofNullable(roleEntity.getRole()).orElseThrow(() -> new ResourceNotFoundException("Role has no parent role"));
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
            final var entity = roleRepository.findById(role.id()).orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            roleMapper.fromDto(role, entity);
            final var roleEntityParent = role.idRoleParent() != null ? roleRepository.findById(role.idRoleParent()).orElseThrow(() -> new ResourceNotFoundException("Role parent not found")) : null;
            entity.setRole(roleEntityParent);
            roleRepository.save(entity);
        }
    }

    public Long getTotalRoles() {
        return roleRepository.count();
    }
}
