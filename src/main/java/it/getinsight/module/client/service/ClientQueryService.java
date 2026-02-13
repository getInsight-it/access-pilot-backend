package it.getinsight.module.client.service;

import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.dto.ClientFilterDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.mapper.ClientFilterMapper;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.client.mapper.ClientRepresentationMapper;
import it.getinsight.module.client.repository.ClientRepository;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.service.IdentityProviderService;
import it.getinsight.module.level.dto.ItemResponseNodeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import it.getinsight.module.client.repository.specification.ClientSpecification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;


@Service
@RequiredArgsConstructor
@Slf4j
public class ClientQueryService {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;
    private final ClientFilterMapper clientFilterMapper;
    private final ClientRepresentationMapper clientRepresentationMapper;
    private final IdentityProviderService identityProviderService;
    private final ClientItemTreeService clientItemTreeService;

    private static final String NAME_QUERY_FIND_ALL_CLIENTS = "find-all-clients";
    private static final String IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED = "acl.client.managed";


    public List<ClientDTO> getAllClientsDynamicQuery() {
        log.debug("Fetching all clients using dynamic query");
        final var parameters = DynamicParameters.get();
        return clientRepository.findAllNative(NAME_QUERY_FIND_ALL_CLIENTS, parameters, clientMapper);
    }


    public List<ClientDTO> getAllClientsPublished() {
        log.debug("Fetching all published clients");
        return clientRepository.findAllByStatus(ClientStatus.PUBLISHED)
            .stream()
            .map(clientMapper::toDto)
            .toList();
    }


    public PageableResponseModel<ClientDTO> getAllClientsPageable(PageableRequestModel<ClientFilterDTO> configPage) {
        log.debug("Fetching clients with pagination and filters");

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

        final var clientsNotSynchronized = page.getContent()
            .stream()
            .filter(o -> o.getClientUUID() == null)
            .map(clientMapper::toDto)
            .toList();

        final var clientsSynchronized = fetchUpdatedClientFromIDP(page.getContent());
        final var dtos = Stream.concat(clientsNotSynchronized.stream(), clientsSynchronized.stream()).toList();

        return PaginationHelper.toPageResponse(dtos, page.getTotalElements());
    }


    public List<ClientDTO> getAssociateClients(Boolean attached) {
        log.debug("Fetching associate clients with attached={}", attached);

        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> resourceAccess = principal.getClaim("resource_access");

        var clientEntities = clientRepository.findAllByManagedAndStatus(true, ClientStatus.PUBLISHED);

        return identityProviderService.getClients().stream()
            .filter(this::isManagedClient)
            .map(obj -> findLocalClientByClientId(clientEntities, obj.getClientId()))
            .filter(Objects::nonNull)
            .filter(obj -> filterByAttachment(obj, resourceAccess, attached))
            .map(client -> {
                List<ItemResponseNodeDTO> itemTree = clientItemTreeService.buildTreeForClient(client);
                return clientMapper.toDto(client).withAllowedItemsHierarchy(itemTree);
            })
            .toList();
    }

    @NotNull
    private List<ClientDTO> fetchUpdatedClientFromIDP(List<ClientEntity> page) {
        return page.stream()
            .filter(o -> o.getClientUUID() != null)
            .map(this::fetchClientFromIDPSafely)
            .flatMap(Optional::stream)
            .map(idpClient -> mapToClientDTO(page, idpClient))
            .toList();
    }


    private Optional<ClientRepresentationDTO> fetchClientFromIDPSafely(ClientEntity client) {
        try {
            return Optional.of(identityProviderService.getClientByUUID(client.getClientUUID()));
        } catch (it.getinsight.core.exception.ResourceNotFoundException e) {
            log.warn("Client {} not found in IDP, it may have been deleted", client.getClientId());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Failed to fetch client {} from IDP", client.getClientId(), e);
            return Optional.empty();
        }
    }


    private ClientDTO mapToClientDTO(List<ClientEntity> localClients, ClientRepresentationDTO idpClient) {
        var localClient = localClients.stream()
            .filter(e -> Objects.equals(e.getClientUUID(), idpClient.getId()))
            .findFirst()
            .orElse(null);
        return clientRepresentationMapper.toDto(localClient, idpClient);
    }


    private boolean isManagedClient(ClientRepresentationDTO client) {
        return client.getAttributes().containsKey(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED)
            && client.getAttributes().get(IDP_KEYCLOAK_NAME_ACL_CLIENT_MANAGED).equals("true");
    }


    private ClientEntity findLocalClientByClientId(List<ClientEntity> clientEntities, String clientId) {
        return clientEntities.stream()
            .filter(c -> Objects.equals(c.getClientId(), clientId))
            .findFirst()
            .orElse(null);
    }


    private boolean filterByAttachment(ClientEntity client, Map<String, List<String>> resourceAccess, Boolean attached) {
        boolean hasAccess = resourceAccess.entrySet().stream()
            .anyMatch(e -> Objects.equals(e.getKey(), client.getClientId()));

        return BooleanUtils.isTrue(attached) == hasAccess;
    }
}

