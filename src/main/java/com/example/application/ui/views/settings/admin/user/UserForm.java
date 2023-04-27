package com.example.application.ui.views.settings.admin.user;

import com.example.application.persistence.entity.AuthorityEntity;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.persistence.entity.UserEntity;
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
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;

import java.util.ArrayList;
import java.util.List;


public class UserForm extends AbstractForm<UserEntity> {
    private List<AuthorityEntity> allAuthorities = new ArrayList<>();
    private final TextField username = new TextField("Uživatelské jméno");
    private final TextField firstName = new TextField("Jméno");
    private final TextField lastName = new TextField("Přijmení");
    private final EmailField email = new EmailField("Email");
    private final PasswordField password = new PasswordField("Heslo");
    private final ComboBox<FirmEntity> firmsSelect = new ComboBox<>();
    private final Checkbox enabled = new Checkbox("Povoleno");
    private final CheckboxGroup<AuthorityEntity> authoritiesSelect = new CheckboxGroup<>();
    private final List<FirmEntity> firmEntities;

    public UserForm(List<FirmEntity> firmEntities) {
        super(new BeanValidationBinder<>(UserEntity.class));
        this.firmEntities = firmEntities;
        authoritiesSelect.setSizeFull();
        authoritiesSelect.setItemLabelGenerator(AuthorityEntity::getName);
        authoritiesSelect.addSelectionListener(e -> {
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
                        name -> name.length() >= 4,
                        "Min 4 znáků"
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

        binder.forField(firmsSelect).asRequired("Údaje o firmě jsou povinná")
                .bind(UserEntity::getFirmEntity, UserEntity::setFirmEntity);
        binder.forField(authoritiesSelect).asRequired("Role je povinná")
                .bind(UserEntity::getAuthorityEntities, UserEntity::setAuthorityEntities);
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(username, firstName, lastName, email, password, firmsSelect, authoritiesSelect, enabled);
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
        authoritiesSelect.setItems(allAuthorities);
        if (getEntity() != null && getEntity().getAuthorityEntities() != null)
            authoritiesSelect.select(getEntity().getAuthorityEntities());
        password.setEnabled(true);
        if (getEntity() != null && getEntity().getPassword() != null)
            password.setEnabled(false);
        super.open(title);
    }

    private void configureSelect() {
        firmsSelect.setLabel("Firmy");
        firmsSelect.addClassName("label");
        firmsSelect.setItemLabelGenerator(FirmEntity::getName);
        firmsSelect.setItems(firmEntities);
    }

    public void setAllAuthorities(List<AuthorityEntity> allAuthorities) {
        this.allAuthorities = allAuthorities;
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

    public CheckboxGroup<AuthorityEntity> getAuthoritiesSelect() {
        return authoritiesSelect;
    }

    public List<AuthorityEntity> getAllAuthorities() {
        return allAuthorities;
    }

    public ComboBox<FirmEntity> getFirmsSelect() {
        return firmsSelect;
    }
}
