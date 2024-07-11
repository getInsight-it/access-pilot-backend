package it.getinsight.module.solicitacao.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import it.getinsight.client.KeycloakClient;
import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.service.EmailService;
import it.getinsight.module.erro.service.ErroService;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.solicitacao.dto.SolicitacaoDTO;
import it.getinsight.module.solicitacao.entity.SolicitacaoEntity;
import it.getinsight.module.solicitacao.enuns.SolicitacaoStatus;
import it.getinsight.module.solicitacao.mapper.SolicitacaoMapper;
import it.getinsight.module.solicitacao.repository.SolicitacaoRepository;
import it.getinsight.module.usuario.dto.UsuarioDTO;
import it.getinsight.module.usuario.entity.UsuarioEntity;
import it.getinsight.module.usuario.mapper.UsuarioMapperImpl;
import it.getinsight.module.usuario.repository.UsuarioRepository;
import it.getinsight.module.usuario.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.APROVADOR_NAO_AUTORIZADO;
import static it.getinsight.message.MessageProperty.SOLICITACAO_NAO_ENCOTRADA_ERRO;

@Service
@RequiredArgsConstructor
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final SolicitacaoMapper solicitacaoMapper;
    private final ZeebeClient client;
    private final KeycloakClient keycloakClient;
    private final ErroService erroService;
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final UsuarioService usuarioService;
    private final EmailService emailService;
    private final ObjectMapper mapper;

    private static final String NAME_QUERY_OBTER_TODOS_SOLICITACAO = "obter-todos-solicitacoes";
    private final UsuarioMapperImpl usuarioMapperImpl;

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public SolicitacaoDTO createSolicitacao(final SolicitacaoDTO dto) {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getSubject();
        String username = principal.getClaimAsString("preferred_username");
        var roleEntity = roleRepository.findByIdRoleExterno(dto.roleId()).orElseThrow(ResourceNotFoundException::new);
        final var entity = solicitacaoMapper.toEntity(dto);
        var usuario = usuarioRepository.findByIdUsuarioExterno(userId).orElseGet(() -> {
            var usuarioEntity = new UsuarioEntity();
            usuarioEntity.setIdUsuarioExterno(userId);
            usuarioEntity.setNome(principal.getClaimAsString("name"));
            usuarioEntity.setSobrenome(principal.getClaimAsString("family_name"));
            usuarioEntity.setUsername(username);
            return usuarioRepository.save(usuarioEntity);
        });
        entity.setUsuarioSolicitante(usuario);
        entity.setStatus(SolicitacaoStatus.CREATED);
        entity.setRole(roleEntity);
        solicitacaoRepository.save(entity);

        var solicitacaoSavedDTO = new SolicitacaoDTO(entity.getId(), entity.getStatus(), usuario.getId().toString(), entity.getRole().getId().toString());
        client.newCreateInstanceCommand()
            .bpmnProcessId("solicitacao")
            .latestVersion()
            .variables(new ObjectMapper().convertValue(solicitacaoSavedDTO, new TypeReference<Map<String, Object>>() {
            }))
            .send().join();
        return solicitacaoSavedDTO;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateSolicitacao(Long id, String status) {
        solicitacaoRepository.findById(id).ifPresentOrElse(onbordingEntity -> {
            onbordingEntity.setStatus(SolicitacaoStatus.valueOf(status));
            solicitacaoRepository.save(onbordingEntity);
        }, erroService::falharComResourceNotFoundException);
    }

    public SolicitacaoEntity save(SolicitacaoDTO solicitacaoDTO) {
        var entity = solicitacaoMapper.toEntity(solicitacaoDTO);
        solicitacaoRepository.save(entity);
        return entity;
    }

    public void publishSolicitacaoUpdateEvent(Long id, String status) {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var usuarioAprovadorDTO = usuarioService.recuperarOuImportarPorIdUsuarioExterno(principal.getSubject());
        var solicitacaoEntity = solicitacaoRepository.findById(id).orElseThrow(SOLICITACAO_NAO_ENCOTRADA_ERRO::businessException);
        var roleEntityParent = roleRepository.findByIdRoleExterno(solicitacaoEntity.getRole().getIdRoleExterno()).orElseThrow(() -> new ResourceNotFoundException("Role não encontrada"));
        var usuarios = roleService.recuperarOuImportarAprovadoresPorIdRole(roleEntityParent.getId());
        boolean userExists = usuarios.stream().anyMatch(obj -> obj.id().equals(usuarioAprovadorDTO.id()));
        if (!userExists) {
            throw APROVADOR_NAO_AUTORIZADO.accessForbiddenException();
        }
        var usuarioAprovadorEntity = usuarioRepository.findById(usuarioAprovadorDTO.id()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        publishUpdateSolicitacaoEvent(id, status, usuarioAprovadorDTO.id());
        solicitacaoEntity.setStatus(SolicitacaoStatus.valueOf(status));
        solicitacaoEntity.setUsuarioAprovador(usuarioAprovadorEntity);
        solicitacaoRepository.save(solicitacaoEntity);
    }

    private void publishUpdateSolicitacaoEvent(Long id, String status, Long approverId) {
        client.newPublishMessageCommand()
            .messageName("updateSolicitacaoEvent")
            .correlationKey(String.valueOf(id))
            .variables(Map.of("status", SolicitacaoStatus.valueOf(status).name(), "approvadorId", approverId))
            .send().join();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void confirmRoles(Long id) {
        var entity = solicitacaoRepository.findById(id).orElseThrow(SOLICITACAO_NAO_ENCOTRADA_ERRO::businessException);
        var roleEntity = roleRepository.findById(entity.getRole().getId()).orElseThrow(ResourceNotFoundException::new);
        var role = keycloakClient.getRoleByNameAndClientUUID(roleEntity.getNome(), roleEntity.getCliente().getClientUUID());
        Optional.of(role).ifPresentOrElse(obj ->
            {
                keycloakClient.assignRoles(entity.getUsuarioSolicitante().getIdUsuarioExterno(), roleEntity.getCliente().getClientUUID(), List.of(obj));
                entity.setStatus(SolicitacaoStatus.ROLES_ASSIGNED);
                solicitacaoRepository.save(entity);
            }
            , () -> {
                entity.setStatus(SolicitacaoStatus.ROLES_NOT_FOUND);
                solicitacaoRepository.save(entity);
            });
    }

    public List<SolicitacaoDTO> getAllConfigurationsDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return solicitacaoRepository.findAllNative(NAME_QUERY_OBTER_TODOS_SOLICITACAO, parameters, solicitacaoMapper);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void sendApproves(Long solicitacaoId) {
        var solicitacaoEntity = solicitacaoRepository.findById(solicitacaoId).orElseThrow(SOLICITACAO_NAO_ENCOTRADA_ERRO::businessException);
        var solicitacaoDTO = solicitacaoMapper.toDto(solicitacaoEntity);
        var roleEntity = roleRepository.findByIdRoleExterno(solicitacaoEntity.getRole().getIdRoleExterno()).orElseThrow(() -> new ResourceNotFoundException("Role não encontrada"));
        var roleParent = Optional.ofNullable(roleEntity.getRole()).orElseThrow(() -> new ResourceNotFoundException("Role não tem role pai"));
        var role = keycloakClient.getRoleByNameAndClientUUID(roleParent.getNome(), roleEntity.getCliente().getClientUUID());
        var solicitanteDTO = Optional.of(solicitacaoEntity.getUsuarioSolicitante()).map(usuarioMapperImpl::toDto).orElseThrow(() -> new BusinessException("Não foi possivel converter o usuário"));
        var aprovadores = keycloakClient.getUsersByClientUUIDAndRoleName(roleEntity.getCliente().getClientUUID(), role.getName()).stream()
            .map(user ->
                usuarioService.recuperarOuImportarPorIdUsuarioExterno(user.getId())
            ).toList();
        aprovadores.stream()
            .map(approvedDTO -> EmailDTO.builder()
                .to(approvedDTO.email())
                .subject("Aprovação de solicitação")
                .templateName("solicitacao.html")
                .isHtml(true)
                .build())
            .forEach(emailService::sendMail);
        solicitacaoEntity.setStatus(SolicitacaoStatus.APPROVES_SENT);
        solicitacaoRepository.save(solicitacaoEntity);
    }


}
