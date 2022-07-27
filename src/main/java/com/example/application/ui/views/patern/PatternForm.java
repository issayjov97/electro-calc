package com.example.application.ui.views.patern;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.ui.views.AbstractForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class PatternForm extends AbstractForm<PatternEntity> {
    private final TextField       nameField            = new TextField("Name");
    private final TextArea        descriptionField     = new TextArea("Description");
    private final NumberField     durationField        = new NumberField("Duration");
    private final BigDecimalField priceWithoutVatField = new BigDecimalField("Price without VAT");

    public PatternForm() {
        super(new BeanValidationBinder<>(PatternEntity.class));
        setBinder();
        dialog.getElement().setAttribute("aria-label", "Create new pattern");
        dialog.add(createDialogLayout());
        nameField.setSizeFull();
        descriptionField.setSizeFull();
        durationField.setSizeFull();
        priceWithoutVatField.setWidthFull();
    }

    @Override
    protected void setBinder() {
        binder.forField(nameField).asRequired("Name is required")
                .withValidator(
                        name -> name.length() >= 6,
                        "Name must contain at least 6 characters "
                )
                .bind(PatternEntity::getName, PatternEntity::setName);

        binder.bind(descriptionField, PatternEntity::getDescription, PatternEntity::setDescription);

        binder.forField(durationField).asRequired("Duration is required")
                .bind(PatternEntity::getDuration, PatternEntity::setDuration);

        binder.forField(priceWithoutVatField).asRequired("Price without VAT is required")
                .bind(PatternEntity::getPriceWithoutVAT, PatternEntity::setPriceWithoutVAT);
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(nameField, descriptionField, durationField, priceWithoutVatField);
        fieldLayout.setSpacing(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout buttonLayout = createButtonsLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        return dialogLayout;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, getEntity())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    @Override
    protected void validateAndSave() {
        try {
            binder.writeBean(getEntity());
            System.out.println("Validated item: " + getEntity());
            fireEvent(new SaveEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class PatternFormEvent extends ComponentEvent<PatternForm> {
        private final PatternEntity item;

        protected PatternFormEvent(PatternForm source, PatternEntity item) {
            super(source, false);
            this.item = item;
        }

        public PatternEntity getItem() {
            return item;
        }
    }

    public static class SaveEvent extends PatternFormEvent {
        SaveEvent(PatternForm source, PatternEntity item) {
            super(source, item);
        }
    }

    public static class DeleteEvent extends PatternFormEvent {
        DeleteEvent(PatternForm source, PatternEntity item) {
            super(source, item);
        }
    }

    public static class CloseEvent extends PatternFormEvent {
        CloseEvent(PatternForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
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
