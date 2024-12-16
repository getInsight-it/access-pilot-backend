package it.getinsight.module.role.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.role.entity.RoleEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository, JpaSpecificationExecutor<RoleEntity> {

    Optional<RoleEntity> findByNameAndClient(String nome, ClientEntity cliente);

    Optional<RoleEntity> findByRoleExternalId(String roleExternalId);
}
