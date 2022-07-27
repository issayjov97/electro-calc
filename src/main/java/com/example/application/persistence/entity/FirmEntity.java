package com.example.application.persistence.entity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "firms")
public class FirmEntity extends AbstractEntity {

    private String         name;
    private String         street;
    private String         postCode;
    private String         city;
    private String         state;
    private String         CIN;
    private String         VATIN;
    private String         phone;
    private String         mobile;
    private String         email;

    @OneToMany(
            mappedBy = "firmEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private final Set<CustomerEntity> customerEntities = new HashSet<>();

    @OneToMany(
            mappedBy = "firmEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private final Set<UserEntity> users = new HashSet<>();

    @OneToMany(
            mappedBy = "firmEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<OfferEntity> offers;

    @OneToMany(
            mappedBy = "firmEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<OrderEntity> orders;

    @OneToMany(
            mappedBy = "firmEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<DemandEntity> demands;

    @OneToMany(
            mappedBy = "firmEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<JobOrderEntity> jobOrderEntities;

    @OneToMany(
            mappedBy = "firmEntity",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    Set<PatternEntity> patterns = new HashSet<>();


    public void addOrder(OrderEntity orderEntity) {
        this.orders.add(orderEntity);
        orderEntity.setFirmEntity(this);
    }

    public void removeOrder(OrderEntity orderEntity) {
        this.orders.remove(orderEntity);
        orderEntity.setFirmEntity(null);
    }

    public void addDemand(DemandEntity demandEntity) {
        this.demands.add(demandEntity);
        demandEntity.setFirmEntity(this);
    }

    public void removeDemand(DemandEntity demandEntity) {
        this.demands.remove(demandEntity);
        demandEntity.setFirmEntity(null);
    }

    public void addOffer(OfferEntity offerEntity) {
        this.offers.add(offerEntity);
        offerEntity.setFirmEntity(this);
    }

    public void removeOffer(OfferEntity offerEntity) {
        this.offers.remove(offerEntity);
        offerEntity.setFirmEntity(null);
    }

    public Set<JobOrderEntity> getJobOrderEntities() {
        return jobOrderEntities;
    }

    public void setJobOrderEntities(Set<JobOrderEntity> jobOrderEntities) {
        this.jobOrderEntities = jobOrderEntities;
    }

    public Set<CustomerEntity> getCustomerEntities() {
        return customerEntities;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCIN() {
        return CIN;
    }

    public void setCIN(String CIN) {
        this.CIN = CIN;
    }

    public String getVATIN() {
        return VATIN;
    }

    public void setVATIN(String VATIN) {
        this.VATIN = VATIN;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Set<UserEntity> getUsers() {
        return users;
    }

    public Set<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(Set<OfferEntity> offers) {
        this.offers = offers;
    }

    public Set<OrderEntity> getOrders() {
        return orders;
    }

    public void setOrders(Set<OrderEntity> orders) {
        this.orders = orders;
    }

    public Set<DemandEntity> getDemands() {
        return demands;
    }

    public void setDemands(Set<DemandEntity> demands) {
        this.demands = demands;
    }

    public Set<PatternEntity> getPatterns() {
        return patterns;
    }

    public void setPatterns(Set<PatternEntity> patterns) {
        this.patterns = patterns;
    }
}
