package com.example.application.ui.views.settings;

import com.example.application.dto.UserDTO;
import com.example.application.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Account security")
@Route(value = "security", layout = SettingsView.class)
public class AccountSecurityView extends Div {
    private final UserService userService;

    public AccountSecurityView(UserService userService) {
        this.userService = userService;
        setSizeFull();
        addClassName("edit-form");
        add(getAccountSecurityContent());
    }

    private VerticalLayout getAccountSecurityContent() {
        final VerticalLayout settingsContent = new VerticalLayout();
        final Binder<UserDTO> binder = new BeanValidationBinder<>(UserDTO.class);
        PasswordField password = new PasswordField("New password");
        PasswordField confirmPassword = new PasswordField("Confirm new password");
        Button saveButton = new Button("Confirm");

        binder.forField(password)
                .withValidator(
                        v -> v.length() >= 8,
                        "Password must contain at least 8 characters "
                )
                .bind(UserDTO::getPassword, UserDTO::setPassword);

        saveButton.addClickListener(e -> {
            UserDTO userDTO = new UserDTO();
            userDTO.setPassword(password.getValue());
            try {
                binder.writeBean(userDTO);
                userService.updateUser(userDTO);
                var notification = Notification.show("Password was changed");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
                if (!password.getValue().equals(confirmPassword.getValue())) {
                    notification = Notification.show("Entered passwords are different");
                    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                    notification.setPosition(Notification.Position.TOP_CENTER);
                }
            } catch (ValidationException validationException) {
                validationException.printStackTrace();
            }
        });
        settingsContent.add(password);
        settingsContent.add(confirmPassword);
        settingsContent.add(saveButton);
        return settingsContent;
    }
}