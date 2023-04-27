package com.example.application.ui.views.offer;

import com.example.application.ui.views.AbstractForm;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;

@CssImport(value = "./views/menu-bar.css", themeFor = "vaadin-menu-bar")
public class OfferPatternForm extends AbstractForm {
    private final Checkbox isCabelCrossSection = new Checkbox("Započítat prořez");
    final IntegerField count = new IntegerField("Počet");

    public OfferPatternForm() {
        super(null);
        count.setWidthFull();
        isCabelCrossSection.setSizeFull();
        dialog.add(headline, createDialogLayout(), createButtonsLayout());
    }

    @Override
    protected void setBinder() {
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.add(headline, count, isCabelCrossSection);
        count.setValue(1);
        count.setStep(1);
        count.setMin(1);
        count.setMax(100);
        count.setHasControls(true);
        return verticalLayout;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        var horizontalLayout = new HorizontalLayout(saveButton);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        saveButton.addClickListener(event -> validateAndSave());
        return horizontalLayout;
    }

    @Override
    protected void validateAndSave() {
    }

    public Checkbox getIsCabelCrossSection() {
        return isCabelCrossSection;
    }

    public IntegerField getCount() {
        return count;
    }
}