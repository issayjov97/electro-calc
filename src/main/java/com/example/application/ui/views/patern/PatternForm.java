package com.example.application.ui.views.patern;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.patern.events.DeleteEvent;
import com.example.application.ui.views.patern.events.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;

public class PatternForm extends AbstractForm<PatternEntity> {
    private final TextField nameField = new TextField("Název");
    private final TextArea descriptionField = new TextArea("Popis");
    private final NumberField durationField = new NumberField("Delka práce (min)");
    private final BigDecimalField priceWithoutVatField = new BigDecimalField("Cena bez DPH (Kč)");

    public PatternForm() {
        super(new BeanValidationBinder<>(PatternEntity.class));
        setBinder();
        dialog.add(getHeadline(), createDialogLayout(), createButtonsLayout());
    }

    @Override
    protected void setBinder() {
        binder.forField(nameField).asRequired("Nazev je povinny")
                .withValidator(
                        name -> name.length() >= 3,
                        "Název musí obsahovat alespoň 3 znaků"
                )
                .bind(PatternEntity::getName, PatternEntity::setName);

        binder.forField(descriptionField)
                .bind(PatternEntity::getDescription, PatternEntity::setDescription);

        binder.forField(durationField).asRequired("Delka práce je povinná")
                .bind(PatternEntity::getDuration, PatternEntity::setDuration);

        binder.forField(priceWithoutVatField).asRequired("Cena je povinná")
                .bind(PatternEntity::getPriceWithoutVAT, PatternEntity::setPriceWithoutVAT);
    }

    @Override
    protected Component createDialogLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("200px", 2)
        );
        formLayout.setColspan(nameField, 2);
        formLayout.setColspan(descriptionField, 2);
        formLayout.setMaxWidth("400px");
        formLayout.add(nameField, descriptionField, durationField, priceWithoutVatField);
        return formLayout;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        HorizontalLayout buttonsMenu = new HorizontalLayout();
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, getEntity())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this, false)));
        buttonsMenu.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        buttonsMenu.add(saveButton, deleteButton, cancelButton);
        return buttonsMenu;
    }

    @Override
    protected void validateAndSave() {
        try {
            binder.writeBean(getEntity());
            fireEvent(new SaveEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public TextField getNameField() {
        return nameField;
    }

    public TextArea getDescriptionField() {
        return descriptionField;
    }

    public NumberField getDurationField() {
        return durationField;
    }

    public BigDecimalField getPriceWithoutVatField() {
        return priceWithoutVatField;
    }
}
