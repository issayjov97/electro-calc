package com.example.application.persistence.entity;


import javax.persistence.MappedSuperclass;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@MappedSuperclass
public abstract class AbstractServiceEntity extends VATEntity {

    private BigDecimal    transportationCost;
    private BigDecimal    materialsCost;
    private Double        workHours = 0.0;
    private LocalDateTime createdAt;
    private String        description;

    public abstract Set<PatternEntity> getPatterns();

    public abstract CustomerEntity getCustomerEntity();

    public abstract Set<FileEntity> getFiles();

    public abstract void setCustomerEntity(CustomerEntity customerEntity);

    public abstract void setPatterns(Set<PatternEntity> patterns);

    public abstract void setFiles(Set<FileEntity> files);

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }


    public BigDecimal getTransportationCost() {
        return transportationCost;
    }

    public BigDecimal getMaterialsCost() {
        return materialsCost;
    }

    public Double getWorkHours() {
        return workHours;
    }

    public void setTransportationCost(BigDecimal transportationCost) {
        this.transportationCost = transportationCost;
    }

    public void setMaterialsCost(BigDecimal materialsCost) {
        this.materialsCost = materialsCost;
    }

    public void setWorkHours(Double workHours) {
        this.workHours = workHours;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
