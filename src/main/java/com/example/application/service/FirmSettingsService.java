package com.example.application.service;

import com.example.application.persistence.entity.FirmSettingsEntity;
import com.example.application.persistence.repository.FirmSettingsRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FirmSettingsService implements CrudService<FirmSettingsEntity> {
    private final UserService userService;
    private final FirmSettingsRepository firmSettingsRepository;

    public FirmSettingsService(UserService userService, FirmSettingsRepository firmSettingsRepository) {
        this.userService = userService;
        this.firmSettingsRepository = firmSettingsRepository;
    }

    @Override
    public JpaRepository<FirmSettingsEntity, Long> getRepository() {
        return firmSettingsRepository;
    }

    public FirmSettingsEntity getFirmSettings() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return firm.getFirmSettings();
    }

    @Transactional
    @CacheEvict(value = "firms", allEntries = true)
    @Override
    public FirmSettingsEntity save(FirmSettingsEntity entity) {
        var firm = userService.getUserFirm(AuthService.getUsername());
        entity.setFirmEntity(firm);
        return CrudService.super.save(entity);
    }
}
