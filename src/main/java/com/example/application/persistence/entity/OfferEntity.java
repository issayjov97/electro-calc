package com.example.application.persistence.entity;

import com.example.application.model.enums.OrderStatus;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "offers")
public class OfferEntity extends VATEntity {

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private double distance = 0;
    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDate  createdDate        = LocalDate.now();
    @Transient
    private BigDecimal transportationCost = BigDecimal.ZERO;
    @Transient
    private BigDecimal materialsCost = BigDecimal.ZERO;
    @Transient
    private Double     workDuration  = 0.0;

    @Transient
    private BigDecimal workCost = BigDecimal.ZERO;

    @ManyToOne
    private CustomerEntity customerEntity;

    @ManyToOne
    private FirmEntity firmEntity;

    @OneToMany(
            mappedBy = "offerEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<OfferPattern> offerPatterns = new HashSet<>();

    public void addPatterns(PatternEntity patternEntity, int count) {
        OfferPattern offerPattern = new OfferPattern();
        offerPattern.setId(new OfferPatternKey(this.getId(), patternEntity.getId()));
        offerPattern.setCount(count);
        offerPattern.setOfferEntity(this);
        offerPattern.setPatternEntity(patternEntity);
        this.offerPatterns.add(offerPattern);
    }


    public void removePattern(PatternEntity patternEntity) {
        for (Iterator<OfferPattern> iterator = offerPatterns.iterator(); iterator.hasNext(); ) {
            OfferPattern offerPattern = iterator.next();

            if (offerPattern.getOfferEntity().equals(this) && offerPattern.getPatternEntity().equals(patternEntity)) {
                iterator.remove();
                offerPattern.getOfferEntity().getPatterns().remove(offerPattern);
                offerPattern.setOfferEntity(null);
                offerPattern.setPatternEntity(null);
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass())
            return false;

        OfferEntity offer = (OfferEntity) o;
        return Objects.equals(name, offer.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    public Set<OfferPattern> getPatterns() {
        return offerPatterns;
    }

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
    }

    public void setPatterns(Set<OfferPattern> patterns) {
        this.offerPatterns = patterns;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getTransportationCost() {
        return transportationCost;
    }

    public void setTransportationCost(BigDecimal transportationCost) {
        this.transportationCost = transportationCost;
    }

    public BigDecimal getMaterialsCost() {
        return materialsCost;
    }

    public void setMaterialsCost(BigDecimal materialsCost) {
        this.materialsCost = materialsCost;
    }

    public Double getWorkDuration() {
        return workDuration;
    }

    public void setWorkDuration(Double workDuration) {
        this.workDuration = workDuration;
    }

    public LocalDate getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDate createdAt) {
        this.createdDate = createdAt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<OfferPattern> getOfferPatterns() {
        return offerPatterns;
    }

    public void setOfferPatterns(Set<OfferPattern> offerPatterns) {
        this.offerPatterns = offerPatterns;
    }

    public BigDecimal getWorkCost() {
        return workCost;
    }

    public void setWorkCost(BigDecimal workCost) {
        this.workCost = workCost;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

