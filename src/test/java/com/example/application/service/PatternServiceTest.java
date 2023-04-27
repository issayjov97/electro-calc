package com.example.application.service;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.predicate.PatternSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class PatternServiceTest {

    @Autowired
    private PatternService patternService;

    @Autowired
    private FirmService firmService;

    @BeforeEach
    void setUp() {

    }

//    @Test
//    @WithMockUser(username = "tester123")
//    void shouldOverrideDefaultPattern() {
//        final  defaultPattern = patternService.getPattern();
//
//        var patterns = patternService.filter(new PatternSpecification(1L, null));
//
//        assertEquals(1, patterns.size());
//        assertTrue(patterns.stream().anyMatch(it -> it.getFirmEntity() != null));
//    }

    @Test
    @WithMockUser(username = "tester123")
    void shouldNotOverrideDefaultPattern() {
        final long id = 5083L;
        var pattern = patternService.load(id);

        assertTrue(PatternEntity.isDefaultPattern(pattern));

        patternService.save(pattern);

        var patterns = patternService.filter(new PatternSpecification(1L, pattern.getName()));

        assertEquals(1, patterns.size());
    }


    @Test
    @WithMockUser(username = "tester123")
    void shouldCreatFirmPatternFromDefault() {
        final long id = 5083L;
        var pattern = patternService.load(id);

        assertTrue(PatternEntity.isDefaultPattern(pattern));

        var savedPattern = updatePattern(pattern);
        pattern = patternService.load(id);

        assertNotNull(savedPattern.getFirmEntity());
        assertNull(pattern.getFirmEntity());
        assertNotEquals(pattern.getId(), savedPattern.getId());
        assertNotEquals(pattern.getId(), savedPattern.getId());
    }

    @Test
    @WithMockUser(username = "tester123")
    void shouldFilterPatternsByName() {
        final String name = "montáž";

        var patterns = patternService.filter(new PatternSpecification(1L, name));

        assertTrue(patterns.size() > 1);
        assertTrue(patterns.stream().allMatch(it -> it.getName().contains(name)));
    }

    @Test
    @WithMockUser(username = "tester123")
    void shouldDeleteFirmPattern() {
        final long id = 5109L;
        var pattern = patternService.load(id);
        assertFalse(PatternEntity.isDefaultPattern(pattern));

        patternService.delete(pattern);

        assertThrows(EntityNotFoundException.class, () -> {
            patternService.load(id);
        });
    }

    @Test
    @WithMockUser(username = "tester123")
    void shouldNotDeleteDefaultPattern() {
        final long id = 5106L;
        var firmDefaultPatterns = firmService.getFirmWithDefaultPatterns().getDefaultPatterns();
        var pattern = patternService.load(id);

        assertTrue(firmDefaultPatterns.contains(pattern));

        patternService.delete(pattern);
        var result = patternService.load(id);
        firmDefaultPatterns = firmService.getFirmWithDefaultPatterns().getDefaultPatterns();

        assertFalse(firmDefaultPatterns.contains(pattern));
        assertEquals(pattern.getId(), result.getId());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    PatternEntity updatePattern(PatternEntity pattern) {
        pattern.setName("TEST");
        return patternService.save(pattern);
    }
}
