package com.example.application.persistence.entity;

import com.example.application.service.FinancialService;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "patterns")
public class PatternEntity extends VATEntity {

    private String PLU;
    private String name;
    private String description;
    private Double duration = 0.0;
    private String measureUnit;


    @ManyToOne
    private FirmEntity firmEntity;

    @ManyToMany(mappedBy = "defaultPatterns")
    Set<FirmEntity> firmEntities = new HashSet<>();

    @ManyToMany(mappedBy = "orderPatterns")
    Set<OrderEntity> orders = new HashSet<>();

    @ManyToMany(mappedBy = "offerPatterns")
    Set<OfferEntity> offers = new HashSet<>();

    @ManyToMany(mappedBy = "demandPatterns")
    Set<DemandEntity> demands = new HashSet<>();

    @ManyToMany(mappedBy = "imagePatterns")
    List<ImageEntity> patternImages;

    public Set<FirmEntity> getFirmEntities() {
        return firmEntities;
    }

    public void setFirmEntities(Set<FirmEntity> firmEntities) {
        this.firmEntities = firmEntities;
    }

    public String getPLU() {
        return PLU;
    }

    public void setPLU(String PLU) {
        this.PLU = PLU;
    }

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getDuration() {
        return duration;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(Double duration) {
        this.duration = duration;
    }

    public Set<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderEntity> orders) {
        this.orders = orders;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public Set<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(Set<OfferEntity> offers) {
        this.offers = offers;
    }

    public Set<DemandEntity> getDemands() {
        return demands;
    }

    public void setDemands(Set<DemandEntity> demands) {
        this.demands = demands;
    }


    @Override
    public void calculate() {
        this.setPriceWithVAT(FinancialService.calculatePriceWithVat(this));
    }

    public List<ImageEntity> getPatternImages() {
        return patternImages;
    }

    public void setPatternImages(List<ImageEntity> patternImages) {
        this.patternImages = patternImages;
    }


    public static boolean isDefaultPattern(PatternEntity patternEntity) {
        return patternEntity.getId() != null && patternEntity.getFirmEntity() == null;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatternEntity that = (PatternEntity) o;

        if (PLU != null ? !PLU.equals(that.PLU) : that.PLU != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        return duration != null ? duration.equals(that.duration) : that.duration == null;
    }

    @Override
    public int hashCode() {
        int result = PLU != null ? PLU.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (duration != null ? duration.hashCode() : 0);
        return result;
    }
}
