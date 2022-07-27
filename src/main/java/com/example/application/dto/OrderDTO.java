package com.example.application.dto;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.PatternEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public class OrderDTO {

    private long            id;
    private BigDecimal      totalPrice;
    private BigDecimal      priceWithoutVat;
    private BigDecimal      priceWithVat;
    private BigDecimal      transportationCost;
    private BigDecimal      materialsCost;
    private double        workHours;
    private Long          VAT;
    private LocalDateTime      createdAt;
    private Set<PatternEntity> patterns;
    private CustomerEntity        customer;

    public BigDecimal getPriceWithVat() {
        return priceWithVat;
    }

    public void setPriceWithVat(BigDecimal priceWithVat) {
        this.priceWithVat = priceWithVat;
    }

    public Long getVAT() {
        return VAT;
    }

    public void setVAT(Long VAT) {
        this.VAT = VAT;
    }


    public CustomerEntity getCustomer() {
        return customer;
    }

    public Set<PatternEntity> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<PatternEntity> patterns) {
        this.patterns = patterns;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public BigDecimal getPriceWithoutVat() {
        return priceWithoutVat;
    }

    public BigDecimal getTransportationCost() {
        return transportationCost;
    }

    public BigDecimal getMaterialsCost() {
        return materialsCost;
    }

    public double getWorkHours() {
        return workHours;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setPriceWithoutVat(BigDecimal priceWithoutVat) {
        this.priceWithoutVat = priceWithoutVat;
    }

    public void setTransportationCost(BigDecimal transportationCost) {
        this.transportationCost = transportationCost;
    }

    public void setMaterialsCost(BigDecimal materialsCost) {
        this.materialsCost = materialsCost;
    }

    public void setWorkHours(double workHours) {
        this.workHours = workHours;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }


    public void setCustomer(CustomerEntity customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                '}';
    }
}