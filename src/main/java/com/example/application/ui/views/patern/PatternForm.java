package com.example.application.ui.views.patern;

import com.example.application.dto.PatternDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class PatternForm extends Div {
    Binder<PatternDTO> binder = new BeanValidationBinder<>(PatternDTO.class);
    private final Dialog          dialog               = new Dialog();
    private       TextField       nameField            = new TextField("Name");
    private       TextArea        descriptionField     = new TextArea("Description");
    private       NumberField     durationField        = new NumberField("Duration");
    private       BigDecimalField priceWithoutVatField = new BigDecimalField("Price without VAT");
    private       Button          saveButton           = new Button("Save");
    private       Button          deleteButton         = new Button("Delete");
    private       Button          cancelButton         = new Button("Cancel");
    private       PatternDTO      patternDTO;
    private       H2              headline             = new H2();

    public PatternForm() {
        setBinder();
        dialog.getElement().setAttribute("aria-label", "Create new pattern");
        dialog.add(createDialogLayout());
        nameField.setSizeFull();
        descriptionField.setSizeFull();
        durationField.setSizeFull();
        priceWithoutVatField.setWidthFull();
    }

    private void setBinder() {
        binder.forField(nameField).asRequired("Name is required")
                .withValidator(
                        name -> name.length() >= 6,
                        "Name must contain at least 6 characters "
                )
                .bind(PatternDTO::getName, PatternDTO::setName);

        binder.bind(descriptionField, PatternDTO::getDescription, PatternDTO::setDescription);

        binder.forField(durationField).asRequired("Duration is required")
                .bind(PatternDTO::getDuration, PatternDTO::setDuration);

        binder.forField(priceWithoutVatField).asRequired("Price without VAT is required")
                .bind(PatternDTO::getPriceWithoutVat, PatternDTO::setPriceWithoutVat);
    }

    private VerticalLayout createDialogLayout() {
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

    public void open(String title) {
        this.headline.setText(title);
        dialog.open();
    }

    public void close() {
        dialog.close();
    }

    public void setPatternDTO(PatternDTO patternDTO) {
        this.patternDTO = patternDTO;
        binder.readBean(patternDTO);
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, patternDTO)));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        binder.addStatusChangeListener(e -> saveButton.setEnabled(binder.isValid()));
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(patternDTO);
            System.out.println("Validated item: " + patternDTO);
            fireEvent(new SaveEvent(this, patternDTO));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class PatternFormEvent extends ComponentEvent<PatternForm> {
        private PatternDTO item;

        protected PatternFormEvent(PatternForm source, PatternDTO item) {
            super(source, false);
            this.item = item;
        }

        public PatternDTO getItem() {
            return item;
        }
    }

    public static class SaveEvent extends PatternFormEvent {
        SaveEvent(PatternForm source, PatternDTO contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends PatternFormEvent {
        DeleteEvent(PatternForm source, PatternDTO contact) {
            super(source, contact);
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
}
