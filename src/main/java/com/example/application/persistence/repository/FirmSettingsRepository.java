package com.example.application.persistence.repository;

import com.example.application.persistence.entity.FirmSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FirmSettingsRepository extends JpaRepository<FirmSettingsEntity, Long> {
}
