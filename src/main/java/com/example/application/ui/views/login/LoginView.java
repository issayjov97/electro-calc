package com.example.application.ui.views.login;

import com.example.application.config.CustomRequestCache;
import com.example.application.ui.views.order.OrdersView;
import com.example.application.ui.views.patern.ItemsView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;

@Route("login")
@PageTitle("Login | Vaadin CRM")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    private       H1        appName;
    private final LoginForm login = new LoginForm();

    public LoginView(
            AuthenticationManager authenticationManager,
            CustomRequestCache customRequestCache
    ) {
        appName = new H1("Vaadin CRM");
        appName.setClassName("title");
        addClassName("login-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        add(appName, login);

        login.addLoginListener(e -> {
            try {
                final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(e.getUsername(), e.getPassword()));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UI.getCurrent().navigate(ItemsView.class);
            } catch (AuthenticationException ex) {
                login.setError(true);
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if (beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}