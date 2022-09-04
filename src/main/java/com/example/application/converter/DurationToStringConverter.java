package com.example.application.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DurationToStringConverter implements Converter<Double, String> {
    @Override
    public String convert(Double duration) {
        String tmp = "";
        int hours = (int) (duration / 60);
        int minutes = (int) (duration % 60);
        if (hours > 0)
            tmp += hours + "h ";
        tmp += minutes + "min";
        return tmp;
    }
}
