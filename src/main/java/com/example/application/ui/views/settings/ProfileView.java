package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.AuthService;
import com.example.application.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.validator.EmailValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Personal info")
@Route(value = "account", layout = SettingsView.class)
public class ProfileView extends Div {
    private final UserService userService;

    public ProfileView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        addClassName("edit-form");
        add(getProfileContent());
    }

    private VerticalLayout getProfileContent() {
        final VerticalLayout settingsContent = new VerticalLayout();
        final Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
        final UserEntity userEntity = userService.getByUsername(AuthService.getUsername());
        TextField firstName = new TextField("First name");
        firstName.setValue(userEntity.getFirstName());

        TextField lastName = new TextField("Last name");
        lastName.setValue(userEntity.getLastName());

        TextField username = new TextField("Username");
        username.setValue(userEntity.getUsername());

        EmailField email = new EmailField("Email");
        email.setValue(userEntity.getEmail());

        Button saveButton = new Button("Confirm");

        binder.forField(firstName)
                .withValidator(
                        v -> v.length() >= 2,
                        "Min 2 characters"
                )
                .bind(UserEntity::getFirstName, UserEntity::setFirstName);

        binder.forField(lastName)
                .withValidator(
                        v -> v.length() >= 2,
                        "Min 2 characters"
                )
                .bind(UserEntity::getLastName, UserEntity::setLastName);

        binder.forField(username)
                .withValidator(
                        v -> v.length() >= 6,
                        "Min 6 characters"
                )
                .bind(UserEntity::getUsername, UserEntity::setUsername);

        var emailValidator = new EmailValidator("");
        binder.forField(email)
                .withValidator(
                        v -> !emailValidator.apply(v, null).isError(),
                        "This doesn't look like a valid email address")
                .bind(UserEntity::getEmail, UserEntity::setEmail);

        saveButton.addClickListener(e -> {
            UserEntity user = new UserEntity();
            user.setFirstName(firstName.getValue());
            user.setLastName(lastName.getValue());
            user.setUsername(username.getValue());
            user.setEmail(email.getValue());
            try {
                binder.writeBean(user);
                userService.save(user);
                var notification = Notification.show("Personal info were updated");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            } catch (ValidationException validationException) {
                validationException.printStackTrace();
            }
        });
        settingsContent.add(firstName);
        settingsContent.add(lastName);
        settingsContent.add(username);
        settingsContent.add(email);
        settingsContent.add(saveButton);
        return settingsContent;
    }
}