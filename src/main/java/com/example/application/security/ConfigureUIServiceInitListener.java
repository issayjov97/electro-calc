package com.example.application.security;

import com.example.application.error.AccessDeniedException;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import com.example.application.ui.views.login.LoginView;
import org.springframework.stereotype.Component;

@Component
public class ConfigureUIServiceInitListener implements VaadinServiceInitListener {

	@Override
	public void serviceInit(ServiceInitEvent event) { 
		event.getSource().addUIInitListener(uiEvent -> {
			final UI ui = uiEvent.getUI();
			ui.addBeforeEnterListener(this::authenticateNavigation);
		});
	}

	private void authenticateNavigation(BeforeEnterEvent event) {
		if(!SecurityUtils.isAccessGranted(event.getNavigationTarget())) { // (1)
			if(SecurityUtils.isUserLoggedIn()) { // (2)
				event.rerouteToError(AccessDeniedException.class); // (3)
			} else {
				event.rerouteTo(LoginView.class); // (4)
			}
		}
	}
}