package it.getinsight.module.request.service;

import it.getinsight.module.client.dto.ClientDTO;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.client.mapper.ClientMapper;
import it.getinsight.module.level.dto.ItemHierarchyResumedDTO;
import it.getinsight.module.level.dto.LevelDTO;
import it.getinsight.module.level.mapper.LevelMapper;
import it.getinsight.module.level.service.ItemService;
import it.getinsight.module.request.config.EmailNotificationProperties;
import it.getinsight.module.request.dto.RequestDTO;
import it.getinsight.module.request.entity.RequestEntity;
import it.getinsight.module.request.mapper.RequestMapper;
import it.getinsight.module.role.dto.RoleDTO;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.mapper.RoleMapper;
import it.getinsight.module.user.dto.UserDTO;
import it.getinsight.module.user.entity.UserEntity;
import it.getinsight.module.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RequestVariableService {

    private final ItemService itemService;
    private final EmailNotificationProperties emailNotificationProperties;
    private final UserMapper userMapper;
    private final RequestMapper requestMapper;
    private final RoleMapper roleMapper;
    private final ClientMapper clientMapper;
    private final LevelMapper levelMapper;

    public Map<String, Object> buildVariables(UserEntity requestingUser, UserEntity approvingUser,
                                              RequestEntity request, RoleEntity role, ClientEntity client) {
        return Map.of(
            "link", buildLinkVariables(),
            "approvingUser", Optional.ofNullable(userMapper.toDto(approvingUser)).orElse(UserDTO.builder().build()),
            "requestingUser", Optional.ofNullable(userMapper.toDto(requestingUser)).orElse(UserDTO.builder().build()),
            "request", Optional.ofNullable(requestMapper.toDto(request)).orElse(RequestDTO.builder().build()),
            "status", Optional.ofNullable(requestMapper.toDto(request).status().getDescription()).orElse(""),
            "role", Optional.ofNullable(roleMapper.toDto(role)).orElse(RoleDTO.builder().build()),
            "client", Optional.ofNullable(clientMapper.toDto(client)).orElse(ClientDTO.builder().build()),
            "level", Optional.ofNullable(levelMapper.toDto(role.getLevel())).orElse(LevelDTO.builder().build()),
            "item", getItemOrDefault(role, request)
        );
    }

    private Map<String, String> buildLinkVariables() {
        var url = emailNotificationProperties.getUrl();
        return Map.of(
            "address", url.getClientUrl(),
            "hint", url.getHint(),
            "frontendUrl", url.getFrontendUrl()
        );
    }

    private ItemHierarchyResumedDTO getItemOrDefault(RoleEntity role, RequestEntity request) {
        return Optional.ofNullable(request.getCodeItem())
            .flatMap(code -> itemService.findByTypeAndCodeItem(role.getLevel(), code))
            .orElse(ItemHierarchyResumedDTO.builder().build());
    }
}
