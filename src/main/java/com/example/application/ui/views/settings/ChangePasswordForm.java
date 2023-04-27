package com.example.application.ui.views.settings;

import com.example.application.model.dto.UpdatePassword;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.views.AbstractForm;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ChangePasswordForm extends AbstractForm<UserEntity> {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final PasswordField actualPassword = new PasswordField("Aktuální heslo");
    private final PasswordField password = new PasswordField("Nové heslo");
    private final PasswordField confirmPassword = new PasswordField("Zopakujte nové heslo");
    private final UpdatePassword updatePassword = new UpdatePassword();
    private final Binder<UpdatePassword> passwordBinder = new BeanValidationBinder<>(UpdatePassword.class);
    private boolean forgottenPassword = false;

    public ChangePasswordForm(UserService userService, PasswordEncoder passwordEncoder, boolean forgottenPassword) {
        super(new BeanValidationBinder<>(UserEntity.class));
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.forgottenPassword = forgottenPassword;
        setBinder();
        dialog.add(createDialogLayout(), createButtonsLayout());
    }

    @Override
    protected void setBinder() {
        if (!forgottenPassword)
            passwordBinder.forField(actualPassword)
                    .asRequired()
                    .withValidator(
                            v -> v.length() >= 8,
                            "Min 8 znáků")
                    .bind(UpdatePassword::getActualPassword, UpdatePassword::setActualPassword);
        ;
        passwordBinder.forField(password)
                .asRequired()
                .withValidator(
                        v -> v.length() >= 8,
                        "Min 8 znáků")
                .bind(UpdatePassword::getPassword, UpdatePassword::setPassword);
        passwordBinder.forField(confirmPassword)
                .asRequired()
                .withValidator(
                        v -> v.length() >= 8,
                        "Min 8 znáků")
                .bind(UpdatePassword::getConfirmPassword, UpdatePassword::setConfirmPassword);
    }

    @Override
    protected Component createDialogLayout() {
        var vertical = new VerticalLayout();
        if (!forgottenPassword)
            vertical.add(actualPassword);
        vertical.add(password, confirmPassword);
        return vertical;
    }

    @Override
    public void close() {
        actualPassword.clear();
        password.clear();
        confirmPassword.clear();
        super.close();
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        final HorizontalLayout horizontalLayout = new HorizontalLayout(saveButton);
        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        saveButton.addClickListener(event -> validateAndSave());
        return horizontalLayout;
    }

    @Override
    protected void validateAndSave() {
        try {
            passwordBinder.writeBean(updatePassword);
            if (!forgottenPassword) {
                if (actualPassword.isEmpty() || !passwordEncoder.matches(updatePassword.getActualPassword(), getEntity().getPassword())) {
                    actualPassword.setErrorMessage("Heslo není spravné");
                    actualPassword.setInvalid(true);
                    return;
                }
            }
            if (!updatePassword.getPassword().equals(updatePassword.getConfirmPassword())) {
                password.setErrorMessage("Hesla nejsou stejná");
                confirmPassword.setErrorMessage("Hesla nejsou stejná");
                password.setInvalid(true);
                confirmPassword.setInvalid(true);
            } else {
                getEntity().setPassword(passwordEncoder.encode(updatePassword.getPassword()));
                getEntity().setOneTimePassword(null);
                NotificationService.success();
                userService.save(getEntity());
                close();
            }
        } catch (ValidationException ex) {
            ex.printStackTrace();
        }
    }
}