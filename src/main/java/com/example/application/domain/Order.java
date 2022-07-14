package com.example.application.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {

    private Long id;
    private BigDecimal totalPrice;
    private BigDecimal priceWithoutVat;
    private BigDecimal transportationCost;
    private BigDecimal materialsCost;
    private Long workHours;
    private LocalDateTime createdAt;

    public Order(
            Long id,
            LocalDateTime createdAt,
            BigDecimal totalPrice,
            BigDecimal priceWithoutVat,
            BigDecimal transportationCost,
            BigDecimal materialsCost,
            Long workHours
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.totalPrice = totalPrice;
        this.priceWithoutVat = priceWithoutVat;
        this.transportationCost = transportationCost;
        this.materialsCost = materialsCost;
        this.workHours = workHours;
    }

    public Long getId() {
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

    public Long getWorkHours() {
        return workHours;
    }
}
