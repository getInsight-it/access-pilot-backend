package it.getinsight.module.client.service;


import it.getinsight.client.KeycloakClient;
import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.dto.ClienteDTO;
import it.getinsight.module.client.entity.ClienteEntity;
import it.getinsight.module.client.mapper.ClienteMapper;
import it.getinsight.module.client.repository.ClienteRepository;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;
    private final KeycloakClient keycloakClient;

    private static final String NAME_QUERY_OBTER_TODOS_ROLES = "obter-todos-clientes";

    public List<ClienteDTO> getAllClientesDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return clienteRepository.findAllNative(NAME_QUERY_OBTER_TODOS_ROLES, parameters, clienteMapper);
    }

    public PageableResponseModel<ClienteDTO> getAllClientesPageable(PageableRequestModel<ClienteDTO> configPage) {
        final var model = new ClienteEntity();

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("clientId", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        final var page = clienteRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(clienteMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<ClienteDTO> getAllClientesPageableByName(PageableRequestModel<String> configPage) {
        final var model = new ClienteEntity();
        configPage.getFilter().ifPresent(model::setClientId);

        final var matcher = ExampleMatcher
            .matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase();

        final var example = Example.of(model, matcher);

        final var page = clienteRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(clienteMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public ClienteDTO recuperarPorId(Long id) {
        var entity = clienteRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return clienteMapper.toDto(entity);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void synchronizationClients() {
        var clients = keycloakClient.getClients().stream()
            .filter(client -> client.getAttributes().containsKey("acl.client.managed") && client.getAttributes().get("acl.client.managed").equals("true")).toList();
        for (ClientRepresentationDTO client : clients) {
            var entity = clienteRepository.findByClientId(client.getClientId()).orElseGet(() -> {
                var newEntity = new ClienteEntity();
                newEntity.setClientId(client.getClientId());
                newEntity.setDescricao(client.getDescription());
                newEntity.setClientUUID(client.getId());
                return clienteRepository.save(newEntity);
            });
            entity.setDescricao(client.getDescription());
            entity.setClientUUID(client.getId());
            entity.setClientId(client.getClientId());
            clienteRepository.save(entity);
        }
    }
}
