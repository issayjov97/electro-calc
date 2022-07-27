package com.example.application.ui.views.admin;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.ui.views.AbstractForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;


public class FirmForm extends AbstractForm<FirmEntity> {
    private final TextField  name     = new TextField("Name");
    private final TextField  street   = new TextField("Street");
    private final TextField  postCode = new TextField("Post code");
    private final TextField  state    = new TextField("State");
    private final TextField  city     = new TextField("City");
    private final TextField  CIN      = new TextField("CIN");
    private final TextField  VATIN    = new TextField("VATIN");
    private final TextField  phone    = new TextField("Phone");
    private final EmailField email    = new EmailField("Email");

    public FirmForm() {
        super(new BeanValidationBinder<>(FirmEntity.class));
        setBinder();
        dialog.add(createDialogLayout(), createButtonsLayout());
    }


    @Override
    protected void setBinder() {
        binder.forField(name).asRequired("Name is required")
                .withValidator(
                        name -> name.length() >= 4,
                        "Name must contain at least 6 characters"
                ).bind(FirmEntity::getName, FirmEntity::setName);
        binder.bind(street,"street");
        binder.bind(postCode,"postCode");
        binder.bind(state,"state");
        binder.bind(city,"city");
        binder.bind(CIN,"CIN");
        binder.bind(VATIN,"VATIN");
        binder.bind(phone,"phone");
        binder.bind(email,"email");
    }

    @Override
    protected Component createDialogLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(name, street, postCode, city, state, CIN, VATIN, phone, email);
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("200px", 2)
        );
        formLayout.setColspan(name, 2);
        formLayout.setColspan(street, 2);
        formLayout.setColspan(state, 2);
        formLayout.setMaxWidth("600px");
        return formLayout;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        final HorizontalLayout buttonsMenu = new HorizontalLayout();
        buttonsMenu.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, getEntity())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
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

    public static abstract class FirmFormEvent extends ComponentEvent<FirmForm> {
        private final FirmEntity item;

        protected FirmFormEvent(FirmForm source, FirmEntity item) {
            super(source, false);
            this.item = item;
        }

        public FirmEntity getItem() {
            return item;
        }
    }

    public static class SaveEvent extends FirmFormEvent {
        SaveEvent(FirmForm source, FirmEntity contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends FirmFormEvent {
        DeleteEvent(FirmForm source, FirmEntity contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends FirmFormEvent {
        CloseEvent(FirmForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
