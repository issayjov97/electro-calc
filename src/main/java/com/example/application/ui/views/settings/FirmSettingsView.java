package com.example.application.ui.views.settings;

import com.example.application.persistence.entity.FirmSettingsEntity;
import com.example.application.service.FirmSettingsService;
import com.example.application.ui.components.NotificationService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("Firm Settings")
@Route(value = "firmSettings", layout = SettingsView.class)
public class FirmSettingsView extends Div {
    private final FirmSettingsService firmSettingsService;

    public FirmSettingsView(FirmSettingsService firmSettingsService) {
        this.firmSettingsService = firmSettingsService;
        setSizeFull();
        addClassName("edit-form");
        add(getProfileContent());
    }

    private VerticalLayout getProfileContent() {
        final VerticalLayout settingsContent = new VerticalLayout();
        final HorizontalLayout b1 = new HorizontalLayout();
        final HorizontalLayout b2 = new HorizontalLayout();
        final HorizontalLayout b3 = new HorizontalLayout();
        b1.setSizeFull();
        b2.setSizeFull();
        b3.setSizeFull();

        final Binder<FirmSettingsEntity> binder = new BeanValidationBinder<>(FirmSettingsEntity.class);
        final FirmSettingsEntity firmSettingsEntity = firmSettingsService.getFirmSettings();

        NumberField costPerKm = new NumberField("Cena za km (Kč)");
        costPerKm.setWidth("20%");

        NumberField workingHours = new NumberField("Pracovní doba (hod)");
        workingHours.setWidth("20%");

        IntegerField incision = new IntegerField("Prořez (%)");
        incision.setMin(0);
        incision.setMax(100);
        incision.setHasControls(true);
        incision.setWidth("20%");

        NumberField dph = new NumberField("Hladina DPH (%)");
        dph.setWidth("20%");

        NumberField sale = new NumberField("Sleva (%)");
        sale.setWidth("20%");

        NumberField costPerHour = new NumberField("Cena za hodinu (Kč)");
        costPerHour.setWidth("20%");

        Button saveButton = new Button("Uložit");

        binder.forField(costPerKm)
                .bind(FirmSettingsEntity::getCostPerKm, FirmSettingsEntity::setCostPerKm);

        binder.forField(costPerHour)
                .bind(FirmSettingsEntity::getChargePerHour, FirmSettingsEntity::setChargePerHour);

        binder.forField(incision)
                .bind(FirmSettingsEntity::getIncision, FirmSettingsEntity::setIncision);

        binder.forField(sale)
                .bind(FirmSettingsEntity::getSale, FirmSettingsEntity::setSale);

        binder.forField(dph)
                .bind(FirmSettingsEntity::getDph, FirmSettingsEntity::setDph);

        binder.forField(workingHours)
                .bind(FirmSettingsEntity::getWorkingHours, FirmSettingsEntity::setWorkingHours);

        binder.readBean(firmSettingsEntity);

        saveButton.addClickListener(e -> {
            try {
                binder.writeBean(firmSettingsEntity);
                firmSettingsService.save(firmSettingsEntity);
            } catch (ValidationException validationException) {
                validationException.printStackTrace();
            }
            NotificationService.success();
        });
        saveButton.setWidth("20%");
        b1.add(costPerKm, costPerHour);
        b2.add(sale, incision);
        b3.add(workingHours, dph);
        settingsContent.add(b1);
        settingsContent.add(b2);
        settingsContent.add(b3);
        settingsContent.add(saveButton);
        return settingsContent;
    }
}