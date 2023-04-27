package com.example.application.ui.views.login;

import com.example.application.persistence.entity.OneTimePasswordEntity;
import com.example.application.persistence.entity.UserEntity;
import com.example.application.service.EmailNotificationService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.views.settings.ChangePasswordForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.login.LoginI18n;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

@Route("login")
@PageTitle("Rozpočet elektro")
public class LoginView extends Div implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final UserService userService;
    private final EmailNotificationService emailService;
    private final PasswordEncoder passwordEncoder;
    private Dialog forgetPasswordDialog;

    public LoginView(
            UserService userService,
            EmailNotificationService emailService,
            PasswordEncoder passwordEncoder
    ) {
        this.userService = userService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        H1 appName = new H1("Rozpočet elektro");
        Div div = new Div(appName);
        div.addClassName("firm");
        appName.setClassName("title");
        addClassName("login-view");
        setSizeFull();
        login.setForgotPasswordButtonVisible(true);
        add(div, login);
        login.setAction("login");
        configureLoginForm();
        changePasswordFlow();
    }

    private void changePasswordFlow() {
        login.addForgotPasswordListener(e -> {
            forgetPasswordDialog = new Dialog();
            var username = new TextField("Uživatelské jméno");
            var confirm = new Button("Potvrdit");
            var vertical = new VerticalLayout(username, confirm);
            vertical.setAlignItems(FlexComponent.Alignment.CENTER);
            forgetPasswordDialog.add(vertical);
            forgetPasswordDialog.open();
            confirm.addClickListener(event -> {
                if (username.getValue().isBlank()) {
                    username.setErrorMessage("Zadejte spravné uživatelské jméno");
                    username.setInvalid(true);
                } else {
                    var user = userService.getByUsername(username.getValue());
                    var otp = Optional.ofNullable(user.getOneTimePassword())
                            .orElse(new OneTimePasswordEntity());
                    otp.setUserEntity(user);
                    otp.setValue(RandomString.make(5));
                    otp.setCreatedAt(new Date());
                    user.setOneTimePassword(otp);
                    var wasSend = emailService.sendEmail(otp.getValue(), user.getEmail());
                    if (wasSend) {
                        userService.save(user);
                        forgetPasswordDialog.removeAll();
                        forgetPasswordDialog.add(configureOTPForm(user));
                    } else
                        NotificationService.success("Na e-mail " + user.getEmail() + " nejde odeslat OTP");
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
        i18nErrorMessage.setMessage("Zkontrolujte, zda jste zadali správné uživatelské jméno a heslo.");
        i18n.setErrorMessage(i18nErrorMessage);
        login.setI18n(i18n);
    }

    private VerticalLayout configureOTPForm(UserEntity user) {
        var label = new Label("Na e-mail " + user.getEmail() + " bylo odesláno OTP.");
        var otpField = new TextField("OTP");
        var confirmOTP = new Button("Zkontrolovat");
        confirmOTP.addClickListener(click -> {
            if (otpField.getValue().isBlank() || !user.isOTPValid(otpField.getValue())) {
                otpField.setInvalid(true);
                otpField.setErrorMessage("Zadejte spravné OTP");
            } else {
                var changePasswordForm = new ChangePasswordForm(userService, passwordEncoder, true);
                forgetPasswordDialog.removeAll();
                forgetPasswordDialog.add(changePasswordForm);
                changePasswordForm.setEntity(user);
                changePasswordForm.open("Změna hesla");
                changePasswordForm.getDialog().addOpenedChangeListener(e -> {
                    if (!e.isOpened()) {
                        forgetPasswordDialog.close();
                    }
                });
            }
        });
        VerticalLayout vertical = new VerticalLayout(label, otpField, confirmOTP);
        vertical.setAlignItems(FlexComponent.Alignment.CENTER);
        return vertical;
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
}