package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.AuthService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.views.AbstractForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ChangePasswordForm extends AbstractForm<UserEntity> {
    private final UserService     userService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordField   oldPassword     = new PasswordField("Aktuální heslo");
    private final PasswordField   password        = new PasswordField("Nové heslo");
    private final PasswordField   confirmPassword = new PasswordField("Zopakujte nové heslo");

    public ChangePasswordForm(UserService userService, PasswordEncoder passwordEncoder) {
        super(new BeanValidationBinder<>(UserEntity.class));
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        dialog.add(createDialogLayout());
    }

    @Override
    protected void setBinder() {

    }

    @Override
    protected Component createDialogLayout() {
        final VerticalLayout settingsContent = new VerticalLayout();
        final Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);

        password.setSizeFull();
        confirmPassword.setSizeFull();

        binder.forField(password)
                .withValidator(
                        v -> v.length() >= 8,
                        "Heslo musí obsahovat alespon 8 znáků"
                )
                .bind(UserEntity::getPassword, UserEntity::setPassword);

        saveButton.addClickListener(e -> {
            UserEntity userEntity = userService.getByUsername(AuthService.getUsername());
            if (oldPassword.getValue() == null
                    || oldPassword.getValue().isBlank()
                    || !passwordEncoder.matches(oldPassword.getValue(), userEntity.getPassword())) {
                NotificationService.error("Aktuální heslo není spravné");
            } else if (password.getValue().isBlank()
                    || confirmPassword.getValue().isBlank()
                    || !password.getValue().equals(confirmPassword.getValue())) {
                NotificationService.error("Hesla nejsou stejná");
            } else {
                userEntity.setPassword(passwordEncoder.encode(password.getValue()));
                userService.save(userEntity);
                NotificationService.success();
                close();
            }
        });
        cancelButton.addClickListener(e -> {
            close();
        });
        settingsContent.add(oldPassword);
        settingsContent.add(password);
        settingsContent.add(confirmPassword);
        settingsContent.add(new HorizontalLayout(saveButton, cancelButton));
        return settingsContent;
    }

    @Override
    public void close() {
        oldPassword.clear();
        password.clear();
        confirmPassword.clear();
        super.close();
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        return null;
    }

    @Override
    protected void validateAndSave() {

    }
}