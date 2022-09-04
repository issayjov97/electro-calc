package com.example.application.ui.views.settings.admin.user;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.FirmService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.settings.admin.user.events.DeleteEvent;
import com.example.application.ui.views.settings.admin.user.events.SaveEvent;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;

import java.util.ArrayList;
import java.util.List;


public class UserForm extends AbstractForm<UserEntity> {
    private       List<AuthorityEntity>          allAuthorities = new ArrayList<>();
    private final TextField                      username       = new TextField("Uživatelské jméno");
    private final TextField                       firstName      = new TextField("Jméno");
    private final TextField                      lastName       = new TextField("Přijmení");
    private final EmailField                     email          = new EmailField("Email");
    private final PasswordField        password = new PasswordField("Heslo");
    private final ComboBox<FirmEntity> firms    = new ComboBox<>();
    private final Checkbox             enabled  = new Checkbox("Povoleno");
    private final CheckboxGroup<AuthorityEntity> authorities    = new CheckboxGroup<>();
    private final FirmService                    firmService;

    public UserForm(FirmService firmService) {
        super(new BeanValidationBinder<>(UserEntity.class));
        this.firmService = firmService;
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
        binder.forField(username).asRequired("Uživatelské jméno je povinné")
                .withValidator(
                        name -> name.length() >= 6,
                        "Min 6 znáků"
                )
                .bind(UserEntity::getUsername, UserEntity::setUsername);

        binder.forField(firstName).asRequired("Jméno jméno je povinné")
                .withValidator(
                        name -> name.length() >= 3,
                        "Min 3 znáků"
                )
                .bind(UserEntity::getFirstName, UserEntity::setFirstName);

        binder.forField(lastName).asRequired("Přijmení jméno je povinné")
                .withValidator(
                        name -> name.length() >= 3,
                        "Min 3 znáků"
                )
                .bind(UserEntity::getLastName, UserEntity::setLastName);

        binder.forField(email).asRequired("Email je povinný")
                .withValidator(new EmailValidator(
                        "Email není validní, zkuste jiný"))
                .bind(UserEntity::getEmail, UserEntity::setEmail);

        binder.forField(password).asRequired("Heslo je povinné")
                .withValidator(
                        password -> password.length() >= 8,
                        "Min 8 znáků"
                )
                .bind(UserEntity::getPassword, UserEntity::setPassword);
        binder.forField(enabled)
                .bind(UserEntity::getEnabled, UserEntity::setEnabled);

        binder.forField(firms).asRequired("Údaje o firmě jsou povinná")
                .bind(UserEntity::getFirmEntity, UserEntity::setFirmEntity);
        binder.forField(authorities).asRequired("Role je povinná")
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
        firms.setLabel("Firmy");
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

    public TextField getUsername() {
        return username;
    }

    public TextField getFirstName() {
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
