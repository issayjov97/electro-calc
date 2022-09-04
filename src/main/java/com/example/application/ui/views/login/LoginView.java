package com.example.application.ui.views.login;

import com.example.application.persistence.entity.OneTimePasswordEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.EmailNotificationService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;


@Route("login")
@PageTitle("Rozpočet elektro")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private final LoginForm                login = new LoginForm();
    private final UserService              userService;
    private final EmailNotificationService emailService;
    private final PasswordEncoder          passwordEncoder;

    public LoginView(
            AuthenticationManager authenticationManager,
            UserService userService,
            EmailNotificationService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        H1 appName = new H1("Rozpočet elektro");
        appName.setClassName("title");
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        login.setForgotPasswordButtonVisible(true);
        login.addForgotPasswordListener(e -> {

        });
        add(appName, login);
        login.setAction("login");
//        login.addLoginListener(e -> {
//            try {
//                final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//                UI.getCurrent().navigate(PatternsView.class);
//            } catch (AuthenticationException ex) {
//                login.setError(true);
//            }
//        });
        configureLoginForm();
        changePasswordFlow();
    }

    private void changePasswordFlow() {
        login.addForgotPasswordListener(e -> {
            var dialog = new Dialog();
            var username = new TextField("Uživatelské jméno");
            var confirm = new Button("Potvrdit");
            var vertical = new VerticalLayout(username, confirm);
            vertical.setAlignItems(Alignment.CENTER);
            dialog.add(vertical);
            dialog.open();
            confirm.addClickListener(event -> {
                if (username.getValue().isBlank()) {
                    username.setErrorMessage("Zadejte spravné uživatelské jméno");
                    username.setInvalid(true);
                } else {
                    try {
                        var otpField = new TextField("OTP");
                        var confirmOTP = new Button("Zkontrolovat");
                        var user = userService.getByUsername(username.getValue());
                        var otp = new OneTimePasswordEntity();
                        otp.setValue(RandomString.make(12));
                        otp.setCreatedAt(new Date());
                        user.setOneTimePassword(otp);
                        emailService.sendEmail(otp.getValue(), user.getEmail());
                        userService.save(user);
                        vertical.removeAll();
                        NotificationService.success("Byl odeslán e-mail s OTP");
                        vertical.add(otpField, confirmOTP);
                        confirmOTP.addClickListener(click -> {
                            try {
                                if (otpField.getValue().isBlank() || !user.isOTPValid(otpField.getValue())) {
                                    otpField.setInvalid(true);
                                    otpField.setErrorMessage("Zadejte spravný OTP");
                                } else {
                                    dialog.removeAll();
                                    dialog.add(getAccountSecurityContent(user, dialog));
                                }
                            } catch (RuntimeException ex) {
                                NotificationService.error(ex.getMessage());
                            }

                        });
                    } catch (RuntimeException ex) {
                        NotificationService.error(ex.getMessage());
                    }
                }
            });
        });
    }


    private void configureLoginForm() {
        LoginI18n i18n = LoginI18n.createDefault();
        LoginI18n.Form i18nForm = i18n.getForm();
        i18nForm.setTitle("Přihlášení");
        i18nForm.setUsername("Uživatelské jméno");
        i18nForm.setPassword("Heslo");
        i18nForm.setSubmit("Přihlásit");
        i18nForm.setForgotPassword("Zapomenuté heslo");
        i18n.setForm(i18nForm);
        LoginI18n.ErrorMessage i18nErrorMessage = i18n.getErrorMessage();
        i18nErrorMessage.setTitle("Nesprávné uživatelské jméno nebo heslo");
        i18nErrorMessage.setMessage("Zkontrolujte, zda jste zadali správné uživatelské jméno a heslo, a zkuste to znovu.");
        i18n.setErrorMessage(i18nErrorMessage);
        login.setI18n(i18n);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }

    private VerticalLayout getAccountSecurityContent(UserEntity userEntity, Dialog dialog) {
        final VerticalLayout settingsContent = new VerticalLayout();
        settingsContent.setAlignItems(Alignment.CENTER);
        final Binder<UserEntity> binder = new BeanValidationBinder<>(UserEntity.class);
        PasswordField password = new PasswordField("Nové heslo");
        PasswordField confirmPassword = new PasswordField("Zopakujte nové heslo");
        Button saveButton = new Button("Uložit");
        binder.forField(password)
                .withValidator(
                        v -> v.length() >= 8,
                        "Min 8 znáků"
                )
                .bind(UserEntity::getPassword, UserEntity::setPassword);

        saveButton.addClickListener(e -> {
            if (!password.getValue().equals(confirmPassword.getValue())) {
                NotificationService.error("Hesla nejsou stejná");
            } else {
                userEntity.setPassword(passwordEncoder.encode(password.getValue()));
                userEntity.setOneTimePassword(null);
                NotificationService.success();
                userService.save(userEntity);
                dialog.close();
            }
        });
        settingsContent.add(password);
        settingsContent.add(confirmPassword);
        settingsContent.add(saveButton);
        return settingsContent;
    }
}