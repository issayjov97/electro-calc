package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.AbstractEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.PatternRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class PatternService implements FilterableCrudService<PatternEntity> {
    private final PatternRepository patternRepository;
    private final UserService userService;
    private final FirmService firmService;

    public PatternService(PatternRepository patternRepository, UserService userService, FirmService firmService) {
        this.patternRepository = patternRepository;
        this.userService = userService;
        this.firmService = firmService;
    }

    @Override
    public JpaRepository<PatternEntity, Long> getRepository() {
        return patternRepository;
    }

    @Override
    @Transactional
    public PatternEntity save(PatternEntity entity) {
        if (PatternEntity.isDefaultPattern(entity)) {
            var pattern = patternRepository.findPatternWithFirmsById(entity.getId());
            if (!pattern.equals(entity)) {
                var firm = firmService.getFirmWithDefaultPatterns();
                firm.removeDefaultPattern(pattern);
                entity = PatternMapper.convertToEntity(entity, firm);
                entity.getFirmEntity().addPattern(entity);
            }
        } else {
            entity.setFirmEntity(userService.getUserFirm(AuthService.getUsername()));
        }
        var savedPattern = FilterableCrudService.super.save(entity);
        return patternRepository.getOne(savedPattern.getId());
    }

    public List<PatternEntity> saveAll(Collection<PatternEntity> patterns) {
        return patternRepository.saveAll(patterns);
    }

    @Override
    @Transactional
    public void delete(PatternEntity entity) {
        var pattern = patternRepository.findFullPatternById(entity.getId());
        var firm = firmService.getFirmWithDefaultPatterns();
        if (PatternEntity.isDefaultPattern(pattern)) {
            firm.removeDefaultPattern(pattern);
            firmService.save(firm);
        } else {
            firm.removePattern(pattern);
            FilterableCrudService.super.delete(pattern);
        }
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> getFirmDefaultPatterns() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return patternRepository.findByDefaultPatternsFirmEntityId(firm.getId());
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> getFirmPatterns() {
        var firm = userService.getUserFirm(AuthService.getUsername());
        return patternRepository.findFirmPatterns(firm.getId());
    }

    @Override
    public long countAnyMatching(Specification<PatternEntity> specification) {
        return patternRepository.count(specification);
    }

    @Override
    public Set<PatternEntity> filter(Specification<PatternEntity> specifications, int offset, int size) {
        return new LinkedHashSet<>(patternRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }

    @Transactional(readOnly = true)
    public Set<PatternEntity> filter(Specification<PatternEntity> specifications) {
        return new LinkedHashSet<>(patternRepository.findAll(specifications));
    }

    @Transactional(readOnly = true)
    public PatternEntity getPattern(PatternEntity patternEntity) {
        return patternRepository.findByName(patternEntity.getName(), patternEntity.getDescription(), patternEntity.getDuration());
    }
}
