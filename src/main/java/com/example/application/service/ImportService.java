package com.example.application.service;

import com.example.application.persistence.entity.PatternEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

@Service
public class ImportService {
    private final        FirmService    firmService;
    private final        PatternService patternService;
    private static final Logger         logger = LoggerFactory.getLogger(ImportService.class);

    public ImportService(FirmService firmService, PatternService patternService) {
        this.firmService = firmService;
        this.patternService = patternService;
    }

    public void importPatterns(InputStream inputStream) {
        try (Scanner scanner = new Scanner(inputStream, "Windows-1250")) {
            scanner.nextLine(); //skip header
            while (scanner.hasNextLine()) {
                var parsedPattern = getRecordFromLine(scanner.nextLine());
                var tmp = patternService.getPattern(parsedPattern.getName());
                if (tmp != null) {
                    tmp.setPriceWithoutVAT(parsedPattern.getPriceWithoutVAT());
                    tmp.setDuration(parsedPattern.getDuration());
                    tmp.setDescription(parsedPattern.getDescription());
                    patternService.save(tmp);
                } else
                    patternService.save(parsedPattern);
            }
        }
    }

    private PatternEntity getRecordFromLine(String line) {
        var patternEntity = new PatternEntity();
        logger.info("PATTERN TO PARSE: {}", line);
        String[] tmp = line.split(",");
        String name = tmp[0];
        String description = tmp[1];
        Double duration = Double.parseDouble(tmp[2]);
        BigDecimal priceWithoutVAT = new BigDecimal(tmp[3]);
        long id = Long.parseLong(tmp[4]);
        patternEntity.setName(name);
        patternEntity.setDescription(description);
        patternEntity.setDuration(duration);
        patternEntity.setPriceWithoutVAT(priceWithoutVAT);
        if (id > 0) {
            patternEntity.setFirmEntity(firmService.load(id));
        }
        return patternEntity;
    }
}
