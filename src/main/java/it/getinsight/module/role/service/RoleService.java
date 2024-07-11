package it.getinsight.module.role.service;


import it.getinsight.client.KeycloakClient;
import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.repository.ClienteRepository;
import it.getinsight.module.keycloak.dto.ClientRepresentationDTO;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.usuario.dto.UsuarioDTO;
import it.getinsight.module.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final ClienteRepository clienteRepository;
    private final RoleMapper roleMapper;
    private final KeycloakClient keycloakClient;
    private final UsuarioService usuarioService;

    private static final String NAME_QUERY_OBTER_TODOS_ROLES = "obter-todos-roles";

    public List<RoleDTO> getAllRolesDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return roleRepository.findAllNative(NAME_QUERY_OBTER_TODOS_ROLES, parameters, roleMapper);
    }

    public PageableResponseModel<RoleDTO> getAllRolesPageable(PageableRequestModel<RoleDTO> configPage) {
        final var model = new RoleEntity();

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("nome", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        final var page = roleRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(roleMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<RoleDTO> getAllRolesPageableByName(PageableRequestModel<String> configPage) {
        final var model = new RoleEntity();
        configPage.getFilter().ifPresent(model::setNome);

        final var matcher = ExampleMatcher
            .matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase();

        final var example = Example.of(model, matcher);

        final var page = roleRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(roleMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public RoleDTO recuperarPorId(Long id) {
        var entity = roleRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return roleMapper.toDto(entity);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void synchronizationRoles() {
        var clients = keycloakClient.getClients().stream()
            .filter(client -> client.getAttributes().containsKey("acl.client.managed") && client.getAttributes().get("acl.client.managed").equals("true")).toList();
        for (ClientRepresentationDTO client : clients) {
            var roles = keycloakClient.getRolesByClientUUID(client.getId());
            var clienteEntity = clienteRepository.findByClientId(client.getClientId()).orElseThrow(() -> new ResourceNotFoundException("Client não encontrado"));
            for (RoleRepresentationDTO role : roles) {
                    var entity = roleRepository.findByNome(role.getName()).orElseGet(() -> {
                    var newEntity = new RoleEntity();
                    newEntity.setNome(role.getName());
                    newEntity.setCliente(clienteEntity);
                    newEntity.setIdRoleExterno(role.getId());
                    newEntity.setDescricao(role.getDescription());
                    return roleRepository.save(newEntity);
                });
                entity.setDescricao(role.getDescription());
                entity.setCliente(clienteEntity);
                entity.setIdRoleExterno(role.getId());
                entity.setNome(role.getName());
                roleRepository.save(entity);
            }
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<UsuarioDTO> recuperarOuImportarAprovadoresPorIdRole(Long id) {
        var roleEntity = roleRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Role não encontrada"));
        var roleParent = Optional.ofNullable(roleEntity.getRole()).orElseThrow(() -> new ResourceNotFoundException("Role não tem role pai"));
        var role = keycloakClient.getRoleByNameAndClientUUID(roleParent.getNome(), roleEntity.getCliente().getClientUUID());
        return keycloakClient.getUsersByClientUUIDAndRoleName(roleEntity.getCliente().getClientUUID(), role.getName()).stream()
            .map(user ->
                usuarioService.recuperarOuImportarPorIdUsuarioExterno(user.getId())
            )
            .toList();
    }


}
