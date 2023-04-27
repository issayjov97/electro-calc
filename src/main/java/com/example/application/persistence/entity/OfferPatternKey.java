package com.example.application.persistence.entity;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class OfferPatternKey implements Serializable {

    @Column(name = "offer_id")
    Long offerId;

    @Column(name = "pattern_id")
    Long patternId;

    public OfferPatternKey(Long offerId, Long patternId) {
        this.offerId = offerId;
        this.patternId = patternId;
    }

    public OfferPatternKey() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OfferPatternKey that = (OfferPatternKey) o;

        return Objects.equals(patternId, that.patternId) &&
                Objects.equals(offerId, that.offerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(patternId, offerId);
    }

    public Long getPatternId() {
        return patternId;
    }

    public void setPatternId(Long patternId) {
        this.patternId = patternId;
    }

    public Long getOfferId() {
        return offerId;
    }

    public void setOfferId(Long offerId) {
        this.offerId = offerId;
    }

}