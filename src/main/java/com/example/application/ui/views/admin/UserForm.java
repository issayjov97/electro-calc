package com.example.application.ui.views.admin;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.FirmService;
import com.example.application.ui.views.AbstractForm;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;


public class UserForm extends AbstractForm<UserEntity> {
    private       List<AuthorityEntity> allAuthorities = new ArrayList<>();
    private final TextField             username       = new TextField("Username");
    private final TextArea              firstName      = new TextArea("First name");
    private final TextField             lastName       = new TextField("Last name");
    private final EmailField            email          = new EmailField("Email");
    private final     PasswordField  password        = new PasswordField("Password");
    private final Select<FirmEntity> firms = new Select<>();
    private final Checkbox           enabled         = new Checkbox("Enabled");
    private final CheckboxGroup<AuthorityEntity> authorities = new CheckboxGroup<>();
    private final FirmService                    firmService;
    public UserForm(FirmService firmService) {
        super(new BeanValidationBinder<>(UserEntity.class));
        this.firmService = firmService;
        authorities.setLabel("Authorities");
        authorities.setSizeFull();
        authorities.setItemLabelGenerator(AuthorityEntity::getName);
        authorities.addSelectionListener(e -> {
            if (!e.getAllSelectedItems().isEmpty()) {
                getEntity().setAuthorityEntities(e.getAllSelectedItems());
            }
        });
        setBinder();
        dialog.add(createDialogLayout());
        configureSelect();
    }

    @Override
    protected void setBinder() {
        binder.forField(username).asRequired("Username is required")
                .withValidator(
                        name -> name.length() >= 6,
                        "Name must contain at least 6 characters"
                )
                .bind(UserEntity::getUsername, UserEntity::setUsername);

        binder.forField(firstName).asRequired("First name is required")
                .withValidator(
                        name -> name.length() >= 3,
                        "First name must contain at least 6 characters"
                )
                .bind(UserEntity::getFirstName, UserEntity::setFirstName);

        binder.forField(lastName).asRequired("Last name is required")
                .withValidator(
                        name -> name.length() >= 3,
                        " Last name must contain at least 6 characters"
                )
                .bind(UserEntity::getLastName, UserEntity::setLastName);

        binder.forField(email).asRequired("Email is required")
                .withValidator(new EmailValidator(
                        "This doesn't look like a valid email address"))
                .bind(UserEntity::getEmail, UserEntity::setEmail);

        binder.forField(password).asRequired("Password is required")
                .withValidator(
                        password -> password.length() >= 8,
                        "Last name must contain at least 6 characters"
                )
                .bind(UserEntity::getPassword, UserEntity::setPassword);
        binder.forField(enabled)
                .bind(UserEntity::getEnabled, UserEntity::setEnabled);

        binder.forField(firms).asRequired("Firm is required")
                .bind(UserEntity::getFirmEntity, UserEntity::setFirmEntity);
        binder.forField(authorities).asRequired("Authority is required")
                .bind(UserEntity::getAuthorityEntities, UserEntity::setAuthorityEntities);
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(username, firstName, lastName, email, password, firms, authorities, enabled);
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
        authorities.setItems(allAuthorities);
        if (getEntity() != null && getEntity().getAuthorityEntities() != null)
            authorities.select(getEntity().getAuthorityEntities());
        password.setEnabled(true);
        if (getEntity() != null && getEntity().getPassword() != null)
            password.setEnabled(false);
        super.open(title);
    }

    private void configureSelect() {
        firms.setLabel("Firms");
        firms.addClassName("label");
        firms.setItemLabelGenerator(FirmEntity::getName);
        firms.setItems(firmService.loadAll());
    }

    public void setAllAuthorities(List<AuthorityEntity> allAuthorities) {
        this.allAuthorities = allAuthorities;
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
            fireEvent(new SaveEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public static abstract class UserFormEvent extends ComponentEvent<UserForm> {
        private final UserEntity item;

        protected UserFormEvent(UserForm source, UserEntity item) {
            super(source, false);
            this.item = item;
        }

        public UserEntity getItem() {
            return item;
        }
    }

    public static class SaveEvent extends UserFormEvent {
        SaveEvent(UserForm source, UserEntity contact) {
            super(source, contact);
        }
    }

    public static class DeleteEvent extends UserFormEvent {
        DeleteEvent(UserForm source, UserEntity contact) {
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

    public TextField getUsername() {
        return username;
    }

    public TextArea getFirstName() {
        return firstName;
    }

    public TextField getLastName() {
        return lastName;
    }

    public EmailField getEmail() {
        return email;
    }

    public PasswordField getPassword() {
        return password;
    }

    public Checkbox getEnabled() {
        return enabled;
    }

    public CheckboxGroup<AuthorityEntity> getAuthorities() {
        return authorities;
    }

    public List<AuthorityEntity> getAllAuthorities() {
        return allAuthorities;
    }
}
