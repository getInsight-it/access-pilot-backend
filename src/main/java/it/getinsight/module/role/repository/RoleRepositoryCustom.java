package it.getinsight.module.role.repository;

import it.getinsight.module.role.entity.RoleEntity;

import java.util.List;
import java.util.Map;

public interface RoleRepositoryCustom {

    List<RoleEntity> findRoleIdsByResourceAccess(Map<String, List<String>> resourceAccess);
}
