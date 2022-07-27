package com.example.application.persistence.entity;

import com.example.application.service.FinancialService;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "patterns")
public class PatternEntity extends VATEntity {

    private String name;
    private String description;
    private Double duration = 0.0;
    private String measureUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    private FirmEntity firmEntity;

    @ManyToMany(mappedBy = "orderPatterns")
    List<OrderEntity> orders;

    @ManyToMany(mappedBy = "offerPatterns")
    List<OfferEntity> offers;

    @ManyToMany(mappedBy = "demandPatterns")
    List<DemandEntity> demands;

    @ManyToMany(mappedBy = "imagePatterns")
    List<ImageEntity> patternImages;


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

    public List<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderEntity> orders) {
        this.orders = orders;
    }

    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public List<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(List<OfferEntity> offers) {
        this.offers = offers;
    }

    public List<DemandEntity> getDemands() {
        return demands;
    }

    public void setDemands(List<DemandEntity> demands) {
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


}
