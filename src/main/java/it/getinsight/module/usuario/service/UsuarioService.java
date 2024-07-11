package it.getinsight.module.usuario.service;

import it.getinsight.client.KeycloakClient;
import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.usuario.dto.UsuarioDTO;
import it.getinsight.module.usuario.entity.UsuarioEntity;
import it.getinsight.module.usuario.mapper.UsuarioMapper;
import it.getinsight.module.usuario.repository.UsuarioRepository;
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
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final KeycloakClient keycloakClient;
    private final UsuarioMapper usuarioMapper;

    private static final String NAME_QUERY_OBTER_TODOS_USUARIOS = "obter-todos-usuarios";

    public List<UsuarioDTO> getAllUsersDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return usuarioRepository.findAllNative(NAME_QUERY_OBTER_TODOS_USUARIOS, parameters, usuarioMapper);
    }

    public PageableResponseModel<UsuarioDTO> getAllUsersPageable(PageableRequestModel<UsuarioDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(usuarioMapper::toEntity)
            .orElse(new UsuarioEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("nome", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        final var page = usuarioRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(usuarioMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<UsuarioDTO> getAllUsersPageableByName(PageableRequestModel<String> configPage) {
        final var model = new UsuarioEntity();
        configPage.getFilter().ifPresent(model::setNome);

        final var matcher = ExampleMatcher
            .matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase();

        final var example = Example.of(model, matcher);

        final var page = usuarioRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(usuarioMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public UsuarioDTO recuperarPorId(Long id) {
        var entity = usuarioRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return usuarioMapper.toDto(entity);
    }


    public UsuarioDTO recuperarOuImportarPorIdUsuarioExterno(String idUsuarioExterno) {
        var entity = usuarioRepository.findByIdUsuarioExterno(idUsuarioExterno).orElseGet(() -> {
            final var usuarioDTO = keycloakClient.getUsers(idUsuarioExterno);
            final var usuarioEntity = new UsuarioEntity(null, usuarioDTO.getUsername(), usuarioDTO.getFirstName(), usuarioDTO.getLastName(), usuarioDTO.getEmail(), usuarioDTO.getId());
            return usuarioRepository.save(usuarioEntity);
        });
        return usuarioMapper.toDto(entity);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public UsuarioDTO adicionarUsuario(UsuarioDTO usuarioDTO) {
        var entity = usuarioMapper.toEntity(usuarioDTO);
        entity.setId(null);
        return usuarioMapper.toDto(usuarioRepository.save(entity));
    }

}
