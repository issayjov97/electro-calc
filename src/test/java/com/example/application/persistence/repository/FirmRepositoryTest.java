package com.example.application.persistence.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest(excludeAutoConfiguration = EmbeddedDatabase.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class FirmRepositoryTest {

    @Autowired
    private FirmRepository firmRepository;

    @Test
    void shouldReturnFirmDefaultPatterns() {
        final long firmId = 1L;

        var result = firmRepository.findWithDefaultPatternsById(firmId);

        assertTrue(result.getDefaultPatterns().size() > 0);
        assertTrue(result.getDefaultPatterns().stream().allMatch(it -> it.getFirmEntity() == null));
    }

    @Test
    void shouldReturnFirmWithCustomPatterns() {
        final long firmId = 1L;

        var result = firmRepository.findWithCustomPatternsById(firmId);

        assertTrue(result.getDefaultPatterns().size() > 0);
        assertTrue(result.getPatterns().stream().allMatch(it -> it.getFirmEntity().getId() == firmId));
    }
}