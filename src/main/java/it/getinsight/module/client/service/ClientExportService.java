package it.getinsight.module.client.service;

import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.module.client.dto.ClientExportClientDTO;
import it.getinsight.module.client.dto.ClientExportDTO;
import it.getinsight.module.client.dto.ClientExportApprovalPolicyDTO;
import it.getinsight.module.client.dto.ClientExportApprovalPolicyRoleDTO;
import it.getinsight.module.client.dto.ClientExportRoleDTO;
import it.getinsight.module.client.dto.ClientImportCountersDTO;
import it.getinsight.module.client.dto.ClientImportRequestDTO;
import it.getinsight.module.client.dto.ClientImportResultDTO;
import it.getinsight.module.client.dto.ClientImportSummaryDTO;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.dto.ClientFilterDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.mapper.ClientExportMapper;
import it.getinsight.module.client.mapper.ClientFilterMapper;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.client.mapper.ClientRepresentationMapper;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.client.repository.specification.ClientSpecification;
import it.getinsight.module.configuration.dto.AttachmentConfigurationDTO;
import it.getinsight.module.configuration.entity.AttachmentConfigurationEntity;
import it.getinsight.module.configuration.mapper.AttachmentConfigurationMapper;
import it.getinsight.module.configuration.repository.AttachmentConfigurationRepository;
import it.getinsight.module.keycloak.config.KeycloakProperties;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.role.dto.ApprovalPolicyDTO;
import it.getinsight.module.role.dto.ApprovalPolicyRoleDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.ApprovalPolicyService;
import it.getinsight.module.role.service.RoleLevelPolicyService;
import it.getinsight.module.role.service.RoleValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static it.getinsight.message.MessageProperty.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientExportService {

    private static final String STATUS_CREATED = "CREATED";
    private static final String STATUS_UPDATED = "UPDATED";
    private static final String STATUS_IGNORED = "IGNORED";
    private static final String STATUS_ERROR = "ERROR";

    private final ClientRepository clientRepository;
    private final ClientExportMapper clientExportMapper;
    private final ClientFilterMapper clientFilterMapper;
    private final ClientMapper clientMapper;
    private final ClientRepresentationMapper clientRepresentationMapper;
    private final AttachmentConfigurationRepository attachmentConfigurationRepository;
    private final AttachmentConfigurationMapper attachmentConfigurationMapper;
    private final RoleRepository roleRepository;
    private final LevelRepository levelRepository;
    private final IdentityProviderService identityProviderService;
    private final RoleLevelPolicyService roleLevelPolicyService;
    private final ApprovalPolicyService approvalPolicyService;
    private final ClientValidationService clientValidationService;
    private final RoleValidationService roleValidationService;
    private final KeycloakProperties keycloakProperties;

    @Transactional(readOnly = true)
    public ClientExportDTO exportClient(Long clientId, boolean includeRoles, boolean includeConfigurations) {
        var client = clientRepository.findById(clientId)
            .orElseThrow(CLIENT_NOT_FOUND_ERROR::resourceNotFoundException);

        return exportClient(client, includeRoles, includeConfigurations);
    }

    @Transactional(readOnly = true)
    public List<ClientExportDTO> exportClientsPage(PageableRequestModel<ClientFilterDTO> configPage,
                                                   boolean includeRoles,
                                                   boolean includeConfigurations) {
        final var model = configPage
            .getFilter()
            .map(clientFilterMapper::toDto)
            .map(clientMapper::toEntity)
            .orElse(new ClientEntity());

        final var spec = Specification.anyOf(
            ClientSpecification.nameContains(model.getName()),
            ClientSpecification.clientIdContains(model.getClientId()),
            ClientSpecification.descriptionContains(model.getDescription()),
            ClientSpecification.statusContains(model.getStatus())
        );

        final var page = clientRepository.findAll(spec, PaginationHelper.toPageable(configPage));

        return page.getContent()
            .stream()
            .map(client -> exportClient(client, includeRoles, includeConfigurations))
            .toList();
    }

    private ClientExportDTO exportClient(ClientEntity client, boolean includeRoles, boolean includeConfigurations) {
        var clientExport = clientExportMapper.toExportClient(client);

        var configurations = includeConfigurations
            ? Optional.ofNullable(client.getConfigurations()).orElse(List.of()).stream()
                .map(attachmentConfigurationMapper::toDto)
                .toList()
            : List.<AttachmentConfigurationDTO>of();

        var roles = includeRoles
            ? roleRepository.findAllByClient(client).stream()
                .filter(role -> !roleValidationService.isIgnoredRoleName(role.getName()))
                .map(this::toExportRole)
                .toList()
            : List.<ClientExportRoleDTO>of();

        return ClientExportDTO.builder()
            .client(clientExport)
            .configurations(configurations)
            .roles(roles)
            .build();
    }

    private ClientExportRoleDTO toExportRole(RoleEntity roleEntity) {
        var base = clientExportMapper.toExportRole(roleEntity);
        return ClientExportRoleDTO.builder()
            .name(base.name())
            .label(base.label())
            .description(base.description())
            .icon(base.icon())
            .color(base.color())
            .parentName(base.parentName())
            .levelName(base.levelName())
            .levelType(base.levelType())
            .approvalPolicies(toExportApprovalPolicies(roleEntity))
            .build();
    }

    private List<ClientExportApprovalPolicyDTO> toExportApprovalPolicies(RoleEntity roleEntity) {
        var byType = new EnumMap<ApprovalPolicyType, it.getinsight.module.role.entity.ApprovalPolicyEntity>(ApprovalPolicyType.class);
        Optional.ofNullable(roleEntity.getApprovalPolicies()).orElse(List.of()).stream()
            .filter(Objects::nonNull)
            .filter(policy -> policy.getType() != null)
            .forEach(policy -> byType.put(policy.getType(), policy));

        return Arrays.stream(ApprovalPolicyType.values())
            .map(type -> {
                var policy = byType.get(type);
                return ClientExportApprovalPolicyDTO.builder()
                    .type(type)
                    .enabled(policy != null && Boolean.TRUE.equals(policy.getEnabled()))
                    .roles(toExportPolicyTargets(policy))
                    .build();
            })
            .toList();
    }

    private List<ClientExportApprovalPolicyRoleDTO> toExportPolicyTargets(it.getinsight.module.role.entity.ApprovalPolicyEntity policy) {
        if (policy == null || policy.getRoles() == null || policy.getRoles().isEmpty()) {
            return List.of();
        }
        return policy.getRoles().stream()
            .filter(Objects::nonNull)
            .filter(target -> Boolean.TRUE.equals(target.getActive()))
            .filter(target -> target.getRole() != null && StringUtils.isNotBlank(target.getRole().getName()))
            .map(target -> ClientExportApprovalPolicyRoleDTO.builder()
                .roleName(target.getRole().getName())
                .canApprove(Boolean.TRUE.equals(target.getCanApprove()))
                .canReject(Boolean.TRUE.equals(target.getCanReject()))
                .canRevoke(Boolean.TRUE.equals(target.getCanRevoke()))
                .build())
            .toList();
    }

    @CacheEvict(value = {"clients", "getTotalClients"}, allEntries = true)
    public ClientImportSummaryDTO importClients(ClientImportRequestDTO request) {
        var start = System.nanoTime();
        var results = new ArrayList<ClientImportResultDTO>();

        boolean force = isForce(request);
        boolean importRoles = shouldImportRoles(request);
        boolean importConfigurations = shouldImportConfigurations(request);

        var exports = resolveExports(request);
        for (var export : exports) {
            results.add(importClient(export, force, importRoles, importConfigurations));
        }

        return buildImportSummary(results, start);
    }

    private ClientImportResultDTO importClient(
        ClientExportDTO export,
        boolean force,
        boolean importRoles,
        boolean importConfigurations
    ) {
        var validationError = validateClientExport(export);
        if (validationError != null) {
            return validationError;
        }

        var clientId = normalizeClientId(export.client().clientId());
        var ignored = buildIgnoredClientResult(clientId);
        if (ignored != null) {
            return ignored;
        }

        try {
            return processClientImport(export, clientId, force, importRoles, importConfigurations);
        } catch (Exception ex) {
            log.error("Falha ao importar client '{}'", clientId, ex);
            return buildErrorClientResult(clientId, ex);
        }
    }

    private boolean isForce(ClientImportRequestDTO request) {
        return request != null && Boolean.TRUE.equals(request.force());
    }

    private boolean shouldImportRoles(ClientImportRequestDTO request) {
        return request == null || request.importRoles() == null || request.importRoles();
    }

    private boolean shouldImportConfigurations(ClientImportRequestDTO request) {
        return request == null || request.importConfigurations() == null || request.importConfigurations();
    }

    private List<ClientExportDTO> resolveExports(ClientImportRequestDTO request) {
        return request != null && CollectionUtils.isNotEmpty(request.exports())
            ? request.exports()
            : List.of();
    }

    private ClientImportResultDTO buildIgnoredClientResult(String clientId) {
        if (!isIgnoredClientId(clientId)) {
            return null;
        }
        return ClientImportResultDTO.builder()
            .clientId(clientId)
            .status(STATUS_IGNORED)
            .message(CLIENT_IMPORT_IGNORED.message())
            .build();
    }

    private ClientImportResultDTO validateClientExport(ClientExportDTO export) {
        if (export == null || export.client() == null || StringUtils.isBlank(export.client().clientId())) {
            return ClientImportResultDTO.builder()
                .status(STATUS_ERROR)
                .message(CLIENT_IMPORT_CLIENT_ID_REQUIRED.message())
                .build();
        }
        return null;
    }

    private ClientImportResultDTO processClientImport(ClientExportDTO export,
                                                      String clientId,
                                                      boolean force,
                                                      boolean importRoles,
                                                      boolean importConfigurations) {
        var existingEntity = clientRepository.findByClientId(clientId);
        if (existingEntity.isPresent() && !force) {
            return ClientImportResultDTO.builder()
                .clientId(clientId)
                .status(STATUS_IGNORED)
                .message(CLIENT_IMPORT_ALREADY_EXISTS_NOT_FORCED.message())
                .build();
        }

        var entity = existingEntity.orElse(new ClientEntity());
        var isNew = existingEntity.isEmpty();
        applyClientFromExport(entity, export.client());
        entity = clientRepository.save(entity);

        if (Boolean.TRUE.equals(entity.getManaged())) {
            entity = ensureManagedClientInIdp(entity);
        }

        var roleCounters = new ClientImportCountersDTO();
        var configurationCounters = new ClientImportCountersDTO();

        if (importConfigurations) {
            configurationCounters = importConfigurations(entity, export.configurations(), force);
        }

        if (importRoles) {
            roleCounters = importRoles(entity, export.roles(), force);
        }

        return ClientImportResultDTO.builder()
            .clientId(clientId)
            .status(isNew ? STATUS_CREATED : STATUS_UPDATED)
            .rolesCreated(roleCounters.getCreated())
            .rolesUpdated(roleCounters.getUpdated())
            .rolesDeleted(roleCounters.getDeleted())
            .configurationsCreated(configurationCounters.getCreated())
            .configurationsUpdated(configurationCounters.getUpdated())
            .configurationsDeleted(configurationCounters.getDeleted())
            .build();
    }

    private ClientImportResultDTO buildErrorClientResult(String clientId, Exception ex) {
        return ClientImportResultDTO.builder()
            .clientId(clientId)
            .status(STATUS_ERROR)
            .message(ex.getMessage())
            .build();
    }

    private ClientImportSummaryDTO buildImportSummary(List<ClientImportResultDTO> results, long start) {
        long created = 0;
        long updated = 0;
        long ignored = 0;
        long errors = 0;

        for (var result : results) {
            if (STATUS_CREATED.equals(result.status())) {
                created++;
            } else if (STATUS_UPDATED.equals(result.status())) {
                updated++;
            } else if (STATUS_IGNORED.equals(result.status())) {
                ignored++;
            } else if (STATUS_ERROR.equals(result.status())) {
                errors++;
            } else {
                ignored++;
            }
        }

        var duration = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
        return ClientImportSummaryDTO.builder()
            .created(created)
            .updated(updated)
            .ignored(ignored)
            .errors(errors)
            .duration(duration)
            .results(results)
            .build();
    }

    private void applyClientFromExport(ClientEntity entity, ClientExportClientDTO exportClient) {
        var clientId = normalizeClientId(exportClient.clientId());
        entity.setClientId(clientId);
        var name = StringUtils.trimToNull(exportClient.name());
        if (name == null) {
            name = clientId;
        }
        entity.setName(name);

        var label = StringUtils.trimToNull(exportClient.label());
        if (label == null) {
            label = name;
        }
        entity.setLabel(label);

        entity.setDescription(exportClient.description());
        entity.setBaseUrl(exportClient.baseUrl());
        entity.setManaged(Boolean.TRUE.equals(exportClient.managed()));
        entity.setStatus(resolveStatus(exportClient.status()));
    }

    private ClientEntity ensureManagedClientInIdp(ClientEntity entity) {
        var clients = identityProviderService.getClientsByClientId(entity.getClientId());
        if (CollectionUtils.isEmpty(clients)) {
            var defaultClient = ClientRepresentationDTO.createDefault(entity.getClientId(), entity.getDescription(), entity.getBaseUrl());
            identityProviderService.createClient(defaultClient);
            clients = identityProviderService.getClientsByClientId(entity.getClientId());
            if (CollectionUtils.isEmpty(clients)) {
                throw CLIENT_NOT_FOUND_ERROR.businessException();
            }
        }

        var representation = clients.getFirst();
        if (!Objects.equals(entity.getClientUUID(), representation.getId())) {
            entity.setClientUUID(representation.getId());
        }

        var dto = ClientDTO.builder()
            .id(entity.getId())
            .name(entity.getName())
            .label(entity.getLabel())
            .clientId(entity.getClientId())
            .clientUUID(entity.getClientUUID())
            .managed(entity.getManaged())
            .status(entity.getStatus() != null ? entity.getStatus().name() : null)
            .description(entity.getDescription())
            .baseUrl(entity.getBaseUrl())
            .configurations(List.of())
            .allowedItemsHierarchy(List.of())
            .build();
        clientRepresentationMapper.fromDtoRepresentation(dto, representation);
        identityProviderService.updateClient(representation.getId(), representation);

        return clientRepository.save(entity);
    }

    private ClientImportCountersDTO importConfigurations(ClientEntity client, List<AttachmentConfigurationDTO> configurations, boolean force) {
        var counters = new ClientImportCountersDTO();
        var configurationList = normalizeConfigurationList(configurations);
        var providedNames = collectConfigurationNames(configurationList);

        upsertConfigurations(client, configurationList, counters);
        if (force) {
            deleteMissingConfigurations(client, providedNames, counters);
        }

        clientValidationService.validateAttachmentConfigurations(attachmentConfigurationRepository.findAllByClient(client));
        return counters;
    }

    private ClientImportCountersDTO importRoles(ClientEntity client, List<ClientExportRoleDTO> roles, boolean force) {
        var counters = new ClientImportCountersDTO();
        var roleList = normalizeRoleList(roles);
        var providedNames = collectRoleNames(roleList);

        var upserted = upsertRoles(client, roleList, counters);
        applyRoleParents(client, roleList, upserted);
        applyApprovalPolicies(roleList, upserted);
        if (force) {
            deleteMissingRoles(client, providedNames, counters);
        }

        return counters;
    }

    private List<AttachmentConfigurationDTO> normalizeConfigurationList(List<AttachmentConfigurationDTO> configurations) {
        return configurations != null ? configurations : List.of();
    }

    private Set<String> collectConfigurationNames(List<AttachmentConfigurationDTO> configurationList) {
        return configurationList.stream()
            .map(AttachmentConfigurationDTO::name)
            .filter(StringUtils::isNotBlank)
            .map(name -> name.trim().toLowerCase())
            .collect(Collectors.toSet());
    }

    private void upsertConfigurations(ClientEntity client,
                                      List<AttachmentConfigurationDTO> configurationList,
                                      ClientImportCountersDTO counters) {
        for (var dto : configurationList) {
            if (dto == null || StringUtils.isBlank(dto.name())) {
                continue;
            }
            var name = dto.name().trim();
            var entity = findConfigurationByName(client.getId(), name);
            boolean created = false;
            if (entity == null) {
                entity = AttachmentConfigurationEntity.builder().build();
                entity.setName(name);
                created = true;
            }

            entity.setClient(client);
            entity.setDescription(dto.description());
            entity.setRequired(dto.required() != null ? dto.required() : Boolean.FALSE);
            entity.setAllowedExtensions(dto.allowedExtensions());
            entity.setIcon(dto.icon());
            entity.setColor(dto.color());
            entity.setActive(dto.active() != null ? dto.active() : Boolean.TRUE);

            attachmentConfigurationRepository.save(entity);
            if (created) {
                counters.setCreated(counters.getCreated() + 1);
            } else {
                counters.setUpdated(counters.getUpdated() + 1);
            }
        }
    }

    private void deleteMissingConfigurations(ClientEntity client,
                                             Set<String> providedNames,
                                             ClientImportCountersDTO counters) {
        for (var existing : attachmentConfigurationRepository.findAllByClient(client)) {
            var name = existing.getName();
            if (StringUtils.isBlank(name)) {
                continue;
            }
            if (!providedNames.contains(name.trim().toLowerCase())) {
                attachmentConfigurationRepository.softDelete(existing.getId());
                counters.setDeleted(counters.getDeleted() + 1);
            }
        }
    }

    private List<ClientExportRoleDTO> normalizeRoleList(List<ClientExportRoleDTO> roles) {
        return roles != null ? roles : List.of();
    }

    private Set<String> collectRoleNames(List<ClientExportRoleDTO> roleList) {
        return roleList.stream()
            .map(ClientExportRoleDTO::name)
            .filter(StringUtils::isNotBlank)
            .map(name -> name.trim().toLowerCase())
            .filter(name -> !roleValidationService.isIgnoredRoleName(name))
            .collect(Collectors.toSet());
    }

    private Map<String, RoleEntity> upsertRoles(ClientEntity client,
                                                List<ClientExportRoleDTO> roleList,
                                                ClientImportCountersDTO counters) {
        var upserted = new HashMap<String, RoleEntity>();
        for (var dto : roleList) {
            if (dto == null || StringUtils.isBlank(dto.name())) {
                continue;
            }
            var name = dto.name().trim();
            if (roleValidationService.isIgnoredRoleName(name)) {
                continue;
            }

            var entity = findRoleByName(client.getId(), name);
            boolean created = false;
            if (entity == null) {
                entity = RoleEntity.builder().build();
                entity.setName(name);
                entity.setActive(true);
                created = true;
            }
            var label = StringUtils.trimToNull(dto.label());
            entity.setClient(client);
            entity.setLabel(label != null ? label : name);
            entity.setDescription(dto.description());
            entity.setIcon(dto.icon());
            entity.setColor(dto.color());
            entity.setActive(true);
            entity.setLevel(resolveRoleLevel(dto));
            entity.setRole(null);

            boolean managedClient = Boolean.TRUE.equals(client.getManaged())
                && StringUtils.isNotBlank(client.getClientUUID());
            if (created && managedClient) {
                synchronizeRoleInIdp(client, entity);
            } else {
                roleRepository.save(entity);
                if (managedClient) {
                    synchronizeRoleInIdp(client, entity);
                }
            }
            upserted.put(name.toLowerCase(), entity);
            if (created) {
                counters.setCreated(counters.getCreated() + 1);
            } else {
                counters.setUpdated(counters.getUpdated() + 1);
            }

            synchronizeRoleInIdp(client, entity);
        }
        return upserted;
    }

    private void applyApprovalPolicies(List<ClientExportRoleDTO> roles, Map<String, RoleEntity> upserted) {
        for (var dto : roles) {
            if (dto == null || StringUtils.isBlank(dto.name())) {
                continue;
            }
            var role = upserted.get(dto.name().trim().toLowerCase(Locale.ROOT));
            if (role == null) {
                continue;
            }
            var approvalPolicies = toApprovalPolicies(dto.approvalPolicies(), upserted);
            approvalPolicyService.syncPolicies(role, approvalPolicies);
            roleRepository.save(role);
        }
    }

    private List<ApprovalPolicyDTO> toApprovalPolicies(List<ClientExportApprovalPolicyDTO> exportedPolicies,
                                                       Map<String, RoleEntity> upserted) {
        if (exportedPolicies == null || exportedPolicies.isEmpty()) {
            return List.of();
        }
        return exportedPolicies.stream()
            .filter(Objects::nonNull)
            .filter(policy -> policy.type() != null)
            .map(policy -> ApprovalPolicyDTO.builder()
                .type(policy.type())
                .enabled(Boolean.TRUE.equals(policy.enabled()))
                .roles(toApprovalPolicyRoles(policy, upserted))
                .build())
            .toList();
    }

    private List<ApprovalPolicyRoleDTO> toApprovalPolicyRoles(ClientExportApprovalPolicyDTO policy,
                                                              Map<String, RoleEntity> upserted) {
        if (policy == null || policy.type() == ApprovalPolicyType.AUTO_APPROVAL) {
            return List.of();
        }
        return Optional.ofNullable(policy.roles()).orElse(List.of()).stream()
            .filter(Objects::nonNull)
            .map(target -> {
                var targetRoleName = StringUtils.trimToNull(target.roleName());
                if (targetRoleName == null) {
                    return null;
                }
                var targetRole = upserted.get(targetRoleName.toLowerCase(Locale.ROOT));
                if (targetRole == null || targetRole.getId() == null) {
                    throw ROLE_NOT_FOUND_ERROR.resourceNotFoundException();
                }
                return ApprovalPolicyRoleDTO.builder()
                    .roleId(targetRole.getId())
                    .canApprove(Boolean.TRUE.equals(target.canApprove()))
                    .canReject(Boolean.TRUE.equals(target.canReject()))
                    .canRevoke(Boolean.TRUE.equals(target.canRevoke()))
                    .build();
            })
            .filter(Objects::nonNull)
            .toList();
    }

    private void deleteMissingRoles(ClientEntity client,
                                    Set<String> providedNames,
                                    ClientImportCountersDTO counters) {
        for (var existing : roleRepository.findAllByClient(client)) {
            var name = existing.getName();
            if (StringUtils.isBlank(name) || roleValidationService.isIgnoredRoleName(name)) {
                continue;
            }
            if (!providedNames.contains(name.trim().toLowerCase())) {
                roleRepository.softDelete(existing.getId());
                counters.setDeleted(counters.getDeleted() + 1);
                deleteRoleFromIdp(client, name);
            }
        }
    }

    private LevelEntity resolveRoleLevel(ClientExportRoleDTO dto) {
        if (dto == null) {
            return null;
        }
        boolean hasLevelName = StringUtils.isNotBlank(dto.levelName());
        boolean hasLevelType = StringUtils.isNotBlank(dto.levelType());
        if (!hasLevelName && !hasLevelType) {
            return null;
        }
        if (!hasLevelName || !hasLevelType) {
            throw LEVEL_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        LevelType type;
        try {
            type = LevelType.valueOf(dto.levelType().trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw LEVEL_NOT_FOUND_ERROR.resourceNotFoundException();
        }

        return levelRepository.findByNameIgnoreCaseAndTypeAndActiveTrue(dto.levelName().trim(), type)
            .orElseThrow(LEVEL_NOT_FOUND_ERROR::resourceNotFoundException);
    }

    private void applyRoleParents(ClientEntity client, List<ClientExportRoleDTO> roles, Map<String, RoleEntity> upserted) {
        for (var dto : roles) {
            if (dto == null || StringUtils.isBlank(dto.name()) || StringUtils.isBlank(dto.parentName())) {
                continue;
            }
            var roleName = dto.name().trim().toLowerCase();
            var parentName = dto.parentName().trim().toLowerCase();
            if (roleValidationService.isIgnoredRoleName(roleName) || roleValidationService.isIgnoredRoleName(parentName)) {
                continue;
            }

            var role = upserted.get(roleName);
            var parent = upserted.get(parentName);
            if (role == null || parent == null) {
                continue;
            }

            var parentLevelId = parent.getLevel() != null ? parent.getLevel().getId() : null;
            var roleLevelId = role.getLevel() != null ? role.getLevel().getId() : null;
            roleLevelPolicyService.validateChildLevelAssignment(parentLevelId, roleLevelId);

            role.setRole(parent);
            roleRepository.save(role);
        }
    }

    private void synchronizeRoleInIdp(ClientEntity client, RoleEntity role) {
        if (!Boolean.TRUE.equals(client.getManaged()) || StringUtils.isBlank(client.getClientUUID())) {
            return;
        }

        RoleRepresentationDTO representation;
        try {
            representation = identityProviderService.getRole(client.getClientUUID(), role.getName());
            identityProviderService.updateRole(client.getClientUUID(), role.getName(), RoleRepresentationDTO.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build());
        } catch (Exception ex) {
            identityProviderService.createRole(client.getClientUUID(), RoleRepresentationDTO.builder()
                .name(role.getName())
                .description(role.getDescription())
                .build());
            representation = identityProviderService.getRole(client.getClientUUID(), role.getName());
        }

        if (representation != null && representation.id() != null) {
            role.setRoleExternalId(representation.id());
            roleRepository.save(role);
        }
    }

    private void deleteRoleFromIdp(ClientEntity client, String roleName) {
        if (!Boolean.TRUE.equals(client.getManaged()) || StringUtils.isBlank(client.getClientUUID())) {
            return;
        }
        try {
            identityProviderService.deleteRole(client.getClientUUID(), roleName);
        } catch (Exception ex) {
            log.warn("Role '{}' ja estava removida no IDP", roleName);
        }
    }

    private RoleEntity findRoleByName(Long clientId, String name) {
        if (clientId == null || StringUtils.isBlank(name)) {
            return null;
        }
        return roleRepository.findByClientIdAndNameIgnoreCase(clientId, name).stream().findFirst().orElse(null);
    }

    private AttachmentConfigurationEntity findConfigurationByName(Long clientId, String name) {
        if (clientId == null || StringUtils.isBlank(name)) {
            return null;
        }
        return attachmentConfigurationRepository.findByClientIdAndNameIgnoreCase(clientId, name).stream().findFirst().orElse(null);
    }

    private ClientStatus resolveStatus(String status) {
        if (StringUtils.isBlank(status)) {
            return ClientStatus.UNPUBLISHED;
        }
        return Arrays.stream(ClientStatus.values())
            .filter(s -> s.name().equalsIgnoreCase(status))
            .findFirst()
            .orElseThrow(() -> CLIENT_INVALID_STATUS.bind(status).businessException());
    }

    private String normalizeClientId(String clientId) {
        return clientId.trim().toLowerCase();
    }

    private boolean isIgnoredClientId(String clientId) {
        if (clientId == null) {
            return false;
        }
        List<String> ignoreClients = keycloakProperties.getIgnoreClients();
        if (CollectionUtils.isEmpty(ignoreClients)) {
            return false;
        }
        return ignoreClients.contains(clientId.trim().toLowerCase());
    }

}
