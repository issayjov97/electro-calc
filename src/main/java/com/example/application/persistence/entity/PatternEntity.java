package com.example.application.persistence.entity;

import com.example.application.service.FinancialService;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "patterns")
public class PatternEntity extends VATEntity {

    private String name;
    @Column(columnDefinition = "TEXT")
    private String description;
    private Double duration = 0.00;
    private String measureUnit;

    @ManyToOne(fetch = FetchType.LAZY)
    private FirmEntity firmEntity;

    @ManyToMany(mappedBy = "defaultPatterns")
    private Set<FirmEntity> firmEntities;

    @OneToMany(
            mappedBy = "patternEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    Set<OfferPattern> offerPatterns;

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


    public String getMeasureUnit() {
        return measureUnit;
    }

    public void setMeasureUnit(String measureUnit) {
        this.measureUnit = measureUnit;
    }

    public static boolean isDefaultPattern(PatternEntity patternEntity) {
        return patternEntity.getId() != null && patternEntity.getFirmEntity() == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PatternEntity pattern = (PatternEntity) o;
        return  Objects.equals(getId(), pattern.getId()) &&
                Objects.equals(name, pattern.name) &&
                Objects.equals(description, pattern.description) &&
                Objects.equals(duration, pattern.duration) &&
                Objects.equals(getPriceWithoutVAT(), pattern.getPriceWithoutVAT());

    }

    @Override
    public int hashCode() {
        return Objects.hash(name,description, duration, getPriceWithoutVAT());
    }

    public Set<OfferPattern> getOfferPatterns() {
        return offerPatterns;
    }

    public void setOfferPatterns(Set<OfferPattern> offerPatterns) {
        this.offerPatterns = offerPatterns;
    }

    public Set<FirmEntity> getFirmEntities() {
        return firmEntities;
    }

    public void setFirmEntities(Set<FirmEntity> firmEntities) {
        this.firmEntities = firmEntities;
    }

}
