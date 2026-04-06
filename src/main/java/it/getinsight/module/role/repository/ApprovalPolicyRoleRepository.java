package it.getinsight.module.role.repository;

import it.getinsight.module.role.entity.ApprovalPolicyRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApprovalPolicyRoleRepository extends JpaRepository<ApprovalPolicyRoleEntity, Long> {
}
