package it.getinsight.module.request.service;


import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.service.EmailService;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.mapper.RequestFilterMapper;
import it.getinsight.module.request.mapper.RequestMapper;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.storage.service.StorageFileService;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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
    private final RequestFilterMapper requestFilterMapper;
    private final KeycloakClient keycloakClient;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final UserService userService;
    private final EmailService emailService;
    private final StorageFileService storageFileService;

    private static final String NAME_QUERY_FIND_ALL_REQUESTS_ME = "find-all-requests-children";
    private static final String NAME_QUERY_FIND_ALL_REQUESTS_IN_ROLES = "find-all-requests-in-roles";
    private static final String NAME_QUERY_FIND_ALL_REQUESTS = "find-all-requests";
    public static final String  PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET = "private-getinsight-accesspilot-docs";
    private final UserMapper userMapper;
    private final RoleMapper roleMapper;
    private final ClientMapper clientMapper;
    private final EmailNotificationProperties emailNotificationProperties;

    @Transactional(propagation = Propagation.REQUIRED)
    public RequestDTO createRequest(final RequestDTO requestDTO, List<MultipartFile> attachments) {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getSubject();
        String username = principal.getClaimAsString("preferred_username");
        var roleEntity = roleRepository.findById(requestDTO.role().id()).orElseThrow(ResourceNotFoundException::new);
        final var entity = requestMapper.toEntity(requestDTO);
        var user = userRepository.findByExternalId(userId).orElseGet(() -> {
            var userEntity = new UserEntity();
            userEntity.setExternalId(userId);
            userEntity.setFirstName(principal.getClaimAsString("name"));
            userEntity.setLastName(principal.getClaimAsString("family_name"));
            userEntity.setEmail(principal.getClaimAsString("email"));
            userEntity.setUsername(username);
            return userRepository.save(userEntity);
        });
        entity.setRequestingUser(user);
        entity.setRole(roleEntity);
        sendApproves(entity);
        requestRepository.save(entity);
        storageFileService.save(attachments, PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET, false, false, entity.getId());
        return requestMapper.toDto(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
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
        requestEntity.setStatus(RequestStatus.valueOf(status));
        requestEntity.setApprovingUser(approvingUserEntity);
        var variables = getVariables(requestEntity.getRequestingUser(), approvingUserEntity, requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient());
        if (RequestStatus.APPROVED.equals(requestEntity.getStatus())) {
            confirmRoles(requestEntity);
            sendNotificationToUser(requestEntity, variables);
        }else if (RequestStatus.REJECTED.equals(requestEntity.getStatus())) {
            sendNotificationToUser(requestEntity, variables);
            requestRepository.save(requestEntity);
        }
    }


    private void confirmRoles(RequestEntity entity) {
        var roleEntity = roleRepository.findById(entity.getRole().getId()).orElseThrow(ResourceNotFoundException::new);
        var role = keycloakClient.getRoleByNameAndClientUUID(roleEntity.getName(), roleEntity.getClient().getClientUUID());
        try {
            keycloakClient.assignRoles(entity.getRequestingUser().getExternalId(), roleEntity.getClient().getClientUUID(), List.of(role));
        } catch (Exception e) {
            entity.setStatus(RequestStatus.ROLES_NOT_ASSIGNED);
            requestRepository.save(entity);
        }
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

    public PageableResponseModel<RequestDTO> getAllRequestsByRolesDynamicQuery(PageableRequestModel<String> configPage) {
        var roles = configPage.getFilter().filter(StringUtils::isNotBlank).orElse(null);
        if (StringUtils.isNotBlank(roles)) {
            DynamicParameters parameters = DynamicParameters.get().append("roles", List.of(roles.split(",")));
            return requestRepository.findAllNative(NAME_QUERY_FIND_ALL_REQUESTS_IN_ROLES, parameters,PaginationHelper.toPageable(configPage), requestMapper);
        }else {
            return requestRepository.findAllNative(NAME_QUERY_FIND_ALL_REQUESTS, DynamicParameters.get(),PaginationHelper.toPageable(configPage), requestMapper);
        }
    }


    public PageableResponseModel<RequestDTO> getAllRequests(PageableRequestModel<RequestFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(requestFilterMapper::toDto)
            .map(requestMapper::toEntity)
            .orElse(new RequestEntity());


        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("role.name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("role.client.name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("role.client.clientId", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("status", ExampleMatcher.GenericPropertyMatcher::exact)
            .withMatcher("managed", ExampleMatcher.GenericPropertyMatcher::exact)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);
        final var page = requestRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(requestMapper.toDto(page.getContent()), page.getTotalElements());
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



    private void sendApproves(RequestEntity requestEntity) {
        requestEntity.setStatus(RequestStatus.CREATED);
        var roleEntity = Optional.ofNullable(requestEntity.getRole().getRole()).orElseThrow(() -> new BusinessException("Role parent não encontrada"));
        var approvals = keycloakClient.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), roleEntity.getName()).stream()
            .map(user ->
                userService.findOrImportByExternalId(user.id())
            ).toList();
        approvals.stream()
            .map(approvedDTO -> EmailDTO.builder()
                .to(approvedDTO.email())
                .userId(approvedDTO.id())
                .subject(emailNotificationProperties.getApprover().getSubject())
                .templateName("request.html")
                .variables(getVariables(requestEntity.getRequestingUser(),userMapper.toEntity(approvedDTO), requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient()))
                .isHtml(true)
                .build())
            .forEach(o -> {
                emailService.sendMail(o);
                requestEntity.setStatus(RequestStatus.PENDING);
                var variables = getVariables(requestEntity.getRequestingUser(),null, requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient());
                sendNotificationToUser(requestEntity, variables);
                requestRepository.save(requestEntity);
            });
    }

    private Map<String, Object> getVariables(UserEntity requestingUserEntity, UserEntity approvingUserEntity, RequestEntity requestEntity, RoleEntity roleEntity, ClientEntity clientEntity) {
        Map<String, Object> variables = new HashMap<>();
        var url = emailNotificationProperties.getUrl();
        variables.put("link", Map.of("address", url.getClientUrl(), "hint", url.getHint()));
        variables.put("approvingUser", userMapper.toDto(approvingUserEntity));
        variables.put("requestingUser", userMapper.toDto(requestingUserEntity));
        variables.put("request", requestMapper.toDto(requestEntity));
        variables.put("status", requestMapper.toDto(requestEntity).status().name());
        variables.put("role", roleMapper.toDto(roleEntity));
        variables.put("client", clientMapper.toDto(clientEntity));
        return variables;
    }

    public void sendNotificationToUser(RequestEntity requestEntity, Map<String, Object> variables) {
        var requestingUserDTO = Optional.of(requestEntity.getRequestingUser()).map(userMapper::toDto).orElseThrow(() -> new BusinessException("Não foi possivel converter o usuário"));
        emailService.sendMail(EmailDTO.builder()
            .to(requestingUserDTO.email())
            .subject(emailNotificationProperties.getStatusRequest().getSubject())
            .templateName("status-request.html")
            .userId(requestingUserDTO.id())
            .variables(variables)
            .isHtml(true)
            .build());
    }

    public Long getTotalRequestsByStatus(RequestStatus status) {
        Example<RequestEntity> example = Example.of(RequestEntity.builder().status(status).build());
        return requestRepository.count(example);
    }
}
