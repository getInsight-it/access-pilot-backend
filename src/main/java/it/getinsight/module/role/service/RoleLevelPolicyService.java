package it.getinsight.module.role.service;

import it.getinsight.core.exception.BusinessException;
import it.getinsight.module.level.entity.LevelEntity;
import it.getinsight.module.level.repository.LevelRepository;
import it.getinsight.module.role.entity.RoleEntity;
import it.getinsight.module.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static it.getinsight.message.MessageProperty.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoleLevelPolicyService {

    private final LevelRepository levelRepository;


    public void validateChildLevelAssignment(Long parentLevelId, Long selfLevelId) {
        if (parentLevelId == null || selfLevelId == null) {
            return;
        }

        var ancestors = levelRepository.findAncestorLevels(selfLevelId);
        boolean parentIsAncestor = ancestors.stream().anyMatch(l -> l.getId().equals(parentLevelId));

        if (!parentIsAncestor) {
            throw LEVEL_HIERARCHY_INVALID_ERROR.businessException();
        }
    }

}
