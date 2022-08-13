package com.example.application.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.persistence.EntityNotFoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FirmServiceTest {

    @Autowired
    private FirmService firmService;

    @Autowired
    private UserService userService;

    @Autowired
    private PatternService patternService;

    @BeforeEach
    void setUp() {

    }

    @Test
    @WithMockUser(username = "tester123")
    public void shouldReturnFirmDefaultPatterns() {
        var firm = firmService.getFirmWithDefaultPatterns();
        assertEquals(3, firm.getDefaultPatterns().size());
    }

    @Test
    @WithMockUser(username = "tester123")
    public void shouldRemoveFirmDefaultPattern() {
        var pattern = patternService.load(5083);
        firmService.deleteFirmWithDefaultPattern(pattern);
        var firm = firmService.getFirmWithDefaultPatterns();

        assertEquals(2, firm.getDefaultPatterns().size());
        pattern = patternService.load(5083);

        assertNotNull(pattern);
    }

    @Test
    @WithMockUser(username = "tester123")
    public void shouldReturnFirmCustomPatterns() {
        var patterns = patternService.getFirmPatterns();
        assertEquals(7, patterns.size());
    }

    @Test
    @WithMockUser(username = "tester123")
    public void shouldRemoveFirmCustomerPattern() {
        var pattern = patternService.load(5097);
        firmService.deleteFirmWithCustomPattern(pattern);
        var patterns = patternService.getFirmPatterns();

        assertEquals(6, patterns.size());


        assertThrows(EntityNotFoundException.class, () -> {
            patternService.load(5097);
        });
}


//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldNotOverrideDefaultPattern() {
//    }
//
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldCreateUserPatternFromDefault() {
//
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldFilterPatternsByName() {
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldFilterPatternsByPLU() {
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldDeleteFirmPattern() {
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void shouldNotDeleteDefaultPattern() {
//    }
}
