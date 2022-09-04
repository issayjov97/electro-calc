package com.example.application.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToLongConverter implements Converter<String, Long> {

    @Override
    public Long convert(String value) {
        return Long.valueOf(value);
    }
}
