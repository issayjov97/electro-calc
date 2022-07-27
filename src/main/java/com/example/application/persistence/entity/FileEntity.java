package com.example.application.persistence.entity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "files")
public class FileEntity extends AbstractEntity {

    private String      name;
    private String      type;
    private byte[]      data;
    @ManyToOne
    private OrderEntity orderEntity;

    @ManyToOne
    private OfferEntity offerEntity;

    @ManyToOne
    private DemandEntity demandEntity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public OfferEntity getOfferEntity() {
        return offerEntity;
    }

    public void setOfferEntity(OfferEntity offerEntity) {
        this.offerEntity = offerEntity;
    }

    public DemandEntity getDemandEntity() {
        return demandEntity;
    }

    public void setDemandEntity(DemandEntity demandEntity) {
        this.demandEntity = demandEntity;
    }
}