package com.example.application.mapper;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.entity.UserEntity;

public class PatternMapper {

    public static PatternEntity convertToEntity(PatternEntity source, FirmEntity firmEntity) {
        final PatternEntity patternEntity = new PatternEntity();
        patternEntity.setName(source.getName());
        patternEntity.setDescription(source.getDescription());
        patternEntity.setDuration(source.getDuration());
        patternEntity.setPriceWithoutVAT(source.getPriceWithoutVAT());
        patternEntity.setMeasureUnit(source.getMeasureUnit());
        patternEntity.setFirmEntity(firmEntity);
        return patternEntity;
    }
}