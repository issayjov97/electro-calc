package com.example.application.service;

import com.example.application.ui.components.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.persistence.ParameterMode;
import javax.persistence.StoredProcedureQuery;

import static com.example.application.Values.TESTER;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class FirmServiceTest {

    @Autowired
    private FirmService firmService;

    @Autowired
    private PatternService patternService;

    @Test
    @WithMockUser(username = TESTER)
    void shouldReturnFirmDefaultPatterns() {
        var firm = firmService.getFirmWithDefaultPatterns();

        assertEquals(4, firm.getDefaultPatterns().size());
    }

    @Test
    @WithMockUser(username = TESTER)
    void shouldRemoveFirmDefaultPattern() {
        var firm = firmService.getFirmWithDefaultPatterns();
        var pattern = patternService.load(5083);

        assertEquals(4, firm.getDefaultPatterns().size());
        assertTrue(firm.getDefaultPatterns().contains(pattern));
        firmService.deleteFirmWithDefaultPattern(pattern);

        assertEquals(3, firm.getDefaultPatterns().size());
        assertFalse(firm.getDefaultPatterns().contains(pattern));
        pattern = patternService.load(5083);

        assertNotNull(pattern);
    }

    @Test
    @WithMockUser(username = TESTER)
    void shouldReturnFirmCustomPatterns() {
        var patterns = firmService.getFirmWithCustomPatterns().getPatterns();

        assertEquals(7, patterns.size());
    }

    @Test
    @WithMockUser(username = TESTER)
    void shouldRemoveFirmCustomerPattern() {
        var pattern = patternService.load(5097);
        firmService.deleteFirmWithCustomPattern(pattern);
        var patterns = firmService.getFirmWithCustomPatterns().getPatterns();

        assertEquals(6, patterns.size());
        assertThrows(EntityNotFoundException.class, () -> {
            patternService.load(5097);
        });
    }

    @Test
    @WithMockUser(username = TESTER)
    void shouldCopyDefaultPatterns() {
        var pattern = patternService.load(5196);
        var firm = firmService.load(1);

        assertFalse(firm.getPatterns().contains(pattern));
        firmService.copyDefaultPatterns(firm.getId());

        assertTrue(firm.getPatterns().contains(pattern));
    }
}
