package it.getinsight.module.role.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.client.entity.ClientEntity;
import it.getinsight.module.role.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository, JpaSpecificationExecutor<RoleEntity> {

    Optional<RoleEntity> findByNameAndClient(String nome, ClientEntity cliente);

    Optional<RoleEntity> findByRoleExternalId(String roleExternalId);

    List<RoleEntity> findAllByClient(ClientEntity client);

    @Query(value = """
        SELECT *
        FROM TB_ROLE r
        WHERE r.ID_CLIENTE = :clientId
          AND LOWER(r.NOME) = LOWER(:name)
          AND r.ATIVO = true
        """, nativeQuery = true)
    List<RoleEntity> findByClientIdAndNameIgnoreCase(@Param("clientId") Long clientId, @Param("name") String name);

    List<RoleEntity> findAllByRoleIn(List<RoleEntity> roles);

    @Modifying
    @Query("UPDATE RoleEntity r SET r.active = false WHERE r.id = :id")
    void softDelete(@Param("id") Long id);

    @Query(value = """
        WITH RECURSIVE role_descendants AS (
            -- Base case: Get the direct children of the parent role
            SELECT r.*
            FROM TB_ROLE r
            WHERE r.ID_ROLE_PARENT = :parentRoleId
            AND r.ATIVO = true

            UNION ALL

            -- Recursive case: Get all descendants
            SELECT r.*
            FROM TB_ROLE r
            INNER JOIN role_descendants rd ON r.ID_ROLE_PARENT = rd.ID
            WHERE r.ATIVO = true
        )
        SELECT DISTINCT rd.*
        FROM role_descendants rd
        WHERE (:clientId IS NULL OR rd.ID_CLIENTE = :clientId)
        """, nativeQuery = true)
    List<RoleEntity> findDescendantRoles(@Param("parentRoleId") Long parentRoleId, @Param("clientId") Long clientId);

    @Query(value = """
        WITH RECURSIVE role_ancestors AS (
            SELECT r.*
            FROM TB_ROLE r
            WHERE r.ID = :childRoleId
            AND r.ATIVO = true

            UNION ALL

            SELECT r.*
            FROM TB_ROLE r
            INNER JOIN role_ancestors ra ON r.ID = ra.ID_ROLE_PARENT
            WHERE r.ATIVO = true
        )
        SELECT DISTINCT ra.*
        FROM role_ancestors ra
        WHERE (:clientId IS NULL OR ra.ID_CLIENTE = :clientId)
        """, nativeQuery = true)
    List<RoleEntity> findAncestorRoles(@Param("childRoleId") Long childRoleId, @Param("clientId") Long clientId);

}
