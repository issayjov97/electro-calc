package com.example.application.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "customers")
public class CustomerEntity extends AbstractEntity {

    private String name;
    private String email;
    private String phone;

    @ManyToOne
    private FirmEntity firmEntity;

    @OneToMany(
            mappedBy = "customerEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<OrderEntity> orders;

    @OneToMany(
            mappedBy = "customerEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<OfferEntity> offers;

    @OneToMany(
            mappedBy = "customerEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER
    )
    private Set<DemandEntity> demands;

    public FirmEntity getFirmEntity() {
        return firmEntity;
    }

    public void setFirmEntity(FirmEntity firmEntity) {
        this.firmEntity = firmEntity;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Set<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderEntity> orders) {
        this.orders = orders;
    }

    public Set<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(Set<OfferEntity> offers) {
        this.offers = offers;
    }

    public Set<DemandEntity> getDemands() {
        return demands;
    }

    public void setDemands(Set<DemandEntity> demands) {
        this.demands = demands;
    }
}
