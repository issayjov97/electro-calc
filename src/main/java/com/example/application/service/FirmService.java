package com.example.application.service;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.FirmSettingsEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.FirmRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class FirmService implements FilterableCrudService<FirmEntity> {

    private static final Logger logger = LoggerFactory.getLogger(FirmService.class);
    private final UserService userService;
    private final FirmRepository firmRepository;
    private final EntityManager entityManager;

    public FirmService(UserService userService, FirmRepository firmRepository, EntityManager entityManager) {
        this.userService = userService;
        this.firmRepository = firmRepository;
        this.entityManager = entityManager;
    }

    @Transactional
    @CacheEvict(value = "firms", allEntries = true)
    @Override
    public FirmEntity save(FirmEntity firmEntity) {
        if (firmEntity.getFirmSettings() == null)
            firmEntity.addFirmSettings(new FirmSettingsEntity());
        return FilterableCrudService.super.save(firmEntity);
    }

    @Override
    public JpaRepository<FirmEntity, Long> getRepository() {
        return firmRepository;
    }

    @Transactional(readOnly = true)
    public FirmEntity getFirmWithDefaultPatterns() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return firmRepository.getById(firm.getId());
    }

    @Transactional(readOnly = true)
    public FirmEntity getFirmWithAllPatterns() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return firmRepository.findWithAllPatternsById(firm.getId());
    }


    @Transactional
    public void deleteFirmWithDefaultPattern(PatternEntity patternEntity) {
        var firm = userService.getUserFirm(AuthService.getUsername());
        FirmEntity firmEntity = firmRepository.getById(firm.getId());
        firmEntity.removeDefaultPattern(patternEntity);
        firmRepository.saveAndFlush(firmEntity);
    }

    @Transactional
    public void deleteFirmWithCustomPattern(PatternEntity patternEntity) {
        var firm = userService.getUserFirm(AuthService.getUsername());
        FirmEntity firmEntity = firmRepository.getById(firm.getId());
        firmEntity.removePattern(patternEntity);
        firmRepository.saveAndFlush(firmEntity);
    }

    @Override
    public long countAnyMatching(Specification<FirmEntity> specification) {
        return 0;
    }

    @Override
    public Set<FirmEntity> filter(Specification<FirmEntity> specifications) {
        return new LinkedHashSet<>(firmRepository.findAll(specifications));
    }

    @Override
    public Set<FirmEntity> filter(Specification<FirmEntity> specifications, int offset, int size) {
        return null;
    }

    public void copyDefaultPatterns(Long id) {
        try {
            Query query = entityManager.createNativeQuery("CALL copy_patterns(:firmId)")
                    .setParameter("firmId", id);
            query.getSingleResult();
        } catch (Exception ex) {
            logger.warn("CALL copy_patterns firmId {}", id, ex);
        }
    }
}
