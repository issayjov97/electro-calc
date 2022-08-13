package com.example.application.ui.views.customer;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.customer.events.DeleteEvent;
import com.example.application.ui.views.customer.events.SaveEvent;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;

public class CustomerForm extends AbstractForm<CustomerEntity> {
    private final TextField      nameField    = new TextField("Name");
    private final TextArea       emailField   = new TextArea("Email");
    private final TextField      phoneField   = new TextField("Phone");

    public CustomerForm() {
        super(new BeanValidationBinder<>(CustomerEntity.class));
        setBinder();
        dialog.getElement().setAttribute("aria-label", "Add new customer");
        dialog.add(createDialogLayout());
        nameField.setSizeFull();
        emailField.setSizeFull();
        phoneField.setSizeFull();
    }

    @Override
    protected void setBinder() {
        binder.forField(nameField).asRequired("Name is required")
                .withValidator(
                        name -> name.length() >= 3,
                        "Name must contain at least 3 characters "
                )
                .bind(CustomerEntity::getName, CustomerEntity::setName);

        binder.forField(emailField).asRequired("Email is required")
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(CustomerEntity::getEmail, CustomerEntity::setEmail);

        binder.forField(phoneField)
                .bind(CustomerEntity::getPhone, CustomerEntity::setPhone);
    }


    @Override
    protected VerticalLayout createDialogLayout() {
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

    @Override
    protected HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, getEntity())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this, false)));
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
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

    public TextArea getEmailField() {
        return emailField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }
}
