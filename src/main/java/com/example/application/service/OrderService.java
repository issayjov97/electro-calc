package com.example.application.service;

import com.example.application.dto.OrderDTO;
import com.example.application.mapper.OrderMapper;
import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.OrderEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.OrderRepository;
import com.example.application.persistence.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService     userService;

    public OrderService(
            OrderRepository orderRepository,
            UserRepository userRepository, UserService userService) {
        this.orderRepository = orderRepository;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getAll() {
        return orderRepository.findAll().stream().map(OrderMapper::convertToDTO)
                .sorted(Comparator.comparing(OrderDTO::getTotalPrice))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getUserOrders() {
        var userEntity = userService.getByUsername(AuthService.getUsername());
        var orders = userEntity.getCustomerEntities().stream().map(CustomerEntity::getOrders).flatMap(Collection::stream).collect(Collectors.toList());
        return orders.stream().map(OrderMapper::convertToDTO).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersSummary() {
        final var ordersEntities = orderRepository.findAll();
        return orderRepository.findAll().stream().map(OrderMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public void saveOrder(OrderDTO orderDTO) {
        orderDTO.setCreatedAt(LocalDateTime.now());
        orderRepository.saveAndFlush(OrderMapper.convertToEntity(orderDTO));
    }

    public void saveEntityOrder(OrderEntity order) {
        orderRepository.saveAndFlush(order);
    }

    public void updateOrder(OrderDTO orderDTO) {
        final OrderEntity orderEntity = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderEntity.setMaterialsCost(orderDTO.getMaterialsCost());
        orderEntity.setTransportationCost(orderDTO.getTransportationCost());
        orderEntity.setWorkHours(orderDTO.getWorkHours());
        orderEntity.setVat(orderDTO.getVAT());

        orderRepository.saveAndFlush(orderEntity);
    }


    public void update(OrderDTO orderDTO) {
        var orderEntity = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderEntity.getOrderPatterns().addAll(
                orderDTO.getPatterns().stream().map(PatternMapper::convertToEntity)
                        .collect(Collectors.toSet()));
        orderRepository.save(orderEntity);
    }

    public void deletePatterns(OrderDTO orderDTO) {
        var orderEntity = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orderEntity.setOrderPatterns(
                orderDTO.getPatterns().stream().map(PatternMapper::convertToEntity)
                        .collect(Collectors.toSet()));
        orderRepository.save(orderEntity);
    }


    public OrderDTO getOrderById(Long id) {
        var orderEntity = orderRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Order not found"));
        return OrderMapper.convertToDTO(orderEntity);
    }

    public OrderEntity getOrderEntityId(Long id) {
        return orderRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Order not found"));
    }


    public void deleteOrder(OrderDTO orderDTO) {
        orderRepository.deleteById(orderDTO.getId());
    }

}
