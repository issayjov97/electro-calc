package com.example.application.persistence.repository;

import com.example.application.persistence.entity.FirmSettingsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FirmSettingsRepository extends JpaRepository<FirmSettingsEntity, Long> {
}
