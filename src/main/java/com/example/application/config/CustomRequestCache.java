package com.example.application.config;

import com.example.application.security.SecurityUtils;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import org.springframework.security.web.savedrequest.DefaultSavedRequest;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomRequestCache extends HttpSessionRequestCache {
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if (!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }

    public String resolveRedirectUrl() {
        SavedRequest savedRequest = getRequest(VaadinServletRequest.getCurrent().getHttpServletRequest(), VaadinServletResponse.getCurrent().getHttpServletResponse());
        if(savedRequest instanceof DefaultSavedRequest) {
            final String requestURI = ((DefaultSavedRequest) savedRequest).getRequestURI(); // (1)
            if (requestURI != null && !requestURI.isEmpty() && !requestURI.contains("login")) { // (2)
                return requestURI.startsWith("/") ? requestURI.substring(1) : requestURI; // (3)
            }
        }
        return "";
    }
}
