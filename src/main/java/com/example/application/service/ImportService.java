package com.example.application.service;

import com.example.application.persistence.entity.PatternEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Service
public class ImportService {

    private static final Logger logger = LoggerFactory.getLogger(ImportService.class);
    private final FirmService firmService;
    private final PatternService patternService;
    private static final List<String> SKIP_TEXT = List.of("NULL");

    public ImportService(FirmService firmService, PatternService patternService) {
        this.firmService = firmService;
        this.patternService = patternService;
    }

    public void importPatterns(InputStream inputStream) {
        List<PatternEntity> patternEntityList = new ArrayList<>();
        try (Scanner scanner = new Scanner(inputStream, "Windows-1250")) {
            scanner.nextLine(); //skip header
            while (scanner.hasNextLine()) {
                var parsedPattern = getRecordFromLine(scanner.nextLine());
                var currentPattern = patternService.getPattern(parsedPattern);
                if (currentPattern != null) {
                    currentPattern.setPriceWithoutVAT(parsedPattern.getPriceWithoutVAT());
                    currentPattern.setDuration(parsedPattern.getDuration());
                    currentPattern.setDescription(parsedPattern.getDescription());
                    patternEntityList.add(currentPattern);
                } else
                    patternEntityList.add(parsedPattern);
            }
            patternService.saveAll(patternEntityList);
            firmService.enableCopyDefaultPatternsFeature();
        }
    }

    private PatternEntity getRecordFromLine(String line) throws EntityNotFoundException {
        var patternEntity = new PatternEntity();
        logger.info("PATTERN TO PARSE: {}", line);
        String[] tmp = line.split(",");
        String name = tmp[0];
        String description = tmp[1];
        double duration = Math.ceil(Double.parseDouble(tmp[2]) * 60);
        BigDecimal priceWithoutVAT = new BigDecimal(tmp[3]);
        long id = Long.parseLong(tmp[4]);
        patternEntity.setName(name);
        patternEntity.setDescription(SKIP_TEXT.contains(description) ? null : description);
        patternEntity.setDuration(duration);
        patternEntity.setPriceWithoutVAT(priceWithoutVAT);
        if (id > 0) {
            patternEntity.setFirmEntity(firmService.load(id));
        }
        return patternEntity;
    }
}
