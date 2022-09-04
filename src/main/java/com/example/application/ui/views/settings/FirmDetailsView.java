package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.service.AuthService;
import com.example.application.service.FirmService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Firm Details")
@Route(value = "firmDetails", layout = SettingsView.class)
public class FirmDetailsView extends Div {
    private final FirmService firmService;
    private final UserService userService;

    public FirmDetailsView(FirmService firmService, UserService userService) {
        this.firmService = firmService;
        this.userService = userService;
        setSizeFull();
        addClassName("edit-form");
        add(getProfileContent());
    }

    private VerticalLayout getProfileContent() {
        final VerticalLayout settingsContent = new VerticalLayout();
        final HorizontalLayout b1 = new HorizontalLayout();
        final HorizontalLayout b2 = new HorizontalLayout();
        Button saveButton = new Button("Uložit");
        b1.setSizeFull();
        b2.setSizeFull();
        final Binder<FirmEntity> binder = new BeanValidationBinder<>(FirmEntity.class);
        final FirmEntity firmEntity = userService.getUserFirm(AuthService.getUsername());

        final TextField name = new TextField("Název");
        final TextField street = new TextField("Ulice");
        final TextField postCode = new TextField("PSČ");
        final TextField state = new TextField("Stát");
        final TextField city = new TextField("Město");
        final TextField CIN = new TextField("IČO");
        final TextField VATIN = new TextField("DIČ");
        final TextField phone = new TextField("Telefon");
        final EmailField email = new EmailField("Email");

        name.setWidth("20%");
        street.setWidth("20%");
        postCode.setWidth("10%");
        state.setWidth("20%");
        city.setWidth("9.5%");
        postCode.setWidth("9.5%");
        CIN.setWidth("9.5%");
        VATIN.setWidth("9.5%");
        phone.setWidth("20%");
        email.setWidth("20%");

        binder.forField(name).asRequired("Name is required")
                .withValidator(
                        e -> e.length() >= 4,
                        "Name must contain at least 6 characters"
                ).bind(FirmEntity::getName, FirmEntity::setName);
        binder.bind(street, "street");
        binder.bind(postCode, "postCode");
        binder.bind(state, "state");
        binder.bind(city, "city");
        binder.bind(CIN, "CIN");
        binder.bind(VATIN, "VATIN");
        binder.bind(phone, "phone");
        binder.bind(email, "email");

        binder.readBean(firmEntity);


        saveButton.addClickListener(e -> {
            try {
                binder.writeBean(firmEntity);
                firmService.save(firmEntity);
            } catch (ValidationException validationException) {
                validationException.printStackTrace();
            }
            NotificationService.success();
        });
        saveButton.setWidth("20%");
        b1.add(postCode, city);
        b2.add(CIN, VATIN);
        settingsContent.add(name, street, b1, state, b2, phone, email, saveButton);
        return settingsContent;
    }
}