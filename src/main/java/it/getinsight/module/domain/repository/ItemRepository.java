package it.getinsight.module.domain.repository;

import it.getinsight.core.dynamicquery.repository.DynamicNativeQueryRepository;
import it.getinsight.core.dynamicquery.repository.DynamicQueryRepository;
import it.getinsight.module.domain.entity.ItemEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<ItemEntity, Long>, DynamicQueryRepository, DynamicNativeQueryRepository {

    List<ItemEntity> findAllByDomainId(Long id);

    Page<ItemEntity> findAllByDomainId(Long id, Example<ItemEntity> example, Pageable pageable);


    List<ItemEntity> findAllByDomainIdAndParentId(Long id, Long itemId);

    Page<ItemEntity> findAllByDomainIdAndParentId(Long id, Long itemId, Example<ItemEntity> example, Pageable pageable);

}
