package com.example.application.persistence.repository;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.entity.OfferEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface DemandRepository extends JpaRepository<DemandEntity, Long>, JpaSpecificationExecutor<DemandEntity> {

    Set<DemandEntity> findDemandByFirmEntityId(Long id);

    @EntityGraph(attributePaths = {"demandPatterns"})
    DemandEntity getById(Long id);
}
