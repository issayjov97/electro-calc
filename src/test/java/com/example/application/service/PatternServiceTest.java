package com.example.application.service;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.predicate.PatternSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityNotFoundException;

import static com.helger.commons.mock.CommonsAssert.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PatternServiceTest {

    @Autowired
    private PatternService patternService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @WithMockUser(username = "tester123")
    public void shouldOverrideDefaultPattern() {
        final String PLU = "6396654";

        var patterns = patternService.filter(new PatternSpecification(1L, PLU, null));

        assertEquals(1, patterns.size());
        assertTrue(patterns.stream().anyMatch(it -> it.getPLU().equals(PLU)));
        assertTrue(patterns.stream().anyMatch(it -> it.getFirmEntity() != null));
    }

//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldNotOverrideDefaultPattern() {
//        final long id = 5083L;
//        var pattern = patternService.load(id);
//
//        assertTrue(PatternEntity.isDefaultPattern(pattern));
//        pattern.setPLU("8321984751TEST");
//
//        var savedPattern = patternService.save(pattern);
//
//        var patterns = patternService.filter(new PatternSpecification(1L, null, pattern.getName()));
//
//        assertEquals(2, patterns.size());
//    }
//
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldCreateUserPatternFromDefault() {
//        final long id = 5083L;
//        var pattern = patternService.load(id);
//
//        assertTrue(PatternEntity.isDefaultPattern(pattern));
//
//        var savedPattern = patternService.save(pattern);
//        pattern = patternService.load(id);
//
//        assertNotNull(savedPattern.getFirmEntity());
//        assertNull(pattern.getFirmEntity());
//        assertNotEquals(pattern.getId(), savedPattern.getId());
//        assertEquals(pattern.getPLU(), savedPattern.getPLU());
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldFilterPatternsByName() {
//        final String name = "montáž";
//
//        var patterns = patternService.filter(new PatternSpecification(1L, null, name));
//
//        assertTrue(patterns.size() > 1);
//        assertTrue(patterns.stream().allMatch(it -> it.getName().contains(name)));
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldFilterPatternsByPLU() {
//        final String PLU = "1317940";
//        var patterns = patternService.filter(new PatternSpecification(1L, PLU, null));
//
//        assertEquals(1, patterns.size());
//        assertTrue(patterns.stream().allMatch(it -> it.getPLU().equals(PLU)));
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldDeleteFirmPattern() {
//        final long id = 5109L;
//        var pattern = patternService.load(id);
//
//        assertFalse(PatternEntity.isDefaultPattern(pattern));
//
//        patternService.delete(pattern);
//
//        assertThrows(EntityNotFoundException.class, () -> {
//            patternService.load(id);
//        });
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldNotDeleteDefaultPattern() {
//        final long id = 5106L;
//        patternService.getDefaultPatterns();
//        var pattern = patternService.load(id);
//
//
////        assertFalse(PatternEntity.isDefaultPattern(pattern));
//
//        patternService.delete(pattern);
//
//        assertThrows(EntityNotFoundException.class, () -> {
//            patternService.load(id);
//        });
//    }
}
