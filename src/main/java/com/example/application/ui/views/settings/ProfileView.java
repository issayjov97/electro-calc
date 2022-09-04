package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.AuthService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.crypto.password.PasswordEncoder;

@PageTitle("Personal info")
@Route(value = "account", layout = SettingsView.class)
public class ProfileView extends Div {
    private final UserService        userService;
    private final ChangePasswordForm changePasswordForm;

    public ProfileView(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        changePasswordForm = new ChangePasswordForm(userService, passwordEncoder);
        setSizeFull();
        addClassName("edit-form");
        add(getProfileContent(), changePasswordForm);
    }

    private VerticalLayout getProfileContent() {
        final VerticalLayout settingsContent = new VerticalLayout();
        final Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
        final UserEntity userEntity = userService.getByUsername(AuthService.getUsername());
        TextField firstName = new TextField("Jméno");
        firstName.setWidth("20%");
        firstName.setValue(userEntity.getFirstName());

        TextField lastName = new TextField("Přijmení");
        lastName.setWidth("20%");
        lastName.setValue(userEntity.getLastName());

        TextField username = new TextField("Uživatelské jméno");
        username.setWidth("20%");
        username.setValue(userEntity.getUsername());

        EmailField email = new EmailField("Email");
        email.setWidth("20%");

        email.setValue(userEntity.getEmail());

        Button saveButton = new Button("Uložit");
        Button changePassword = new Button("Změnit heslo");

        binder.forField(firstName)
                .withValidator(
                        v -> v.length() >= 2,
                        "Min 2 znaky"
                )
                .bind(UserEntity::getFirstName, UserEntity::setFirstName);

        binder.forField(lastName)
                .withValidator(
                        v -> v.length() >= 2,
                        "Min 2 znaky"
                )
                .bind(UserEntity::getLastName, UserEntity::setLastName);

        binder.forField(username)
                .withValidator(
                        v -> v.length() >= 6,
                        "Min 6 znaků"
                )
                .bind(UserEntity::getUsername, UserEntity::setUsername);

        var emailValidator = new EmailValidator("");
        binder.forField(email)
                .withValidator(
                        v -> !emailValidator.apply(v, null).isError(),
                        "Email není validní, zkuste jiný")
                .bind(UserEntity::getEmail, UserEntity::setEmail);

        saveButton.addClickListener(e -> {
            try {
                if (!username.getValue().equals(userEntity.getUsername()) && !username.isInvalid())
                    AuthService.replaceAuthentication(username.getValue());
                binder.writeBean(userEntity);
                userService.save(userEntity);
                NotificationService.success();
            } catch (ValidationException validationException) {
                validationException.printStackTrace();
            }
        });
        changePassword.addClickListener(e->{
           changePasswordForm.open("Změna hesla");
        });
        changePassword.setWidth("20%");
        saveButton.setWidth("20%");
        settingsContent.add(firstName);
        settingsContent.add(lastName);
        settingsContent.add(username);
        settingsContent.add(email);
        settingsContent.add(changePassword);
        settingsContent.add(saveButton);
        return settingsContent;
    }
}