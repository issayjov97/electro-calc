package com.example.application.persistence.entity;

import javax.persistence.MappedSuperclass;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.math.BigDecimal;


@MappedSuperclass
public abstract class VATEntity extends AbstractEntity {

    private Integer    VAT = 21;
    private BigDecimal priceWithoutVAT = new BigDecimal(0);
    @Transient
    private BigDecimal priceWithVAT;


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

    public Integer getVAT() {
        return VAT;
    }
    public void setVAT(Integer VAT) {
        this.VAT = VAT;
    }

    @PostLoad
    public abstract void calculate();
}
