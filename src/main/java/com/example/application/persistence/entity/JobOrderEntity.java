package com.example.application.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "job_orders")
public class JobOrderEntity extends AbstractEntity {
    private LocalDate createdAt;
    private String    jobName;
    private LocalDate demandDate;
    private LocalDate offerDate;
    private LocalDate orderDate;

    @ManyToOne
    private CustomerEntity customerEntity;

    @ManyToOne
    private FirmEntity firmEntity;

    @OneToMany(
            mappedBy = "jobOrderEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<OfferEntity> offerEntities;

    @OneToMany(
            mappedBy = "jobOrderEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<DemandEntity> demandEntities;

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String name) {
        this.jobName = name;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(LocalDate demandDate) {
        this.demandDate = demandDate;
    }

    public LocalDate getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(LocalDate offerDate) {
        this.offerDate = offerDate;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void addDemandEntity(DemandEntity demandEntity) {
        this.demandEntities.add(demandEntity);
        demandEntity.setJobOrderEntity(this);
    }

    public void removeDemandEntity(DemandEntity demandEntity) {
        this.demandEntities.remove(demandEntity);
        demandEntity.setJobOrderEntity(null);
    }

    public void addOfferEntity(OfferEntity offerEntity) {
        this.offerEntities.add(offerEntity);
        offerEntity.setJobOrderEntity(this);
    }

    public void removeDemandEntity(OfferEntity offerEntity) {
        this.offerEntities.remove(offerEntity);
        offerEntity.setJobOrderEntity(null);
    }

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public Set<OfferEntity> getOfferEntities() {
        return offerEntities;
    }

    public void setOfferEntities(Set<OfferEntity> offerEntities) {
        this.offerEntities = offerEntities;
    }

    public Set<DemandEntity> getDemandEntities() {
        return demandEntities;
    }

    public void setDemandEntities(Set<DemandEntity> demandEntities) {
        this.demandEntities = demandEntities;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JobOrderEntity that = (JobOrderEntity) o;
        return jobName != null ? !jobName.equals(that.jobName) : that.jobName != null;
    }

    @Override
    public int hashCode() {
        int result = createdAt != null ? createdAt.hashCode() : 0;
        result = 31 * result + (jobName != null ? jobName.hashCode() : 0);
        result = 31 * result + (demandDate != null ? demandDate.hashCode() : 0);
        result = 31 * result + (offerDate != null ? offerDate.hashCode() : 0);
        result = 31 * result + (orderDate != null ? orderDate.hashCode() : 0);
        return result;
    }
}

