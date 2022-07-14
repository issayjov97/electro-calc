package com.example.application.mapper;

import com.example.application.dto.PatternDTO;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.entity.UserEntity;

public class PatternMapper {

    public static PatternDTO convertToDTO(PatternEntity source) {
        final PatternDTO patternDTO = new PatternDTO();
        patternDTO.setId(source.getId());
        patternDTO.setName(source.getName());
        patternDTO.setDescription(source.getDescription());
        patternDTO.setDuration(source.getDuration());
        patternDTO.setPriceWithoutVat(source.getPriceWithoutVat());
        return patternDTO;
    }

    public static PatternEntity convertToEntity(PatternDTO source) {
        final PatternEntity patternEntity = new PatternEntity();
        patternEntity.setId(source.getId());
        patternEntity.setName(source.getName());
        patternEntity.setDescription(source.getDescription());
        patternEntity.setDuration(source.getDuration());
        patternEntity.setPriceWithoutVat(source.getPriceWithoutVat());
        return patternEntity;
    }

    public static PatternEntity convertToEntity(PatternEntity source, UserEntity userEntity) {
        final PatternEntity patternEntity = new PatternEntity();
        patternEntity.setName(source.getName());
        patternEntity.setDescription(source.getDescription());
        patternEntity.setDuration(source.getDuration());
        patternEntity.setPriceWithoutVat(source.getPriceWithoutVat());
        patternEntity.setUserEntity(userEntity);
        return patternEntity;
    }
}
