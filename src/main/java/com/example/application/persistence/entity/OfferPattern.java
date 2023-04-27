package com.example.application.persistence.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "offer_pattern")
public class OfferPattern {

    @EmbeddedId
    private OfferPatternKey id;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("offerId")
    @JoinColumn(name = "offer_id")
    private OfferEntity offerEntity;

    @ManyToOne(fetch = FetchType.EAGER)
    @MapsId("patternId")
    @JoinColumn(name = "pattern_id")
    private PatternEntity patternEntity;

    @Column(name = "count")
    private int count = 1;

    @Column(name = "include_cabel_cross_section")
    private boolean withCabelCrossSection;

    @Transient
    private BigDecimal materialsCost = BigDecimal.ZERO;

    @Transient
    private BigDecimal workCost = BigDecimal.ZERO;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OfferPattern that = (OfferPattern) o;

        return Objects.equals(patternEntity, that.patternEntity) &&
                Objects.equals(offerEntity, that.offerEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternEntity, offerEntity);
    }

    public OfferEntity getOfferEntity() {
        return offerEntity;
    }

    public void setOfferEntity(OfferEntity offerEntity) {
        this.offerEntity = offerEntity;
    }

    public PatternEntity getPatternEntity() {
        return patternEntity;
    }

    public void setPatternEntity(PatternEntity patternEntity) {
        this.patternEntity = patternEntity;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public OfferPatternKey getId() {
        return id;
    }

    public void setId(OfferPatternKey id) {
        this.id = id;
    }

    public BigDecimal getMaterialsCost() {
        return materialsCost;
    }

    public void setMaterialsCost(BigDecimal materialsCost) {
        this.materialsCost = materialsCost;
    }

    public BigDecimal getWorkCost() {
        return workCost;
    }

    public void setWorkCost(BigDecimal workCost) {
        this.workCost = workCost;
    }

    public boolean withCabelCrossSection() {
        return withCabelCrossSection;
    }

    public void setWithCabelCrossSection(boolean withCabelCrossSection) {
        this.withCabelCrossSection = withCabelCrossSection;
    }
}