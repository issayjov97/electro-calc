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
@Table(name = "demands")
public class DemandEntity extends AbstractServiceEntity {

    @ManyToOne
    private CustomerEntity customerEntity;

    @ManyToOne
    private JobOrderEntity jobOrderEntity;

    @ManyToOne
    private FirmEntity firmEntity;

    @ManyToMany(cascade = {CascadeType.MERGE})
    @JoinTable(
            name = "demand_pattern",
            joinColumns = @JoinColumn(name = "demand_id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id"))
    private Set<PatternEntity> demandPatterns;

    @OneToMany(
            mappedBy = "demandEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL
    )
    private Set<FileEntity> demandFiles;


    public void addFileEntity(FileEntity fileEntity) {
        this.demandFiles.add(fileEntity);
        fileEntity.setDemandEntity(this);
    }

    public void removeFileEntity(FileEntity fileEntity) {
        this.demandFiles.remove(fileEntity);
        fileEntity.setDemandEntity(null);
    }

    public void addPatterns(Collection<PatternEntity> patternEntities) {
        this.demandPatterns.addAll(patternEntities);
        patternEntities.forEach(it -> it.getDemands().add(this));
    }


    public void removePattern(PatternEntity patternEntity) {
        this.demandPatterns.remove(patternEntity);
        patternEntity.getDemands().remove(this);
    }

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    @Override
    public Set<PatternEntity> getPatterns() {
        return demandPatterns;
    }

    @Override
    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    @Override
    public Set<FileEntity> getFiles() {
        return demandFiles;
    }

    @Override
    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    @Override
    public void setPatterns(Set<PatternEntity> patterns) {
        this.demandPatterns = patterns;
    }

    @Override
    public void setFiles(Set<FileEntity> files) {
        this.demandFiles = files;
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

