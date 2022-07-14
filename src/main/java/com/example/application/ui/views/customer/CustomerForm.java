package com.example.application.ui.views.customer;

import com.example.application.dto.CustomerDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;

public class CustomerForm extends Div {
    Binder<CustomerDTO> binder = new BeanValidationBinder<>(CustomerDTO.class);
    private final Dialog    dialog    = new Dialog();
    private       TextField nameField  = new TextField("Name");
    private       TextArea  emailField = new TextArea("Email");
    private       TextField phoneField = new TextField("Phone");
    private       Button    saveButton = new Button("Save");
    private       Button      deleteButton = new Button("Delete");
    private       Button      cancelButton = new Button("Cancel");
    H2 headline = new H2();
    private CustomerDTO customerDTO;

    public CustomerForm() {
        setBinder();
        dialog.getElement().setAttribute("aria-label", "Add new customer");
        dialog.add(createDialogLayout());
        nameField.setSizeFull();
        emailField.setSizeFull();
        phoneField.setSizeFull();
    }

    private void setBinder() {
        binder.forField(nameField).asRequired("Name is required")
                .withValidator(
                        name -> name.length() >= 3,
                        "Name must contain at least 3 characters "
                )
                .bind(CustomerDTO::getName, CustomerDTO::setName);

        binder.forField(emailField).asRequired("Email is required")
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(CustomerDTO::getEmail, CustomerDTO::setEmail);

        binder.forField(phoneField)
                .bind(CustomerDTO::getPhone, CustomerDTO::setPhone);
    }

    private VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(nameField, emailField, phoneField);
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
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
        headline.setText(title);
        dialog.open();
    }

    public void close() {
        dialog.close();
    }

    public void setCustomerDTO(CustomerDTO customer) {
        this.customerDTO = customer;
        binder.readBean(customer);
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, customerDTO)));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(customerDTO);
            fireEvent(new SaveEvent(this, customerDTO));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class CustomerFormEvent extends ComponentEvent<CustomerForm> {
        private CustomerDTO item;

        protected CustomerFormEvent(CustomerForm source, CustomerDTO item) {
            super(source, false);
            this.item = item;
        }

        public CustomerDTO getItem() {
            return item;
        }
    }

    public static class SaveEvent extends CustomerFormEvent {
        SaveEvent(CustomerForm source, CustomerDTO contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends CustomerFormEvent {
        DeleteEvent(CustomerForm source, CustomerDTO contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends CustomerFormEvent {
        CloseEvent(CustomerForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
