package com.example.application.util;

import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.html.Label;

public class UIUtils {

    public static Label createLabel(String value, SolidColor color) {
        Label amountLabel = new Label(value);
        if (color == SolidColor.RED)
            amountLabel.getStyle().set("color", "var(--lumo-error-text-color)");
        else if (color == SolidColor.GREEN)
            amountLabel.getStyle().set("color", "var(--lumo-success-text-color)");
        else if (color == SolidColor.BLUE)
            amountLabel.getStyle().set("color", "var(--lumo-primary-color)");
        else if (color == SolidColor.YELLOW)
            amountLabel.getStyle().set("color", "rgb(255 141 0 / 98%)");
        return amountLabel;
    }
}
