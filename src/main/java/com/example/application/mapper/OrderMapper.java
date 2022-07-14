package com.example.application.mapper;

import com.example.application.dto.OrderDTO;
import com.example.application.persistence.entity.OrderEntity;
import com.example.application.persistence.entity.PatternEntity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.stream.Collectors;

public class OrderMapper {

    public static OrderDTO convertToDTO(OrderEntity source) {
        final OrderDTO orderDTO = new OrderDTO();
        orderDTO.setId(source.getId());
        orderDTO.setCreatedAt(source.getCreatedAt());
        orderDTO.setMaterialsCost(source.getMaterialsCost());
        orderDTO.setPriceWithoutVat(calculatePriceWithoutVat(source));
        orderDTO.setPriceWithVat(calculatePriceWithVat(source, orderDTO.getPriceWithoutVat()));
        orderDTO.setTransportationCost(source.getTransportationCost());
        orderDTO.setWorkHours(source.getWorkHours());
        orderDTO.setVAT(source.getVat());
        orderDTO.setTotalPrice(calculatePriceWithVat(source, orderDTO.getPriceWithoutVat()));
        orderDTO.setPatterns(source.getOrderPatterns() != null ?
                source.getOrderPatterns().stream().map(PatternMapper::convertToDTO).collect(Collectors.toSet())
                : Collections.emptySet());
//        if (source.getCustomerEntity() != null)
//            orderDTO.setCustomer(CustomerMapper.convertToDTO(source.getCustomerEntity()));
        return orderDTO;
    }

    private static BigDecimal calculatePriceWithoutVat(OrderEntity source) {
        return source.getMaterialsCost().add(source.getTransportationCost())
                .add(
                        source.getOrderPatterns().stream().map(PatternEntity::getPriceWithoutVat)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );
    }

    private static BigDecimal calculateTotalPrice(OrderEntity  source, BigDecimal priceWithoutVat) {
        return source.getMaterialsCost().add(source.getTransportationCost())
                .add(
                        source.getOrderPatterns().stream().map(PatternEntity::getPriceWithoutVat)
                                .reduce(BigDecimal.ZERO, BigDecimal::add)
                );    }

    private static BigDecimal calculatePriceWithVat(OrderEntity source,  BigDecimal priceWithoutVat) {
        final double rate =  source.getVat() / 100.0;
        final BigDecimal VAT =  priceWithoutVat.multiply(new BigDecimal(rate)).setScale(2, RoundingMode.HALF_UP);
        return priceWithoutVat.add(VAT);
    }

    public static OrderEntity convertToEntity(OrderDTO source) {
        final OrderEntity orderEntity = new OrderEntity();
        orderEntity.setId(source.getId());
        orderEntity.setCreatedAt(source.getCreatedAt());
        orderEntity.setMaterialsCost(source.getMaterialsCost());
        orderEntity.setTransportationCost(source.getTransportationCost());
        orderEntity.setWorkHours(source.getWorkHours());
        return orderEntity;
    }
}
