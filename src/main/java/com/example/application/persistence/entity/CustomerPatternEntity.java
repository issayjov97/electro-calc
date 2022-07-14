//package com.example.application.persistence.entity;
//
//import javax.persistence.Entity;
//import javax.persistence.FetchType;
//import javax.persistence.GeneratedValue;
//import javax.persistence.GenerationType;
//import javax.persistence.Id;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
//import javax.persistence.Table;
//import java.math.BigDecimal;
//
//@Entity
//@Table(name = "customer_patterns")
//public class CustomerPatternEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Integer id;
//    private String name;
//    private String description;
//    private Double duration;
//    private BigDecimal priceWithoutVat;
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="user_entity_id", nullable=false)
//    private UserEntity userEntity;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public Double getDuration() {
//        return duration;
//    }
//
//    public BigDecimal getPriceWithoutVat() {
//        return priceWithoutVat;
//    }
//
//    public UserEntity getUserEntity() {
//        return userEntity;
//    }
//
//    public void setUserEntity(UserEntity userEntity) {
//        this.userEntity = userEntity;
//    }
//
//    public void setId(Integer id) {
//        this.id = id;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
//
//    public void setDuration(Double duration) {
//        this.duration = duration;
//    }
//
//    public void setPriceWithoutVat(BigDecimal priceWithoutVat) {
//        this.priceWithoutVat = priceWithoutVat;
//    }
//}
