package com.example.application.persistence.repository;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.OfferPattern;
import com.example.application.persistence.entity.OfferPatternKey;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Set;

public interface OfferPatternRepository extends JpaRepository<OfferPattern, OfferPatternKey>, JpaSpecificationExecutor<OfferEntity> {

}
