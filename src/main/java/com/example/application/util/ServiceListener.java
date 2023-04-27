package com.example.application.util;

import com.example.application.error.CustomErrorHandler;
import com.vaadin.flow.server.*;
import com.vaadin.flow.spring.annotation.SpringComponent;

@SpringComponent
public class ServiceListener implements VaadinServiceInitListener {

    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addSessionInitListener(initEvent -> initEvent.getSession().setErrorHandler(new CustomErrorHandler()));
    }
}