package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.OrderEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.OrderRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService implements CrudService<OrderEntity> {
    private final OrderRepository orderRepository;
    private final UserService     userService;
    private final FirmService     firmService;

    public OrderService(OrderRepository orderRepository, UserService userService, FirmService firmService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
        this.firmService = firmService;
    }

    @Transactional
    @Override
    public OrderEntity save(OrderEntity orderEntity) {
        if (orderEntity.getId() == null)
            orderEntity.setCreatedAt(LocalDateTime.now());
        orderEntity.setFirmEntity(userService.getUserFirm());
        return CrudService.super.save(orderEntity);
    }

    @Override
    public void delete(OrderEntity entity) {
        entity.setCustomerEntity(null);
        CrudService.super.delete(entity);
    }

    @Override
    public void deleteById(long id) {
        CrudService.super.deleteById(id);
    }

    @Override
    public long count() {
        return CrudService.super.count();
    }

    @Override
    public OrderEntity load(long id) {
        return CrudService.super.load(id);
    }

    @Override
    public JpaRepository<OrderEntity, Long> getRepository() {
        return orderRepository;
    }

    @Transactional(readOnly = true)
    public Set<OrderEntity> getFirmOrders() {
        return userService.getUserFirm().getOrders();
    }

    @Transactional
    public void addOrderPatterns(Set<PatternEntity> orderPatterns, OrderEntity orderEntity) {
        var firmEntity = userService.getUserFirm();
        var addedUserPatterns = orderPatterns.stream().filter(it -> it.getFirmEntity() == null)
                .map(it -> PatternMapper.convertToEntity(it, firmEntity))
                .collect(Collectors.toSet());
        firmEntity.getPatterns().addAll(addedUserPatterns);
        orderEntity.addPatterns(orderPatterns);
        firmService.save(firmEntity);
        save(orderEntity);
    }
}
