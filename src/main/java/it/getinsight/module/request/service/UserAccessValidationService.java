package it.getinsight.module.request.service;

import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.*;

/**
 * Serviço responsável exclusivamente por validações de acesso de usuários.
 * Aplica SRP de forma agressiva - apenas validações de permissão de acesso.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserAccessValidationService {

    private final RoleRepository roleRepository;
    private final UserService userService;
    private final RoleService roleService;
    private final AuthenticationContextService authenticationContextService;

    /**
     * Valida se o usuário tem permissão para acessar um request específico.
     */
    public void validateUserAccessToRequest(Long requestId, RequestEntity requestEntity) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);
        var roleEntityParent = roleRepository.findByRoleExternalId(requestEntity.getRole().getRoleExternalId())
            .orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        var approvingUsersDTO = roleService.getOrImportApprovesByRoleId(roleEntityParent.getId());
        
        boolean userExists = approvingUsersDTO.stream().anyMatch(obj -> obj.id().equals(approvingUserDTO.id()));
        boolean isRequestingUser = requestEntity.getRequestingUser().getExternalId().equals(currentUserId);
        
        if (!isRequestingUser && !userExists) {
            log.warn("Unauthorized access attempt to request {} by user {}", requestId, currentUserId);
            throw APPROVE_NOT_AUTHORIZED.accessForbiddenException();
        }
    }

    /**
     * Valida se o usuário tem permissão para atualizar um request.
     */
    public void validateUserPermissionToUpdateRequest(RequestEntity requestEntity) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);
        var roleEntityParent = roleRepository.findByRoleExternalId(requestEntity.getRole().getRoleExternalId())
            .orElseThrow(ROLE_NOT_FOUND_ERROR::businessException);
        var approvingUsersDTO = roleService.getOrImportApprovesByRoleId(roleEntityParent.getId());
        
        boolean userExists = approvingUsersDTO.stream().anyMatch(obj -> obj.id().equals(approvingUserDTO.id()));
        boolean isRequestingUser = requestEntity.getRequestingUser().getExternalId().equals(currentUserId);
        
        if (!userExists && !isRequestingUser) {
            throw USER_NOT_AUTHORIZED.accessForbiddenException();
        }
    }
}
