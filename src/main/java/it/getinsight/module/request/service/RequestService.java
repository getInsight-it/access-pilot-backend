package it.getinsight.module.request.service;


import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.email.service.EmailService;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.mapper.RequestFilterMapper;
import it.getinsight.module.request.mapper.RequestMapper;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.request.util.ProtocolUtil;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.repository.specification.RoleSpecification;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.storage.service.StorageFileService;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
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
        entity.setProtocolCode(ProtocolUtil.generateUniqueProtocolCode());
        sendApproves(entity);
        requestRepository.save(entity);
        storageFileService.save(attachments, PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET, false, false, entity.getUuid());
        return requestMapper.toDto(entity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void publishRequestUpdateEvent(Long id, RequestUpdateDTO requestUpdateDTO) {
        final var status = requestUpdateDTO.status();
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var approvingUserDTO = userService.findOrImportByExternalId(principal.getSubject());
        var requestEntity = requestRepository.findById(id).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        var roleEntityParent = roleRepository.findByRoleExternalId(requestEntity.getRole().getRoleExternalId()).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        var approvingUsersDTO = roleService.getOrImportApprovesByRoleId(roleEntityParent.getId());
        boolean userExists = approvingUsersDTO.stream().anyMatch(obj -> obj.id().equals(approvingUserDTO.id()));
        if (!userExists) {
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
        if (!ClientStatus.PUBLISHED.equals(requestEntity.getRole().getClient().getStatus())) {
            throw CLIENT_NOT_PUBLISHED_ERROR.businessException();
        }
        var approvingUserEntity = userRepository.findById(approvingUserDTO.id()).orElseThrow(USER_NOT_FOUND_ERROR::businessException);
        requestEntity.setStatus(RequestStatus.valueOf(status));
        requestEntity.setApprovingUser(approvingUserEntity);
        var variables = getVariables(requestEntity.getRequestingUser(), approvingUserEntity, requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient());
        if ( RequestStatus.APPROVED.equals(requestEntity.getStatus())) {
            confirmRoles(requestEntity);
            sendNotificationStatusToUser(requestEntity, variables);
        }else if (RequestStatus.REJECTED.equals(requestEntity.getStatus())) {
            sendNotificationStatusToUser(requestEntity, variables);
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

    public PageableResponseModel<RequestDTO> getAllRequestsMine(PageableRequestModel<String> configPage) {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> resourceAccess = principal.getClaim("resource_access");
        List<Long> rolesParentIds = roleRepository.findAll(RoleSpecification.byResourceAccess(resourceAccess)).stream().map(RoleEntity::getId).toList();

        Specification<RequestEntity> spec = Specification.where(null);
        spec = spec.and(RequestSpecification.byRolesParent(rolesParentIds));
        if (rolesParentIds.isEmpty()){
            return PageableResponseModel.of(0L, Collections.emptyList());
        }

//        var filter = configPage.getFilter();
//        if (filter.isPresent()) {
//            RequestFilterDTO filterDTO = filter.get();
//            if (filterDTO.status() != null) {
//                spec = spec.and((root, query, builder) -> builder.equal(root.get("status"), filterDTO.status()));
//            }
//            if (filterDTO.description() != null) {
//                spec = spec.and((root, query, builder) -> builder.like(root.get("description"), "%" + filterDTO.description() + "%"));
//            }
//        }

        Pageable pageable = PaginationHelper.toPageable(configPage);
        Page<RequestEntity> page = requestRepository.findAll(spec, pageable);

        return PaginationHelper.toPageResponse(requestMapper.toDto(page.getContent()), page.getTotalElements());
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
        var filterDTO = configPage.getFilter().get();
        if (BooleanUtils.isTrue(filterDTO.onlyMine())){
            Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            model.setRequestingUser(UserEntity.builder().externalId(principal.getSubject()).build());
        }

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("role.name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("role.client.name", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("role.client.clientId", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("requestingUser.id", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("requestingUser.externalId", ExampleMatcher.GenericPropertyMatcher::contains)
            .withMatcher("status", ExampleMatcher.GenericPropertyMatcher::exact)
            .withMatcher("managed", ExampleMatcher.GenericPropertyMatcher::exact)
            .withMatcher("description", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);
        final var page = requestRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(requestMapper.toDto(page.getContent()), page.getTotalElements());
    }



    private void sendApproves(RequestEntity requestEntity) {
        requestEntity.setStatus(RequestStatus.CREATED);
        var roleEntity = Optional.ofNullable(requestEntity.getRole().getRole()).orElseThrow(() -> new BusinessException("Role parent não encontrada"));
        var approvals = keycloakClient.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), roleEntity.getName()).stream()
            .map(user ->
                userService.findOrImportByExternalId(user.id())
            ).toList();
        if (approvals.isEmpty()) {
            throw APPROVERS_NOT_FOUND_ERROR.businessException();
        }
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
                log.info("Sending email to {}", o.to());
                emailService.sendMail(o);
                requestEntity.setStatus(RequestStatus.PENDING);
                var variables = getVariables(requestEntity.getRequestingUser(),null, requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient());
                sendNotificationStatusToUser(requestEntity, variables);
                requestRepository.save(requestEntity);
            });
    }

    private Map<String, Object> getVariables(UserEntity requestingUserEntity, UserEntity approvingUserEntity, RequestEntity requestEntity, RoleEntity roleEntity, ClientEntity clientEntity) {
        Map<String, Object> variables = new HashMap<>();
        var url = emailNotificationProperties.getUrl();
        variables.put("link", Map.of("address", url.getClientUrl(), "hint", url.getHint(), "frontendUrl", url.getFrontendUrl()));
        variables.put("approvingUser", userMapper.toDto(approvingUserEntity));
        variables.put("requestingUser", userMapper.toDto(requestingUserEntity));
        variables.put("request", requestMapper.toDto(requestEntity));
        variables.put("status", requestMapper.toDto(requestEntity).status().name());
        variables.put("role", roleMapper.toDto(roleEntity));
        variables.put("client", clientMapper.toDto(clientEntity));
        return variables;
    }

    public void sendNotificationStatusToUser(RequestEntity requestEntity, Map<String, Object> variables) {
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
