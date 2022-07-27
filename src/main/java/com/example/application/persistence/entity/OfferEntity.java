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
@Table(name = "offers")
public class OfferEntity extends AbstractServiceEntity {

    @ManyToOne
    private CustomerEntity customerEntity;

    @ManyToOne
    private JobOrderEntity jobOrderEntity;

    @ManyToOne
    private FirmEntity firmEntity;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.MERGE
    })
    @JoinTable(
            name = "offer_pattern",
            joinColumns = @JoinColumn(name = "offer_id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id"))
    private Set<PatternEntity> offerPatterns;

    @OneToMany(
            mappedBy = "offerEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<FileEntity> offerFiles;


    public void addFileEntity(FileEntity fileEntity) {
        this.offerFiles.add(fileEntity);
        fileEntity.setOfferEntity(this);
    }

    public void removeFileEntity(FileEntity fileEntity) {
        this.offerFiles.remove(fileEntity);
        fileEntity.setOfferEntity(null);
    }

    public void addPatterns(Collection<PatternEntity> patternEntities) {
        this.offerPatterns.addAll(patternEntities);
        patternEntities.forEach(it -> it.getOffers().add(this));
    }


    public void removePatter(PatternEntity patternEntity) {
        this.offerPatterns.remove(patternEntity);
        patternEntity.getOrders().remove(this);
    }

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    @Override
    public Set<PatternEntity> getPatterns() {
        return offerPatterns;
    }

    @Override
    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    @Override
    public Set<FileEntity> getFiles() {
        return offerFiles;
    }

    @Override
    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    @Override
    public void setPatterns(Set<PatternEntity> patterns) {
        this.offerPatterns = patterns;

    }

    @Override
    public void setFiles(Set<FileEntity> files) {
        this.offerFiles = files;
    }

    public JobOrderEntity getJobOrderEntity() {
        return jobOrderEntity;
    }

    public void setJobOrderEntity(JobOrderEntity jobOrderEntity) {
        this.jobOrderEntity = jobOrderEntity;
    }

    @Override
    public void calculate() {
        this.setPriceWithoutVAT(FinancialService.calculateServicePriceWithoutVAT(this));
        this.setPriceWithVAT(FinancialService.calculatePriceWithVat(this));
    }
}

