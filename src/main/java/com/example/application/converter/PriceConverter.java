package com.example.application.converter;

import com.example.application.utils.FormattingUtils;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class PriceConverter implements Converter<BigDecimal, String> {

    @Override
    public String convert(BigDecimal value) {
        return FormattingUtils.formatAsCurrency(value);
    }
}
