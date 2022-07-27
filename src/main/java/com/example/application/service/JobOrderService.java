package com.example.application.service;

import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.persistence.repository.JobOrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Service
public class JobOrderService implements CrudService<JobOrderEntity> {
    private final JobOrderRepository jobOrderRepository;
    private final UserService        userService;

    public JobOrderService(JobOrderRepository jobOrderRepository, UserService userService) {
        this.jobOrderRepository = jobOrderRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public JobOrderEntity save(JobOrderEntity jobOrderEntity) {
        if (jobOrderEntity.getId() == null)
            jobOrderEntity.setCreatedAt(LocalDate.now());
        jobOrderEntity.setFirmEntity(userService.getUserFirm());
        return CrudService.super.save(jobOrderEntity);
    }

    @Override
    public void delete(JobOrderEntity entity) {
        entity.setCustomerEntity(null);
        CrudService.super.delete(entity);
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
    public JobOrderEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public JpaRepository<JobOrderEntity, Long> getRepository() {
        return jobOrderRepository;
    }

    @Transactional(readOnly = true)
    public Set<JobOrderEntity> getFirmJobOrders() {
        return userService.getUserFirm().getJobOrderEntities();
    }
}
