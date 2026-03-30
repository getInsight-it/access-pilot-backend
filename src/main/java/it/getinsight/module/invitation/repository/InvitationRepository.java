package it.getinsight.module.invitation.repository;

import it.getinsight.module.invitation.entity.InvitationEntity;
import jakarta.persistence.LockModeType;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface InvitationRepository extends JpaRepository<InvitationEntity, Long>, JpaSpecificationExecutor<InvitationEntity> {

    @EntityGraph(attributePaths = {"role", "role.client", "role.level"})
    Optional<InvitationEntity> findByUuid(UUID uuid);

    @EntityGraph(attributePaths = {"role", "role.client", "role.level"})
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select i from InvitationEntity i where i.uuid = :invitationUuid")
    Optional<InvitationEntity> findByUuidForUpdate(@Param("invitationUuid") UUID invitationUuid);

    @EntityGraph(attributePaths = {"role", "role.client", "role.level"})
    @NonNull
    Page<InvitationEntity> findAll(@Nullable Specification<InvitationEntity> spec, @NonNull Pageable pageable);
}
