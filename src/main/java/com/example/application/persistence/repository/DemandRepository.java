package com.example.application.persistence.repository;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.entity.OfferEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DemandRepository extends JpaRepository<DemandEntity, Long> {

}
