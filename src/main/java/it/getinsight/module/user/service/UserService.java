package it.getinsight.module.user.service;

import it.getinsight.core.dynamicquery.parameters.DynamicParameters;
import it.getinsight.core.exception.ResourceNotFoundException;
import it.getinsight.core.helper.PaginationHelper;
import it.getinsight.core.pagination.PageableRequestModel;
import it.getinsight.core.pagination.PageableResponseModel;
import it.getinsight.module.keycloak.client.KeycloakClient;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.repository.specification.RoleSpecification;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.mapper.UserMapper;
import it.getinsight.module.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final KeycloakClient keycloakClient;
    private final UserMapper userMapper;

    @Value("${jwt.auth.converter.resource-id}")
    private String clientId;

    private static final String NAME_QUERY_FIND_ALL_USERS = "find-all-users";

    public List<UserDTO> getAllUsersDynamicQuery() {
        final var parameters = DynamicParameters.get();
        return userRepository.findAllNative(NAME_QUERY_FIND_ALL_USERS, parameters, userMapper);
    }

    public PageableResponseModel<UserDTO> getAllUsersPageable(PageableRequestModel<UserDTO> configPage) {
        final var model = configPage
            .getFilter()
            .map(userMapper::toEntity)
            .orElse(new UserEntity());

        final var matcher = ExampleMatcher
            .matchingAll()
            .withIgnoreNullValues()
            .withMatcher("firstName", ExampleMatcher.GenericPropertyMatcher::contains);

        final var example = Example.of(model, matcher);

        final var page = userRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(userMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public PageableResponseModel<UserDTO> getAllUsersPageableByName(PageableRequestModel<String> configPage) {
        final var model = new UserEntity();
        configPage.getFilter().ifPresent(model::setFirstName);

        final var matcher = ExampleMatcher
            .matching()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
            .withIgnoreNullValues()
            .withIgnoreCase();

        final var example = Example.of(model, matcher);

        final var page = userRepository.findAll(example, PaginationHelper.toPageable(configPage));
        return PaginationHelper.toPageResponse(userMapper.toDto(page.getContent()), page.getTotalElements());
    }

    public UserDTO findById(Long id) {
        var entity = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return userMapper.toDto(entity);
    }


    public UserDTO findOrImportByExternalId(String externalId) {
        var entity = userRepository.findByExternalId(externalId).orElseGet(() -> {
            final var userDTO = keycloakClient.getUsers(externalId);
            final var userEntity = new UserEntity(null, userDTO.username(), userDTO.firstName(), userDTO.lastName(), userDTO.email(), userDTO.id());
            log.info("User imported from Keycloak: {}", userEntity);
            return userRepository.save(userEntity);
        });
        return userMapper.toDto(entity);
    }


    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public UserDTO addUser(UserDTO userDTO) {
        var entity = userMapper.toEntity(userDTO);
        entity.setId(null);
        return userMapper.toDto(userRepository.save(entity));
    }

    public Optional<UserEntity> getUserLogged() {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = principal.getSubject();
        return userRepository.findById(Long.valueOf(userId));
    }

//    @Cacheable(value = "checkExternalId", key = "#externalId")
    public boolean checkExternalId(String externalId) {
        Jwt principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userExternalId = principal.getSubject();
        return StringUtils.isNotBlank(externalId) && externalId.equals(userExternalId);
    }


    public UserDTO getMe() {
        var principal = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, List<String>> resourceAccess = principal.getClaim("resource_access");
        var roles = roleRepository.findAll(RoleSpecification.byResourceAccess(resourceAccess)).stream().toList();
        var rolesChildren = roleRepository.findAllByRoleIn(roles);
        var userDTO = findOrImportByExternalId(principal.getSubject());
        return UserDTO.builder().isApprover(!rolesChildren.isEmpty())
            .username(userDTO.username())
            .firstName(userDTO.firstName())
            .lastName(userDTO.lastName())
            .email(userDTO.email())
            .externalId(userDTO.externalId()).build();

    }

    public boolean isUserLoggedAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getAuthorities() == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
            .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

}
