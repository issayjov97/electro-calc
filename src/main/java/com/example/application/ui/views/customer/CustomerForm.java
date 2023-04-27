package com.example.application.ui.views.customer;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.customer.events.DeleteEvent;
import com.example.application.ui.views.customer.events.SaveEvent;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;

public class CustomerForm extends AbstractForm<CustomerEntity> {
    private final TextField nameField = new TextField("Název");
    private final TextArea emailField = new TextArea("Email");
    private final TextField phoneField = new TextField("Telefonní číslo");
    private final TextArea note = new TextArea("Poznámka");

    public CustomerForm() {
        super(new BeanValidationBinder<>(CustomerEntity.class));
        setBinder();
        dialog.add(createDialogLayout());
        nameField.setSizeFull();
        emailField.setSizeFull();
        phoneField.setSizeFull();
    }

    @Override
    protected void setBinder() {
        binder.forField(nameField).asRequired("Název je povinný")
                .withValidator(
                        name -> name.length() >= 2,
                        "Min 2 znáků "
                )
                .bind(CustomerEntity::getName, CustomerEntity::setName);

        binder.forField(emailField).asRequired("Email je povinný")
                .withValidator(new EmailValidator(
                        "Email je nevalidní"))
                .bind(CustomerEntity::getEmail, CustomerEntity::setEmail);

        binder.forField(phoneField)
                .bind(CustomerEntity::getPhone, CustomerEntity::setPhone);

        binder.forField(note)
                .bind(CustomerEntity::getNote, CustomerEntity::setNote);
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(nameField, emailField, phoneField, note);
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

    public TextArea getEmailField() {
        return emailField;
    }

    public TextField getPhoneField() {
        return phoneField;
    }
}
