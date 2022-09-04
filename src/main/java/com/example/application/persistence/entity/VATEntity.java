package com.example.application.persistence.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.math.BigDecimal;


@MappedSuperclass
public abstract class VATEntity extends AbstractEntity {

    private BigDecimal priceWithoutVAT      = BigDecimal.ZERO;
    @Transient
    private BigDecimal priceWithVAT         = BigDecimal.ZERO;
    @Transient
    private BigDecimal totalPriceWithoutVAT = BigDecimal.ZERO;
    @Transient
    private BigDecimal totalPriceWithVAT   = BigDecimal.ZERO;

    public BigDecimal getPriceWithVAT() {
        return priceWithVAT;
    }

    public BigDecimal getPriceWithoutVAT() {
        return priceWithoutVAT;
    }

    public void setPriceWithoutVAT(BigDecimal priceWithoutVAT) {
        this.priceWithoutVAT = priceWithoutVAT;
    }

    public void setPriceWithVAT(BigDecimal priceWithVAT) {
        this.priceWithVAT = priceWithVAT;
    }

    public BigDecimal getTotalPriceWithoutVAT() {
        return totalPriceWithoutVAT;
    }

    public void setTotalPriceWithoutVAT(BigDecimal totalPriceWithoutVAT) {
        this.totalPriceWithoutVAT = totalPriceWithoutVAT;
    }

    public BigDecimal getTotalPriceWithVAT() {
        return totalPriceWithVAT;
    }

    public void setTotalPriceWithVAT(BigDecimal totalPriceWithVAT) {
        this.totalPriceWithVAT = totalPriceWithVAT;
    }
}
