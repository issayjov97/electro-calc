package com.example.application.ui.views.admin;

import com.example.application.dto.UserDTO;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;


public class UserForm extends Div {
    Binder<UserDTO> binder = new BeanValidationBinder<>(UserDTO.class);
    private       List<String>  allAuthorities = new ArrayList<>();
    private final Dialog        dialog         = new Dialog();
    private       TextField     username       = new TextField("Username");
    private       TextArea      firstName      = new TextArea("First name");
    private       TextField     lastName       = new TextField("Last name");
    private       EmailField    email          = new EmailField("Email");
    private       PasswordField password       = new PasswordField("Password");
    private       Checkbox      enabled        = new Checkbox("Enabled");
    private       Button        saveButton     = new Button("Save");
    private       Button        deleteButton   = new Button("Delete");
    private       Button        cancelButton   = new Button("Cancel");
    private       H2            headline       = new H2();
    CheckboxGroup<String> authorities = new CheckboxGroup<>();

    private UserDTO userDTO;

    public UserForm() {
        authorities.setLabel("Authorities");
        authorities.setSizeFull();
        authorities.addSelectionListener(e -> {
            if (!e.getAllSelectedItems().isEmpty()) {
                userDTO.setAuthorities(e.getAllSelectedItems());
                // authorities.select(Set.of(e.getAllSelectedItems().toString().split(",")));
                System.out.println(userDTO.getAuthorities());
            }
        });
        setBinder();
        dialog.add(createDialogLayout());
    }

    private void setBinder() {
        binder.forField(username).asRequired("Username is required")
                .withValidator(
                        name -> name.length() >= 6,
                        "Name must contain at least 6 characters"
                )
                .bind(UserDTO::getUsername, UserDTO::setUsername);

        binder.forField(firstName).asRequired("First name is required")
                .withValidator(
                        name -> name.length() >= 3,
                        "First name must contain at least 6 characters"
                )
                .bind(UserDTO::getFirstName, UserDTO::setFirstName);

        binder.forField(lastName).asRequired("Last name is required")
                .withValidator(
                        name -> name.length() >= 3,
                        " Last name must contain at least 6 characters"
                )
                .bind(UserDTO::getLastName, UserDTO::setLastName);

        binder.forField(email).asRequired("Email is required")
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(UserDTO::getEmail, UserDTO::setEmail);

        binder.forField(password).asRequired("Password is required")
                .withValidator(
                        password -> password.length() >= 8,
                        "Last name must contain at least 6 characters"
                )
                .bind(UserDTO::getPassword, UserDTO::setPassword);
        binder.forField(enabled)
                .bind(UserDTO::getEnabled, UserDTO::setEnabled);
    }

    private VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(username, firstName, lastName, email, password, authorities, enabled);
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
        this.headline.setText(title);
        authorities.setItems(allAuthorities);
        if (userDTO != null && userDTO.getAuthorities() != null)
            authorities.select(userDTO.getAuthorities());
        password.setEnabled(true);
        if (userDTO != null && userDTO.getPassword() != null)
            password.setEnabled(false);
        dialog.open();
    }

    public void setAllAuthorities(List<String> allAuthorities) {
        this.allAuthorities = allAuthorities;
    }

    public void close() {
        dialog.close();
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
        binder.readBean(userDTO);
    }

    private HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, userDTO)));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this)));
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    private void validateAndSave() {
        try {
            binder.writeBean(userDTO);
            fireEvent(new SaveEvent(this, userDTO));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private UserDTO item;

        protected UserFormEvent(UserForm source, UserDTO item) {
            super(source, false);
            this.item = item;
        }

        public UserDTO getItem() {
            return item;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, UserDTO contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, UserDTO contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends UserFormEvent {
        CloseEvent(UserForm source) {
            super(source, null);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
