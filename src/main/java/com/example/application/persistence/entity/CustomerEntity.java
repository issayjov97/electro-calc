package com.example.application.persistence.entity;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "customers")
public class CustomerEntity extends AbstractEntity {

    private String name;
    private String email;
    private String phone;
    @Column(columnDefinition = "TEXT")
    private String note;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "firm_id")
    private FirmEntity firmEntity;

    @OneToMany(
            mappedBy = "customerEntity",
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private Set<OfferEntity> offers;

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

    public Set<OfferEntity> getOffers() {
        return offers;
    }

    public void setOffers(Set<OfferEntity> offers) {
        this.offers = offers;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void addOffer(OfferEntity offer) {
        this.offers.add(offer);
        offer.setCustomerEntity(this);
    }

    public void removeOffer(OfferEntity offer) {
        this.offers.remove(offer);
        offer.setCustomerEntity(null);
    }
}
