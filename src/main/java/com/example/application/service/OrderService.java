package com.example.application.service;

import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.OrderEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.OrderRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrderService implements FilterableCrudService<OrderEntity> {
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
        if (orderEntity.getId() == null) {
            orderEntity.setCreatedAt(LocalDateTime.now());
            orderEntity.setFirmEntity(userService.getUserFirm());
        }
        return FilterableCrudService.super.save(orderEntity);
    }

    @Override
    public void delete(OrderEntity entity) {
        entity.setCustomerEntity(null);
        FilterableCrudService.super.delete(entity);
    }

    @Override
    public void deleteById(long id) {
        FilterableCrudService.super.deleteById(id);
    }

    @Override
    public long count() {
        return FilterableCrudService.super.count();
    }

    @Override
    public OrderEntity load(long id) {
        return FilterableCrudService.super.load(id);
    }

    @Override
    public JpaRepository<OrderEntity, Long> getRepository() {
        return orderRepository;
    }

    @Transactional(readOnly = true)
    public Set<OrderEntity> getFirmOrders() {
        var firm = userService.getUserFirm();
        return orderRepository.findOrderByFirmEntityId(firm.getId());
    }

    @Transactional(readOnly = true)
    public Set<OrderEntity> getCustomerOrders(Long id) {
        return orderRepository.findOrderByCustomerEntityId(id);
    }

    @Transactional(readOnly = true)
    public OrderEntity getOrder(Long id) {
        return orderRepository.getById(id);
    }

    @Transactional
    public void addOrderPatterns(Set<PatternEntity> orderPatterns, OrderEntity orderEntity) {
        var order = orderRepository.getById(orderEntity.getId());
        order.getPatterns().addAll(orderPatterns);
        save(order);
    }

    @Override
    public long countAnyMatching(Specification<OrderEntity> specification) {
        return orderRepository.count(specification);
    }

    @Override
    public Set<OrderEntity> filter(Specification<OrderEntity> specifications) {
        return new HashSet<>(orderRepository.findAll(specifications));
    }

    @Override
    public Set<OrderEntity> filter(Specification<OrderEntity> specifications, int offset, int size) {
        return new HashSet<>(orderRepository.findAll(specifications, PageRequest.of(offset / size, size)).getContent());
    }
}
