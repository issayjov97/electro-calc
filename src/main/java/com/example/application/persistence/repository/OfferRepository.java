package com.example.application.persistence.repository;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OfferRepository extends JpaRepository<OfferEntity, Long> {

}
