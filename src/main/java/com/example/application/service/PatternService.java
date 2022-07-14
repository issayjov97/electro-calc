package com.example.application.service;

import com.example.application.dto.OrderDTO;
import com.example.application.dto.PatternDTO;
import com.example.application.mapper.PatternMapper;
import com.example.application.persistence.repository.PatternRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class PatternService {
    private final PatternRepository patternRepository;
    private final UserService       userService;
    private final OrderService orderService;

    public PatternService(
            PatternRepository patternRepository,
            UserService userService, OrderService orderService) {
        this.patternRepository = patternRepository;
        this.userService = userService;
        this.orderService = orderService;
    }

    public Set<PatternDTO> getAll() {
        Set<PatternDTO> customerPatternEntities = getUserPatterns();
        Set<PatternDTO> defaultPatternEntities = customerPatternEntities.isEmpty() ?
                getDefaultPatterns() : getUserDefaultPatterns(customerPatternEntities);

        return Stream.of(customerPatternEntities, defaultPatternEntities).flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Set<PatternDTO> getUserPatterns() {
        var user = userService.getByUsername(AuthService.getUsername());
        return user.getPatterns()
                .stream().map(PatternMapper::convertToDTO)
                .collect(Collectors.toSet());

    }

    @Transactional
    public void addOrderPatterns(Set<PatternDTO> userPatterns, OrderDTO orderDTO) {
        var user = userService.getByUsername(AuthService.getUsername());
        var order = orderService.getOrderEntityId(orderDTO.getId());
        var patterns = patternRepository.findAllById(userPatterns.stream().map(PatternDTO::getId)
                .collect(Collectors.toList()));
        var addedUserPatterns = patterns.stream().filter(it -> it.getUserEntity() == null).map(it -> PatternMapper.convertToEntity(it, user))
                .collect(Collectors.toSet());
        user.getPatterns().addAll(addedUserPatterns);
        order.getOrderPatterns().addAll(addedUserPatterns);
        patternRepository.saveAll(addedUserPatterns);
        userService.saveUserEntity(user);
        orderService.saveEntityOrder(order);
    }

    public Set<PatternDTO> getDefaultPatterns() {
        return patternRepository.findDefaultPattern().stream().map(PatternMapper::convertToDTO)
                .collect(Collectors.toSet());
    }

    public Set<PatternDTO> getUserDefaultPatterns(Set<PatternDTO> customerPatternEntities) {
        return patternRepository.findDefaultPatternsNotIn(
                customerPatternEntities.stream().map(PatternDTO::getName).collect(Collectors.toList()))
                .stream().map(PatternMapper::convertToDTO)
                .collect(Collectors.toSet());
    }
//    public Set<PatternDTO> findOrderPatterns(OrderDTO order) {
//        List<PatternDTO> orderPatterns = patternRepository
//
//        Set<PatternEntity> defaultPatternEntities = customerPatternEntities.isEmpty() ?
//                patternRepository.findDefaultPattern() :
//                patternRepository.findDefaultPatternsNotIn(
//                        customerPatternEntities.stream().map(PatternDTO::getName)
//                                .collect(Collectors.toList()));
//
//        return Stream.of(
//                customerPatternEntities,
//                defaultPatternEntities.stream().map(PatternMapper::convertToDTO).collect(Collectors.toList())
//        )
//                .flatMap(Collection::stream)
//                .collect(Collectors.toSet());
//    }


    public Set<PatternDTO> filter(String name, Double price, Double duration) {
        Set<PatternDTO> result = getAll();
        if (name != null && !name.isBlank()) {
            PatternPredicate.withName(name);
        }
        if (price != null) {
            PatternPredicate.withPrice(price);
        }
        if (duration != null) {
            PatternPredicate.withDuration(duration);
        }
        return PatternPredicate.filterPatterns(result);
    }


    public void savePattern(PatternDTO patternDTO) {
        var user = userService.getByUsername(AuthService.getUsername());
        var patternEntity = PatternMapper.convertToEntity(patternDTO);
        patternEntity.setUserEntity(user);
        patternRepository.save(patternEntity);
    }

    public void deletePattern(PatternDTO patternDTO) {
        patternRepository.deleteById(patternDTO.getId());
    }
}
