package it.getinsight.module.role.service;

import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleSynchronizationService {

    private final RoleRepository roleRepository;
    private final ClientRepository clientRepository;
    private final LevelRepository levelRepository;
    private final IdentityProviderService identityProviderService;
    private final RoleValidationService roleValidationService;

    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizeRoles(List<String> clientIds) {
        synchronizeRoles(clientIds, false);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizeRoles(List<String> clientIds, boolean force) {
        var clients = fetchClientsFromIDP(clientIds, force);
        for (ClientRepresentationDTO client : clients) {
            synchronizeClientRoles(client);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void synchronizeRolesToIdp(ClientEntity clientEntity) {
        if (!shouldSyncClientToIdp(clientEntity)) {
            return;
        }

        roleRepository.findAllByClient(clientEntity).stream()
            .filter(this::isValidLocalRole)
            .filter(role -> !roleValidationService.isIgnoredRoleName(role.getName()))
            .forEach(role -> synchronizeRoleInIdp(clientEntity, role));
    }

    private List<ClientRepresentationDTO> fetchClientsFromIDP(List<String> clientIds, boolean force) {
        if (clientIds == null || clientIds.isEmpty()) {
            return List.of();
        }
        return identityProviderService.getClients().stream()
            .filter(client -> clientIds.contains(client.getClientId()))
            .filter(client -> force || isManagedClient(client))
            .toList();
    }

    private boolean isManagedClient(ClientRepresentationDTO client) {
        return client.getAttributes().containsKey("acl.client.managed")
            && client.getAttributes().get("acl.client.managed").equals("true");
    }

    private void synchronizeClientRoles(ClientRepresentationDTO client) {
        var roles = identityProviderService.getRolesByClientUUID(client.getId());
        var clientEntity = clientRepository.findByClientId(client.getClientId())
            .orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);
        roles.stream()
            .filter(this::isValidRole)
            .filter(role -> !isIgnoredRole(role))
            .forEach(role -> synchronizeRole(role, clientEntity, null, null, null));
    }

    private boolean isValidRole(RoleRepresentationDTO role) {
        return role != null && role.name() != null && !role.name().isBlank();
    }

    private boolean isIgnoredRole(RoleRepresentationDTO role) {
        if (role == null || role.name() == null) {
            return false;
        }
        return roleValidationService.isIgnoredRoleName(role.name());
    }

    private void synchronizeRoleInIdp(ClientEntity client, RoleEntity role) {
        var roleRepresentation = findOrCreateRoleInIdp(client, role);
        if (roleRepresentation != null && StringUtils.isNotBlank(roleRepresentation.id())) {
            role.setRoleExternalId(roleRepresentation.id());
            roleRepository.save(role);
        }
    }

    private RoleRepresentationDTO findOrCreateRoleInIdp(ClientEntity client, RoleEntity role) {
        try {
            var representation = identityProviderService.getRole(client.getClientUUID(), role.getName());
            if (representation != null) {
                identityProviderService.updateRole(client.getClientUUID(), role.getName(), RoleRepresentationDTO.builder()
                    .name(role.getName())
                    .description(role.getDescription())
                    .build());
                return representation;
            }
        } catch (Exception ex) {
            log.debug("Role '{}' not found/updated in IDP for client '{}', will create. Reason: {}",
                role.getName(), client.getClientId(), ex.getMessage());
        }

        identityProviderService.createRole(client.getClientUUID(), RoleRepresentationDTO.builder()
            .name(role.getName())
            .description(role.getDescription())
            .build());
        return identityProviderService.getRole(client.getClientUUID(), role.getName());
    }

    private boolean shouldSyncClientToIdp(ClientEntity clientEntity) {
        return clientEntity != null
            && Boolean.TRUE.equals(clientEntity.getManaged())
            && StringUtils.isNotBlank(clientEntity.getClientUUID());
    }

    private boolean isValidLocalRole(RoleEntity role) {
        return role != null && StringUtils.isNotBlank(role.getName());
    }

    public void synchronizeRole(RoleRepresentationDTO role, ClientEntity clientEntity,
                               LevelEntity levelEntity, RoleEntity roleEntity,
                               RoleEntity roleParentEntity) {
        var roleOpAlreadySynchronized = roleRepository.findByRoleExternalId(role.id());
        var roleOpNotSynchronized = roleRepository.findByNameAndClient(role.name(), clientEntity);

        if (roleOpAlreadySynchronized.isPresent()) {
            updateExistingRole(role, clientEntity, levelEntity, roleParentEntity, roleOpAlreadySynchronized.get());
        } else if (roleOpNotSynchronized.isEmpty()) {
            createNewRoleFromIDP(role, roleEntity, clientEntity, levelEntity);
        }
    }

    private void updateExistingRole(RoleRepresentationDTO role, ClientEntity clientEntity,
                                   LevelEntity levelEntity, RoleEntity roleParentEntity,
                                   RoleEntity roleEntity) {
        roleEntity.setDescription(role.description());
        roleEntity.setClient(clientEntity);
        roleEntity.setActive(true);
        roleEntity.setLabel(resolveRoleLabel(role, roleEntity));
        roleEntity.setIcon(roleEntity.getIcon());
        roleEntity.setRoleExternalId(role.id());
        roleEntity.setName(role.name());
        roleEntity.setRole(roleParentEntity != null ? roleParentEntity : roleEntity.getRole());
        roleEntity.setLevel(levelEntity != null ? levelEntity : roleEntity.getLevel());
        roleRepository.save(roleEntity);
    }

    private void createNewRoleFromIDP(RoleRepresentationDTO role, RoleEntity roleEntityUnsaved,
                                     ClientEntity clientEntity, LevelEntity levelEntity) {
        var icon = roleEntityUnsaved != null ? roleEntityUnsaved.getIcon() : null;
        var color = roleEntityUnsaved != null ? roleEntityUnsaved.getColor() : null;
        var roleEntity = RoleEntity.builder()
            .roleExternalId(role.id())
            .name(role.name())
            .label(resolveRoleLabel(role, roleEntityUnsaved))
            .icon(icon)
            .color(color)
            .active(true)
            .level(levelEntity)
            .description(role.description())
            .client(clientEntity)
            .build();
        roleRepository.save(roleEntity);
    }

    private String resolveRoleLabel(RoleRepresentationDTO role, RoleEntity roleEntity) {
        if (roleEntity != null) {
            String existingLabel = roleEntity.getLabel();
            if (existingLabel != null && !existingLabel.isBlank()) {
                return existingLabel;
            }
        }
        String roleName = role != null ? role.name() : null;
        if (roleName == null || roleName.isBlank()) {
            return null;
        }
        return roleName;
    }

    public void synchronizeWithDatabase(RoleEntity roleEntity) {
        final var createdRole = identityProviderService.getRole(roleEntity.getClient().getClientUUID(), roleEntity.getName());
        final var level = resolveLevel(roleEntity);
        final var client = resolveClient(roleEntity);
        final var parentRole = resolveParentRole(roleEntity);
        synchronizeRole(createdRole, client, level, roleEntity, parentRole);
    }

    private LevelEntity resolveLevel(RoleEntity roleEntity) {
        return roleEntity.getLevel() != null
            ? levelRepository.findById(roleEntity.getLevel().getId())
                .orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException)
            : null;
    }

    private ClientEntity resolveClient(RoleEntity roleEntity) {
        return clientRepository.findByClientId(roleEntity.getClient().getClientId())
            .orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);
    }

    private RoleEntity resolveParentRole(RoleEntity roleEntity) {
        return roleEntity.getRole() != null
            ? roleRepository.findById(roleEntity.getRole().getId())
                .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException)
            : null;
    }
}
