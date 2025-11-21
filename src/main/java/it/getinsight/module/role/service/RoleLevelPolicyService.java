package it.getinsight.module.role.service;

import it.getinsight.module.level.repository.LevelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static it.getinsight.message.MessageProperty.LEVEL_HIERARCHY_INVALID_ERROR;
import static it.getinsight.message.MessageProperty.LEVEL_WITH_ID_NOT_FOUND_ERROR;


@Service
@RequiredArgsConstructor
@Slf4j
public class RoleLevelPolicyService {

    private final LevelRepository levelRepository;


    public void validateChildLevelAssignment(Long parentLevelId, Long selfLevelId) {
        if (selfLevelId == null) {
            return;
        }

        if (parentLevelId == null) {
            throw LEVEL_HIERARCHY_INVALID_ERROR.businessException();
        }

        var childLevel = levelRepository.findById(selfLevelId)
            .orElseThrow(() -> LEVEL_WITH_ID_NOT_FOUND_ERROR.bind("id", String.valueOf(selfLevelId)).resourceNotFoundException());

        if (parentLevelId.equals(selfLevelId)) {
            return;
        }

        var immediateParent = childLevel.getParent();
        Long immediateParentId = immediateParent != null ? immediateParent.getId() : null;

        if (parentLevelId.equals(immediateParentId)) {
            return;
        }

        throw LEVEL_HIERARCHY_INVALID_ERROR.businessException();
    }

}
