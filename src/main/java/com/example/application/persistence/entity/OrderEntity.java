package com.example.application.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long       id;
    private BigDecimal transportationCost;
    private BigDecimal materialsCost;
    private double        workHours = 0.0;
    private Long          vat = 21L;
    private LocalDateTime createdAt;

    @ManyToOne
    private CustomerEntity customerEntity;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "order_pattern",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "pattern_id"))
    private Set<PatternEntity> orderPatterns;

    public Set<PatternEntity> getOrderPatterns() {
        return orderPatterns;

    }

    public Long getVat() {
        return vat;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public CustomerEntity getCustomerEntity() {
        return customerEntity;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setCustomerEntity(CustomerEntity customerEntity) {
        this.customerEntity = customerEntity;
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

    public void setVat(Long vat) {
        this.vat = vat;
    }

    public void setOrderPatterns(Set<PatternEntity> orderPatterns) {
        this.orderPatterns = orderPatterns;
    }

    @Override
    public String toString() {
        return "OrderEntity{" +
                "id=" + id +
                '}';
    }
}

