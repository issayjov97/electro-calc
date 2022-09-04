package com.example.application.utils;

import com.vaadin.flow.component.html.Label;

public class UIUtils {

    public static Label createDPHPrice(String price, boolean vatable) {
        Label amountLabel = new Label(price);
        if (vatable)
            amountLabel.getStyle().set("color", "var(--lumo-error-text-color)");
        else
            amountLabel.getStyle().set("color", "var(--lumo-success-text-color)");
        return amountLabel;
    }

    public static void main(String[] args) {
    }
}
