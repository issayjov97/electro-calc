package com.example.application.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class FormattingUtils {

    public static final Locale APP_LOCALE = Locale.forLanguageTag("cs-CZ");

    public static final String DECIMAL_ZERO = "0.00";

    public static final DateTimeFormatter MONTH_AND_DAY_FORMATTER = DateTimeFormatter.ofPattern("MMM d",APP_LOCALE);

    public static String formatAsCurrency(BigDecimal value) {
        return NumberFormat.getCurrencyInstance((APP_LOCALE)).format(value.setScale(2, RoundingMode.HALF_UP));
    }

    public static DecimalFormat getUiPriceFormatter() {
        DecimalFormat formatter = new DecimalFormat("#" + DECIMAL_ZERO,
                DecimalFormatSymbols.getInstance((APP_LOCALE)));
        formatter.setGroupingUsed(false);
        return formatter;
    }

}