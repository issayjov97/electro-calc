package com.example.application.service;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.repository.FirmRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FirmService implements CrudService<FirmEntity> {
    private final FirmRepository firmRepository;

    public FirmService(FirmRepository firmRepository) {
        this.firmRepository = firmRepository;
    }

    @Override
    public JpaRepository<FirmEntity, Long> getRepository() {
        return firmRepository;
    }

    @Transactional
    @Override
    public FirmEntity save(FirmEntity firmEntity) {
        return CrudService.super.save(firmEntity);
    }

    @Override
    public void deleteById(long id) {
        CrudService.super.deleteById(id);
    }

    @Override
    public long count() {
        return CrudService.super.count();
    }

    @Override
    public FirmEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public List<FirmEntity> loadAll() {
        return CrudService.super.loadAll();
    }

}
