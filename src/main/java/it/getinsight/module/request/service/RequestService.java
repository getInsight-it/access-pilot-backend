package it.getinsight.module.request.service;

import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
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
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.service.AuthenticationContextService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
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
    private final AuthenticationContextService authenticationContextService;

    private final RequestCreationService requestCreationService;
    private final RequestApprovalService requestApprovalService;
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
        log.debug("Delegating request approval to RequestApprovalService for ID: {}", id);
        requestApprovalService.processRequestUpdate(id, requestUpdateDTO);
    }



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
        var currentUserId = authenticationContextService.getCurrentUserId();
        var filterDTO = configPage.getFilter().get();
        if (BooleanUtils.isTrue(filterDTO.onlyMine())) {
            model.setRequestingUser(UserEntity.builder().externalId(currentUserId).build());
        }

        final var matcher = ExampleMatcher
            .matchingAny()
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
        var requestEntity = requestRepository.findById(id).orElseThrow(REQUEST_NOT_FOUND_ERROR::businessException);
        
        requestValidationService.validateUserAccessToRequest(id, requestEntity);
        
        return requestMapper.toDto(requestEntity);
    }

}
