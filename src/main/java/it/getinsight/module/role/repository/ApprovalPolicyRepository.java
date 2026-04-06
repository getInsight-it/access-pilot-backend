package it.getinsight.module.role.repository;

import it.getinsight.module.role.entity.ApprovalPolicyEntity;
import it.getinsight.module.role.enuns.ApprovalPolicyType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApprovalPolicyRepository extends JpaRepository<ApprovalPolicyEntity, Long> {

    @EntityGraph(attributePaths = {"roles", "roles.role"})
    List<ApprovalPolicyEntity> findAllByRoleId(Long roleId);

    @EntityGraph(attributePaths = {"roles", "roles.role"})
    Optional<ApprovalPolicyEntity> findByRoleIdAndType(Long roleId, ApprovalPolicyType type);

    @EntityGraph(attributePaths = {"role", "role.client", "roles", "roles.role", "roles.role.client", "roles.role.level"})
    @Query("""
        SELECT DISTINCT p
          FROM ApprovalPolicyEntity p
          JOIN p.roles t
         WHERE p.type = :type
           AND p.enabled = true
           AND p.active = true
           AND t.active = true
           AND t.role.id = :roleId
    """)
    List<ApprovalPolicyEntity> findEnabledByTypeAndRoleId(
        @Param("type") ApprovalPolicyType type,
        @Param("roleId") Long roleId
    );

    @EntityGraph(attributePaths = {"role", "role.client", "roles", "roles.role", "roles.role.client", "roles.role.level"})
    @Query("""
        SELECT DISTINCT p
          FROM ApprovalPolicyEntity p
          JOIN p.roles t
         WHERE p.type = :type
           AND p.enabled = true
           AND p.active = true
           AND t.active = true
           AND t.role.id IN :roleIds
    """)
    List<ApprovalPolicyEntity> findEnabledByTypeAndRoleIds(
        @Param("type") ApprovalPolicyType type,
        @Param("roleIds") java.util.Collection<Long> roleIds
    );
}
