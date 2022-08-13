package com.example.application.persistence.entity;

import com.example.application.service.FinancialService;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "orders")
public class OrderEntity extends AbstractServiceEntity {

    @ManyToOne
    private CustomerEntity customerEntity;

    @ManyToOne
    private FirmEntity firmEntity;

    @ManyToOne
    private JobOrderEntity jobOrderEntity;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "order_pattern",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id"))
    private Set<PatternEntity> orderPatterns;

    @OneToMany(
            mappedBy = "orderEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<FileEntity> orderFiles;

    public void addFileEntity(FileEntity fileEntity) {
        this.orderFiles.add(fileEntity);
        fileEntity.setOrderEntity(this);
    }

    public void removeFileEntity(FileEntity fileEntity) {
        this.orderFiles.remove(fileEntity);
        fileEntity.setOrderEntity(null);
    }

    public void addPattern(PatternEntity patternEntity) {
        this.orderPatterns.add(patternEntity);
        patternEntity.getOrders().add(this);
    }

    public void addPatterns(Collection<PatternEntity> patternEntities) {
        this.orderPatterns.addAll(patternEntities);
        patternEntities.forEach(it -> it.getOrders().add(this));
    }


    public void removePattern(PatternEntity patternEntity) {
        this.orderPatterns.remove(patternEntity);
        patternEntity.getOrders().remove(this);
    }

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    @Override
    public void calculate() {
        this.setPriceWithoutVAT(FinancialService.calculateServicePriceWithoutVAT(this));
        this.setPriceWithVAT(FinancialService.calculatePriceWithVat(this));
    }

    @Override
    public Set<PatternEntity> getPatterns() {
        return orderPatterns;
    }

    @Override
    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    @Override
    public Set<FileEntity> getFiles() {
        return orderFiles;
    }

    @Override
    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    @Override
    public void setPatterns(Set<PatternEntity> patterns) {
        this.orderPatterns = patterns;
    }

    @Override
    public void setFiles(Set<FileEntity> files) {
        this.orderFiles = files;
    }

    public JobOrderEntity getJobOrderEntity() {
        return jobOrderEntity;
    }

    public void setJobOrderEntity(JobOrderEntity jobOrderEntity) {
        this.jobOrderEntity = jobOrderEntity;
    }
}

