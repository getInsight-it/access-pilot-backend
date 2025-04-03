package it.getinsight.module.request.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.BusinessException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.client.entity.ClientStatus;
import it.getinsight.module.configuration.dto.ConfigurationValue;
import it.getinsight.module.email.dto.EmailDTO;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.keycloak.dto.RoleRepresentationDTO;
import it.getinsight.module.level.client.FeignClientFactory;
import it.getinsight.module.level.entity.LevelType;
import it.getinsight.module.level.repository.ItemRepository;
import it.getinsight.module.notification.enums.NotificationType;
import it.getinsight.module.notification.service.NotificationService;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.mapper.RequestFilterMapper;
import it.getinsight.module.request.mapper.RequestMapper;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.repository.specification.RequestEntitySpecificationFilter;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.request.util.ProtocolUtil;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.repository.specification.RoleSpecification;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.storage.service.StorageFileService;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.UserService;
import it.getinsight.module.web_notification.dto.WebNotificationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final NotificationService notificationService;
    private final StorageFileService storageFileService;
    private final FeignClientFactory feignClientFactory;
    private final ObjectMapper objectMapper;

    private static final String NAME_QUERY_FIND_ALL_REQUESTS_IN_ROLES = "find-all-requests-in-roles";
    private static final String NAME_QUERY_FIND_ALL_REQUESTS = "find-all-requests";
    public static final String PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET = "private-getinsight-accesspilot-docs";
    private final UserMapper userMapper;
    private final RequestVariableService requestVariableService;
    private final EmailNotificationProperties emailNotificationProperties;
    private final ItemRepository itemRepository;

    @Transactional(propagation = Propagation.REQUIRED)
    public RequestDTO createRequest(final RequestDTO requestDTO, List<MultipartFile> attachments) {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var roleEntity = roleRepository.findById(requestDTO.role().id())
                .orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        validateItemExistence(requestDTO.codeItem(), roleEntity);
        validateRequest(requestDTO, attachments);
        var user = findOrCreateUser(principal);
        var entity = requestMapper.toEntity(requestDTO);
        entity.setRequestingUser(user);
        entity.setRole(roleEntity);
        entity.setCodeItem(requestDTO.codeItem());
        entity.setStatus(RequestStatus.CREATED);
        entity.setProtocolCode(ProtocolUtil.generateUniqueProtocolCode());

        requestRepository.save(entity);
        storageFileService.save(attachments, PRIVATE_GETINSIGHT_ACCESSPILOT_DOCS_BUCKET, false, false, entity.getUuid());

        sendNotifications(entity);

        return requestMapper.toDto(entity);
    }

    private void sendNotifications(RequestEntity requestEntity) {
        var roleEntity = requestEntity.getRole().getRole();
        var approves = keycloakClient.getUsersByClientUUIDAndRoleName(roleEntity.getClient().getClientUUID(), roleEntity.getName())
            .stream()
            .map(user -> userService.findOrImportByExternalId(user.id()))
            .toList();

        if (approves.isEmpty()) {
            throw APPROVERS_NOT_FOUND_ERROR.businessException();
        }

        requestEntity.setStatus(RequestStatus.PENDING);
        approves.stream()
            .map(approvedDTO -> EmailDTO.builder()
                .to(approvedDTO.email())
                .userId(approvedDTO.id())
                .isOpened(false)
                .type(NotificationType.EMAIL)
                .subject(emailNotificationProperties.getApprover().getSubject())
                .templateName("request.html")
                .variables(requestVariableService.buildVariables(requestEntity.getRequestingUser(), userMapper.toEntity(approvedDTO), requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient()))
                .isHtml(true)
                .build())
            .forEach(o -> {
                requestRepository.save(requestEntity);
                log.info("Sending email to {}", o.to());
                notificationService.send(o);
                var variables = requestVariableService.buildVariables(requestEntity.getRequestingUser(), null, requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient());
                sendNotificationStatusToUser(requestEntity, variables);
            });
    }

    private UserEntity findOrCreateUser(Jwt principal) {
        String userId = principal.getSubject();
        return userRepository.findByExternalId(userId).orElseGet(() -> {
            UserEntity user = new UserEntity();
            user.setExternalId(userId);
            user.setFirstName(principal.getClaimAsString("name"));
            user.setLastName(principal.getClaimAsString("family_name"));
            user.setEmail(principal.getClaimAsString("email"));
            return userRepository.save(user);
        });
    }

    private void validateItemExistence(String codeItem,  RoleEntity roleEntity) {
        Optional.ofNullable(roleEntity.getLevel()).ifPresent(level -> {
            var levelType = level.getType();

            if (StringUtils.isBlank(codeItem)) {
                throw CODE_ITEM_NOT_FOUND_FOR_ROLE.businessException();
            }

            if (levelType == LevelType.BUILT_IN || levelType == LevelType.BUSINESS) {
                var entityFound = itemRepository.findById(Long.parseLong(codeItem))
                    .orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
                log.info("Item found for request: {}", entityFound);
            }

            if (levelType == LevelType.EXTERNAL) {
                var opItemDtoFound = feignClientFactory.createClient(level.getExternalUrl())
                    .getItemByExternalCode(level.getApiKey(), codeItem).orElseThrow(ITEM_NOT_FOUND_ERROR::businessException);
                log.info("Item found for request: {}", opItemDtoFound);
            }
        });
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
        boolean isRequestingUser = requestEntity.getRequestingUser().getExternalId().equals(principal.getSubject());
        if (!userExists && !isRequestingUser) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
        if (!ClientStatus.PUBLISHED.equals(requestEntity.getRole().getClient().getStatus())) {
            throw CLIENT_NOT_PUBLISHED_ERROR.businessException();
        }
        var approvingUserEntity = userRepository.findById(approvingUserDTO.id()).orElseThrow(USER_NOT_FOUND_ERROR::businessException);
        requestEntity.setStatus(RequestStatus.valueOf(status));
        requestEntity.setApprovingUser(approvingUserEntity);
        requestEntity.setFinalReason(requestUpdateDTO.finalReason());
        var variables = requestVariableService.buildVariables(requestEntity.getRequestingUser(), approvingUserEntity, requestEntity, requestEntity.getRole(), requestEntity.getRole().getClient());
        if (RequestStatus.APPROVED.equals(requestEntity.getStatus()) && userExists) {
            confirmRoles(requestEntity);
            sendNotificationStatusToUser(requestEntity, variables);
        } else if (List.of(RequestStatus.REJECTED, RequestStatus.CANCELED).contains(requestEntity.getStatus())) {
            sendNotificationStatusToUser(requestEntity, variables);
            requestRepository.save(requestEntity);
        }
    }


    public void confirmRoles(RequestEntity entity) {
        try {
            var roleEntity = roleRepository.findById(entity.getRole().getId()).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
            var role = keycloakClient.getRoleByNameAndClientUUID(roleEntity.getName(), roleEntity.getClient().getClientUUID());
            assignRoleToUser(entity, roleEntity, role);
            if (entity.getCodeItem() != null) {
                updateUserAttributes(entity, roleEntity, role);
            }
        } catch (Exception e) {
            handleAssignmentFailure(entity);
        }
    }


    private void assignRoleToUser(RequestEntity entity, RoleEntity roleEntity, RoleRepresentationDTO role) {
        keycloakClient.assignRoles(
            entity.getRequestingUser().getExternalId(),
            roleEntity.getClient().getClientUUID(),
            List.of(role)
        );
    }

    private void updateUserAttributes(RequestEntity entity, RoleEntity roleEntity, RoleRepresentationDTO role) {
        var user = keycloakClient.getUsers(entity.getRequestingUser().getExternalId());
        String levelAccess = buildLevelAccess(entity, roleEntity, role);

        var levelAttributes = new ArrayList<>(user.attributes().getOrDefault("levelAttributes", Collections.emptyList()));

        if (!levelAttributes.contains(levelAccess)) {
            levelAttributes.add(levelAccess);
            keycloakClient.updateUser(entity.getRequestingUser().getExternalId(), user.withLevelAttributes(levelAttributes));
        }
    }

    private String buildLevelAccess(RequestEntity entity, RoleEntity roleEntity, RoleRepresentationDTO role) {
        return String.join("|",
            roleEntity.getClient().getClientId(),
            role.name(),
            entity.getRole().getLevel().getName(),
            entity.getCodeItem()
        );
    }

    private void handleAssignmentFailure(RequestEntity entity) {
        entity.setStatus(RequestStatus.ROLES_NOT_ASSIGNED);
        requestRepository.save(entity);
    }

    public PageableResponseModel<RequestDTO> getAllRequestsMine(PageableRequestModel<RequestFilterDTO> configPage) {
        Optional<RequestFilterDTO> filter = configPage
            .getFilter();
        final var model = filter
            .map(requestFilterMapper::toDto)
            .map(requestMapper::toEntity)
            .orElse(new RequestEntity());
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> resourceAccess = principal.getClaim("resource_access");

        Specification<RequestEntity> spec = Specification.where(null);
        boolean executeQuery = false;

        if (filter.isPresent() && "created".equalsIgnoreCase(filter.get().type())) {
            model.setRequestingUser(UserEntity.builder().externalId(principal.getSubject()).build());
            executeQuery = true;
        }

        if (filter.isPresent() && "assigned".equalsIgnoreCase(filter.get().type())) {
            List<Long> rolesParentIds = roleRepository.findAll(RoleSpecification.byResourceAccess(resourceAccess)).stream().map(RoleEntity::getId).toList();
            spec.and(RequestSpecification.byRolesParent(rolesParentIds));
            executeQuery = !rolesParentIds.isEmpty();
        }

        if (!executeQuery)
            return PaginationHelper.toPageResponse(List.of(), 0L);

        spec = spec.and(RequestSpecification.matchCustom(model));

        Pageable pageable = PaginationHelper.toPageable(configPage);
        Page<RequestEntity> page = requestRepository.findAll(spec, pageable);

        return PaginationHelper.toPageResponse(requestMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<RequestDTO> getAllRequestsByRolesDynamicQuery(PageableRequestModel<String> configPage) {
        var roles = configPage.getFilter().filter(StringUtils::isNotBlank).orElse(null);
        if (StringUtils.isNotBlank(roles)) {
            DynamicParameters parameters = DynamicParameters.get().append("roles", List.of(roles.split(",")));
            return requestRepository.findAllNative(NAME_QUERY_FIND_ALL_REQUESTS_IN_ROLES, parameters, PaginationHelper.toPageable(configPage), requestMapper);
        } else {
            return requestRepository.findAllNative(NAME_QUERY_FIND_ALL_REQUESTS, DynamicParameters.get(), PaginationHelper.toPageable(configPage), requestMapper);
        }
    }


    public PageableResponseModel<RequestDTO> getAllRequests(PageableRequestModel<RequestFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(requestFilterMapper::toDto)
            .map(requestMapper::toEntity)
            .orElse(new RequestEntity());
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var filterDTO = configPage.getFilter().get();
        if (BooleanUtils.isTrue(filterDTO.onlyMine())) {
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

    public void sendNotificationStatusToUser(RequestEntity requestEntity, Map<String, Object> variables) {
        var requestingUserDTO = Optional.of(requestEntity.getRequestingUser()).map(userMapper::toDto).orElseThrow(() -> new BusinessException("Não foi possivel util o usuário"));
        notificationService.send(EmailDTO.builder()
            .to(requestingUserDTO.email())
            .subject(emailNotificationProperties.getStatusRequest().getSubject())
            .templateName("status-request.html")
            .userId(requestingUserDTO.id())
            .isOpened(false)
            .uuid(UUID.randomUUID().toString())
            .type(NotificationType.EMAIL)
            .variables(variables)
            .isHtml(true)
            .build());
        notificationService.send(
            WebNotificationDTO.builder()
                .userId(requestingUserDTO.id())
                .title("protocolo: " + requestEntity.getProtocolCode())
                .uuid(UUID.randomUUID().toString())
                .requestId(requestEntity.getId())
                .isOpened(false)
                .type(NotificationType.WEB)
                .priority(1L)
                .description(requestEntity.getDescription())
                .build());
    }

    public Long getTotalRequestsByStatus(RequestStatus status) {
        Example<RequestEntity> example = Example.of(RequestEntity.builder().status(status).build());
        return requestRepository.count(example);
    }

    public Long getTotalRequests(RequestStatus status, List<RoleEntity> roles) {
        var filter = RequestEntitySpecificationFilter.builder().status(status).roles(roles).build();
        return requestRepository.count(RequestSpecification.matchCustom(filter));
    }

    public Long getTotalUsers(RequestStatus status, List<RoleEntity> roles) {
        var filter = RequestEntitySpecificationFilter.builder().status(status).roles(roles).build();
        return requestRepository.countRequestingUserDistinct(RequestSpecification.matchCustom(filter));
    }

    public Long getTotalUsers(RequestStatus status) {
        var filter = RequestEntitySpecificationFilter.builder().status(status).build();
        return requestRepository.countRequestingUserDistinct(RequestSpecification.matchCustom(filter));
    }


    public RequestDTO findById(Long id) {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var approvingUserDTO = userService.findOrImportByExternalId(principal.getSubject());
        var requestEntity = requestRepository.findById(id).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        var roleEntityParent = roleRepository.findByRoleExternalId(requestEntity.getRole().getRoleExternalId()).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        var approvingUsersDTO = roleService.getOrImportApprovesByRoleId(roleEntityParent.getId());
        boolean userExists = approvingUsersDTO.stream().anyMatch(obj -> obj.id().equals(approvingUserDTO.id()));
        boolean isRequestingUser = requestEntity.getRequestingUser().getExternalId().equals(principal.getSubject());
        if (!isRequestingUser && !userExists) {
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
        return requestMapper.toDto(requestEntity);
    }

    public void validateRequest(final RequestDTO requestDTO, List<MultipartFile> attachments) {
        var requestEntity = requestMapper.toEntity(requestDTO);
        var roleEntity = roleRepository.findById(requestEntity.getRole().getId()).orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);

        var configurationOp = Optional.ofNullable(roleEntity.getClient().getConfiguration());

        if (configurationOp.isEmpty()) return;
        var configuration = objectMapper.convertValue(configurationOp.get().getValue(), ConfigurationValue.class);
        int total = attachments != null ? attachments.size() : 0;

        boolean belowMin = configuration.minQuantity() != null && total < configuration.minQuantity();
        boolean aboveMax = configuration.maxQuantity() != null && total > configuration.maxQuantity();

        if (belowMin || aboveMax) {
            throw ATTACHMENTS_QUANTITY_ERROR.businessException();
        }
    }

}
