package it.getinsight.module.request.service;

import it.getinsight.module.request.dto.RequestUpdateDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.enuns.RequestStatus;
import it.getinsight.module.request.repository.RequestRepository;
import it.getinsight.module.role.repository.RoleRepository;
import it.getinsight.module.role.service.RoleService;
import it.getinsight.module.user.repository.UserRepository;
import it.getinsight.module.user.service.AuthenticationContextService;
import it.getinsight.module.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static it.getinsight.message.MessageProperty.ROLE_NOT_FOUND_ERROR;
import static it.getinsight.message.MessageProperty.USER_NOT_FOUND_ERROR;


@Service
@RequiredArgsConstructor
@Slf4j
public class RequestStatusUpdateService {

    private final AuthenticationContextService authenticationContextService;
    private final UserService userService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RequestRepository requestRepository;


    public boolean canUserApproveRequest(RequestEntity requestEntity) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);
        var roleEntityParent = roleRepository.findByRoleExternalId(requestEntity.getRole().getRoleExternalId())
            .orElseThrow(ROLE_NOT_FOUND_ERROR::resourceNotFoundException);
        var approvingUsersDTO = roleService.getOrImportApprovesByRoleId(roleEntityParent.getId());

        return approvingUsersDTO.stream().anyMatch(obj -> obj.id().equals(approvingUserDTO.id()));
    }


    public void updateRequestBasicFields(RequestEntity requestEntity, RequestUpdateDTO requestUpdateDTO) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);
        var approvingUserEntity = userRepository.findById(approvingUserDTO.id())
            .orElseThrow(USER_NOT_FOUND_ERROR::businessException);

        requestEntity.setStatus(RequestStatus.valueOf(requestUpdateDTO.status()));
        requestEntity.setApprovingUser(approvingUserEntity);
        requestEntity.setFinalReason(requestUpdateDTO.finalReason());
    }


    public void persistRejectedOrCanceledRequest(RequestEntity requestEntity, Long requestId, String status) {
        var currentUserId = authenticationContextService.getCurrentUserId();
        var approvingUserDTO = userService.findOrImportByExternalId(currentUserId);

        log.info("User {} is updating request {} to status {}", approvingUserDTO.email(), requestId, status);
        requestRepository.save(requestEntity);
    }


    public boolean shouldPersistRequest(RequestStatus status) {
        return List.of(RequestStatus.REJECTED, RequestStatus.CANCELED).contains(status);
    }
}
