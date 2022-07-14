//package com.example.application.persistence.entity;
//
//import com.example.application.domain.Order;
//
//import javax.persistence.Column;
//import javax.persistence.EmbeddedId;
//import javax.persistence.Entity;
//import javax.persistence.EnumType;
//import javax.persistence.Enumerated;
//import javax.persistence.FetchType;
//import javax.persistence.ManyToOne;
//import javax.persistence.MapsId;
//import javax.persistence.Table;
//import java.util.Date;
//
//@Entity
//@Table(name = "order_pattern")
//public class OrderPattern {
//
//    @EmbeddedId
//    private OrderPatternId orderPatternId;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("orderId")
//    private OrderEntity order;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @MapsId("patternId")
//    private PatternEntity pattern;
//
//}
