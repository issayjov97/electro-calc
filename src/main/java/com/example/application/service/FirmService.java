package com.example.application.service;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.FirmSettingsEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.FirmRepository;
import com.example.application.ui.components.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;
import java.util.LinkedHashSet;
import java.util.List;
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
        return firmRepository.findWithDefaultPatternsById(firm.getId());
    }

    @Transactional(readOnly = true)
    public FirmEntity getFirmWithCustomPatterns() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return firmRepository.findWithCustomPatternsById(firm.getId());
    }

    public void enableCopyDefaultPatternsFeature() {
        var firms = firmRepository.findAll();
        firms.forEach(it -> it.setCopyDefaultPatterns(true));
        firmRepository.saveAllAndFlush(firms);
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
        return firmRepository.count(specification);
    }

    @Override
    public Set<FirmEntity> filter(Specification<FirmEntity> specifications) {
        return new LinkedHashSet<>(firmRepository.findAll(specifications));
    }

    @Override
    public Set<FirmEntity> filter(Specification<FirmEntity> specifications, int offset, int size) {
        return new LinkedHashSet<>(firmRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    public void copyDefaultPatterns(Long id) {
        var firm = load(id);
        StoredProcedureQuery storedQuery = entityManager
                .createStoredProcedureQuery("copy_patterns")
                .registerStoredProcedureParameter(
                        1,
                        Long.class,
                        ParameterMode.IN
                )
                .setParameter(1, firm.getId());

        storedQuery.execute();
        logger.info("Defaultni polozky byly uspesne pridany");
        firm.setCopyDefaultPatterns(true);
        getRepository().save(firm);
        NotificationService.success("Položky úspěšně byly přidány");
    }
}
