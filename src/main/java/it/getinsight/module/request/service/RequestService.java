package it.getinsight.module.request.service;

import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.dto.RequestFilterDTO;
import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestAction;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.mapper.RequestFilterMapper;
import it.getinsight.module.request.mapper.RequestMapper;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.request.repository.specification.RequestEntitySpecificationFilter;
import it.getinsight.module.request.repository.specification.RequestSpecification;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static it.getinsight.message.MessageProperty.REQUEST_NOT_FOUND_ERROR;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final RequestFilterMapper requestFilterMapper;
    private final AuthenticationContextService authenticationContextService;

    private final RequestCreationService requestCreationService;
    private final RequestManagementService requestManagementService;
    private final RequestValidationService requestValidationService;
    private final RequestQueryBuilderService requestQueryBuilderService;

    private static final String NAME_QUERY_FIND_ALL_REQUESTS_IN_ROLES = "find-all-requests-in-roles";
    private static final String NAME_QUERY_FIND_ALL_REQUESTS = "find-all-requests";

    @Transactional(propagation = Propagation.REQUIRED)
    public RequestDTO createRequest(final RequestDTO requestDTO, MultiValueMap<String, MultipartFile> attachments) {
        log.debug("Delegating request creation to RequestCreationService");
        return requestCreationService.createRequest(requestDTO, attachments);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void publishRequestUpdateEvent(Long id, RequestUpdateDTO requestUpdateDTO) {
        log.debug("Delegating request approval to RequestManagementService for ID: {}", id);
        requestManagementService.processRequestUpdate(id, requestUpdateDTO);
    }


    @Transactional(readOnly = true)
    public PageableResponseModel<RequestDTO> getAllRequestsMine(PageableRequestModel<RequestFilterDTO> configPage) {
        log.debug("Fetching requests with filters: {}", configPage.getFilter());

        Optional<RequestFilterDTO> filter = configPage.getFilter();
        final var model = filter
            .map(requestFilterMapper::toDto)
            .map(requestMapper::toEntity)
            .orElse(new RequestEntity());

        if (!requestQueryBuilderService.shouldExecuteQuery(filter)) {
            return PaginationHelper.toPageResponse(List.of(), 0L);
        }

        Specification<RequestEntity> spec = requestQueryBuilderService.buildFinalSpecification(model, filter);
        Pageable pageable = PaginationHelper.toPageable(configPage);
        Page<RequestEntity> page = requestRepository.findAll(spec, pageable);

        return PaginationHelper.toPageResponse(requestMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<RequestDTO> getAllRequestsByRolesDynamicQuery(PageableRequestModel<String> configPage) {
        var roles = configPage.getFilter().filter(StringUtils::isNotBlank).orElse(null);
        DynamicParameters parameters = DynamicParameters.get();
        if (StringUtils.isNotBlank(roles)) {
            parameters = parameters.append("roles", List.of(roles.split(",")));
        }

        var jwt = authenticationContextService.getCurrentJwt();
        var claim = jwt.getClaim("levelAttributes");
        java.util.List<String> raw = claim instanceof java.util.Collection<?> c ? c.stream().map(Object::toString).toList() : java.util.List.of();
        var clientIds = new java.util.ArrayList<Long>();
        var levelIds = new java.util.ArrayList<Long>();
        var itemIds  = new java.util.ArrayList<String>();
        for (String v : raw) {
            String[] parts = v.split(":");
            if (parts.length == 4) {
                try {
                    clientIds.add(Long.valueOf(parts[0]));
                    levelIds.add(Long.valueOf(parts[2]));
                    itemIds.add(parts[3]);
                } catch (NumberFormatException e) {
                    log.warn("Invalid scope: {}", e.getMessage());
                }
            }
        }
        if (!clientIds.isEmpty()) parameters = parameters.append("clientIds", clientIds);
        if (!levelIds.isEmpty())  parameters = parameters.append("levelIds", levelIds);
        if (!itemIds.isEmpty())   parameters = parameters.append("itemIds", itemIds);

        String queryName = StringUtils.isNotBlank(roles) ? NAME_QUERY_FIND_ALL_REQUESTS_IN_ROLES : NAME_QUERY_FIND_ALL_REQUESTS;
        return requestRepository.findAllNative(queryName, parameters, PaginationHelper.toPageable(configPage), requestMapper);
    }


    public PageableResponseModel<RequestDTO> getAllRequests(PageableRequestModel<RequestFilterDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(requestFilterMapper::toDto)
            .map(requestMapper::toEntity)
            .orElse(new RequestEntity());
        var currentUserId = authenticationContextService.getCurrentUserId();
        var filterDTO = configPage.getFilter().orElse(null);
        if (filterDTO != null && BooleanUtils.isTrue(filterDTO.onlyMine())) {
            model.setRequestingUser(UserEntity.builder().externalId(currentUserId).build());
        }

        Specification<RequestEntity> spec = requestQueryBuilderService.buildFinalSpecification(model, configPage.getFilter());
        final var page = requestRepository.findAll(spec, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(requestMapper.toDto(page.getContent()), page.getTotalElements());
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

    public Long getTotalAssignedRequests(RequestStatus status) {
        var model = RequestEntity.builder().status(status).build();
        Specification<RequestEntity> spec = requestQueryBuilderService.buildAssignedRequestsSpecification(model);
        return requestRepository.count(spec);
    }

    public Long getTotalAssignedUsers(RequestStatus status) {
        var model = RequestEntity.builder().status(status).build();
        Specification<RequestEntity> spec = requestQueryBuilderService.buildAssignedRequestsSpecification(model);
        return requestRepository.countRequestingUserDistinct(spec);
    }

    @Transactional(readOnly = true)
    public boolean hasAssignedRequestsForCurrentUser() {
        var model = new RequestEntity();
        Specification<RequestEntity> spec = requestQueryBuilderService.buildAssignedRequestsSpecification(model);
        return requestRepository.exists(spec);
    }

    public Long getTotalUsers(RequestStatus status) {
        var filter = RequestEntitySpecificationFilter.builder().status(status).build();
        return requestRepository.countRequestingUserDistinct(RequestSpecification.matchCustom(filter));
    }


    public RequestDTO findById(Long id) {
        var requestEntity = requestRepository.findById(id).orElseThrow(REQUEST_NOT_FOUND_ERROR::resourceNotFoundException);
        var isRequester = authenticationContextService.getCurrentUserId().equals(requestEntity.getRequestingUser().getExternalId());
        if (!isRequester) {
            requestValidationService.validateUserAccessToRequest(id, requestEntity);
        }
        return requestMapper.toDto(requestEntity);
    }

    public List<RequestAction> getAllowedActionsForRequest(Long requestId) {
        return requestManagementService.getAllowedActionsForRequest(requestId);
    }

}
