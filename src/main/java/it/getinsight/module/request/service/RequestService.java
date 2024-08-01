package it.getinsight.module.request.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.camunda.zeebe.client.ZeebeClient;
import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.service.EmailService;
import it.getinsight.module.erro.service.ErrorService;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.mapper.RequestMapper;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static it.getinsight.message.MessageProperty.APPROVE_NOT_AUTHORIZED;
import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;


@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final ZeebeClient client;
    private final KeycloakClient keycloakClient;
    private final ErrorService errorService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final EmailService emailService;

    private static final String NAME_QUERY_FIND_ALL_REQUESTS_ME = "find-all-requests-children";
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final ClientMapper clientMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public RequestDTO createRequest(final RequestDTO dto) {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getSubject();
        String username = principal.getClaimAsString("preferred_username");
        var roleEntity = roleRepository.findByRoleExternalId(dto.roleId()).orElseThrow(ResourceNotFoundException::new);
        final var entity = requestMapper.toEntity(dto);
        var user = userRepository.findByExternalId(userId).orElseGet(() -> {
            var userEntity = new UserEntity();
            userEntity.setExternalId(userId);
            userEntity.setFirstName(principal.getClaimAsString("name"));
            userEntity.setLastName(principal.getClaimAsString("family_name"));
            userEntity.setUsername(username);
            return userRepository.save(userEntity);
        });
        entity.setRequestingUser(user);
        entity.setStatus(RequestStatus.CREATED);
        entity.setRole(roleEntity);
        requestRepository.save(entity);

        var requestSavedDTO = new RequestDTO(entity.getId(), entity.getStatus(), user.getId().toString(), entity.getRole().getId().toString());
        client.newCreateInstanceCommand()
            .bpmnProcessId("request")
            .latestVersion()
            .variables(new ObjectMapper().convertValue(requestSavedDTO, new TypeReference<Map<String, Object>>() {
            }))
            .send().join();
        return requestSavedDTO;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateRequest(Long id, String status) {
        requestRepository.findById(id).ifPresentOrElse(onbordingEntity -> {
            onbordingEntity.setStatus(RequestStatus.valueOf(status));
            requestRepository.save(onbordingEntity);
        }, errorService::failWithResourceNotFoundException);
    }

    public RequestEntity save(RequestDTO requestDTO) {
        var entity = requestMapper.toEntity(requestDTO);
        requestRepository.save(entity);
        return entity;
    }

    public void publishRequestUpdateEvent(Long id, String status) {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var approvingUserDTO = userService.findOrImportByExternalId(principal.getSubject());
        var requestEntity = requestRepository.findById(id).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        var roleEntityParent = roleRepository.findByRoleExternalId(requestEntity.getRole().getRoleExternalId()).orElseThrow(() -> new ResourceNotFoundException("Role não encontrada"));
        var approvingUsersDTO = roleService.getOrImportApprovesByRoleId(roleEntityParent.getId());
        boolean userExists = approvingUsersDTO.stream().anyMatch(obj -> obj.id().equals(approvingUserDTO.id()));
        if (!userExists) {
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
        var approvingUserEntity = userRepository.findById(approvingUserDTO.id()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        publishUpdateRequestEvent(id, status, approvingUserDTO.id());
        requestEntity.setStatus(RequestStatus.valueOf(status));
        requestEntity.setApprovingUser(approvingUserEntity);
        requestRepository.save(requestEntity);
    }

    private void publishUpdateRequestEvent(Long id, String status, Long approverId) {
        client.newPublishMessageCommand()
            .messageName("updateRequestEvent")
            .correlationKey(String.valueOf(id))
            .variables(Map.of("status", RequestStatus.valueOf(status).name(), "approvingUserId", approverId))
            .send().join();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void confirmRoles(Long id) {
        var entity = requestRepository.findById(id).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        var roleEntity = roleRepository.findById(entity.getRole().getId()).orElseThrow(ResourceNotFoundException::new);
        var role = keycloakClient.getRoleByNameAndClientUUID(roleEntity.getName(), roleEntity.getClient().getClientUUID());
        Optional.of(role).ifPresentOrElse(obj ->
            {
                keycloakClient.assignRoles(entity.getRequestingUser().getExternalId(), roleEntity.getClient().getClientUUID(), List.of(obj));
                entity.setStatus(RequestStatus.ROLES_ASSIGNED);
                requestRepository.save(entity);
            }
            , () -> {
                entity.setStatus(RequestStatus.ROLES_NOT_FOUND);
                requestRepository.save(entity);
            });
    }

    public PageableResponseModel<RequestDTO> getAllRequestsByStatusDynamicQuery(PageableRequestModel<String> configPage) {
        var model = new RequestEntity();
        configPage.getFilter().filter(StringUtils::isNotBlank).ifPresent(o -> model.setStatus(RequestStatus.valueOf(o)));
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Object> resourceAccess = principal.getClaim("resource_access");
        Set<String> roles = extractRoles(resourceAccess);

        final var parameters = DynamicParameters.get()
            .append("status", model.getStatus().name())
            .append("rolesParent", roles);
        return requestRepository.findAllNative(NAME_QUERY_FIND_ALL_REQUESTS_ME, parameters,PaginationHelper.toPageable(configPage), requestMapper);
    }

    private Set<String> extractRoles(Map<String, Object> resourceAccess) {
        return resourceAccess.values().stream()
            .flatMap(this::extractRolesFromClientAccess)
            .collect(Collectors.toSet());
    }

    private Stream<String> extractRolesFromClientAccess(Object clientAccess) {
        Map<String, Object> clientAccessMap = (Map<String, Object>) clientAccess;
        List<String> roles = (List<String>) clientAccessMap.get("roles");
        return roles.stream();
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void sendApproves(Long requestId) {
        var requestEntity = requestRepository.findById(requestId).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        var requestDTO = requestMapper.toDto(requestEntity);
        var roleEntity = roleRepository.findByRoleExternalId(requestEntity.getRole().getRoleExternalId()).orElseThrow(() -> new ResourceNotFoundException("Role não encontrada"));
        var roleDTO = roleMapper.toDto(roleEntity);
        var clientDTO = clientMapper.toDto(roleEntity.getClient());
        var roleParent = Optional.ofNullable(roleEntity.getRole()).orElseThrow(() -> new ResourceNotFoundException("Role não tem role pai"));
        var role = keycloakClient.getRoleByNameAndClientUUID(roleParent.getName(), roleEntity.getClient().getClientUUID());
        var requestedDTO = Optional.of(requestEntity.getRequestingUser()).map(userMapper::toDto).orElseThrow(() -> new BusinessException("Não foi possivel converter o usuário"));
        var approvals = keycloakClient.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), role.name()).stream()
            .map(user ->
                userService.findOrImportByExternalId(user.id())
            ).toList();
        approvals.stream()
            .map(approvedDTO -> EmailDTO.builder()
                .to(approvedDTO.email())
                .subject("Aprovação de solicitação")
                .templateName("request.html")
                .variables(getVariables(requestedDTO,approvedDTO,requestDTO, roleDTO, clientDTO))
                .isHtml(true)
                .build())
            .forEach(emailService::sendMail);
        requestEntity.setStatus(RequestStatus.APPROVES_SENT);
        requestRepository.save(requestEntity);
    }

    private Map<String, Object> getVariables(UserDTO requestingUserDTO, UserDTO approvingUserDTO, RequestDTO requestDTO, RoleDTO roleDTO, ClientDTO clientDTO) {
        Map<String, Object> variables = new HashMap<>();
        variables.put("link", Map.of("address", "http://localhost:8080", "hint", "Accesspilot frontend"));
        variables.put("approvingUser", approvingUserDTO);
        variables.put("requestingUser", requestingUserDTO);
        variables.put("request", requestDTO);
        variables.put("status", requestDTO.status().name());
        variables.put("role", roleDTO);
        variables.put("client", clientDTO);
        return variables;
    }

    public void sendNotificationToUser(Long id) {
        var requestEntity = requestRepository.findById(id).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        var requestDTO = requestMapper.toDto(requestEntity);
        var roleDTO = roleMapper.toDto(requestEntity.getRole());
        var clientDTO = clientMapper.toDto(requestEntity.getRole().getClient());
        var requestingUserDTO = Optional.of(requestEntity.getRequestingUser()).map(userMapper::toDto).orElseThrow(() -> new BusinessException("Não foi possivel converter o usuário"));
        var approvingUserDTO = Optional.of(requestEntity.getApprovingUser()).map(userMapper::toDto).orElseThrow(() -> new BusinessException("Não foi possivel converter o usuário"));
        var variables = getVariables(approvingUserDTO, requestingUserDTO, requestDTO, roleDTO, clientDTO);
        emailService.sendMail(EmailDTO.builder()
            .to(requestingUserDTO.email())
            .subject("Status da solicitação de perfil")
            .templateName("status-request.html")
            .variables(variables)
            .isHtml(true)
            .build());
    }
}
