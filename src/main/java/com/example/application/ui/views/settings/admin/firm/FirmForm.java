package com.example.application.ui.views.settings.admin.firm;

import com.example.application.client.MFCRInfoClient;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.settings.admin.firm.events.DeleteEvent;
import com.example.application.ui.views.settings.admin.firm.events.SaveEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import cz.mfcr.wwwinfo.ares.xml_doc.schemas.ares.ares_answer.v_1_0.Odpoved;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FirmForm extends AbstractForm<FirmEntity> {
    private final TextField name = new TextField("Název");
    private final TextField street = new TextField("Ulice");
    private final TextField postCode = new TextField("PSČ");
    private final TextField state = new TextField("Stát");
    private final TextField city = new TextField("Město");
    private final TextField CIN = new TextField("IČO");
    private final TextField VATIN = new TextField("DIČ");
    private final TextField phone = new TextField("Telefonní číslo\n");
    private final EmailField email = new EmailField("Email");
    private MFCRInfoClient mvcrInfoClient;

    public FirmForm(MFCRInfoClient mvcrInfoClient) {
        super(new BeanValidationBinder<>(FirmEntity.class));
        this.mvcrInfoClient = mvcrInfoClient;
        setBinder();
        dialog.add(getHeadline(), createDialogLayout(), createButtonsLayout());
    }

    @Override
    protected void setBinder() {
        binder.forField(name).asRequired("Název je povinný")
                .withValidator(name -> name.length() >= 4, "Min 4 znaků")
                .bind(FirmEntity::getName, FirmEntity::setName);
        binder.forField(street).bind(FirmEntity::getStreet, FirmEntity::setStreet);
        binder.forField(postCode).bind(FirmEntity::getPostCode, FirmEntity::setPostCode);
        binder.forField(state).bind(FirmEntity::getState, FirmEntity::setState);
        binder.forField(city).bind(FirmEntity::getCity, FirmEntity::setCity);
        binder.forField(CIN).bind(FirmEntity::getCIN, FirmEntity::setCIN);
        binder.forField(VATIN).bind(FirmEntity::getVATIN, FirmEntity::setVATIN);
        binder.forField(phone).bind(FirmEntity::getPhone, FirmEntity::setPhone);
        binder.forField(email).bind(FirmEntity::getEmail, FirmEntity::setEmail);
    }

    @Override
    protected Component createDialogLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(name, street, postCode, city, state, CIN, VATIN, phone, email);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("200px", 2));
        formLayout.setColspan(name, 2);
        formLayout.setColspan(street, 2);
        formLayout.setColspan(state, 2);
        formLayout.setMaxWidth("600px");
        var helperText = new Span("Pro vyhledávání v registru MFČR zadejte vaše IČO a zmáčkněte ENTER");
        helperText.addClassName("helper-text");
        CIN.setHelperComponent(helperText);
        CIN.addKeyPressListener(Key.ENTER, event -> {
            var aresOdpovedi = mvcrInfoClient.getAresResponses(CIN.getValue());
            if (aresOdpovedi != null) {
                fillOutForm(aresOdpovedi.getOdpoved());
            }
        });
        return formLayout;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        final HorizontalLayout buttonsMenu = new HorizontalLayout();
        buttonsMenu.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, getEntity())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this, false)));
        buttonsMenu.add(saveButton, deleteButton, cancelButton);
        return buttonsMenu;
    }

    @Override
    protected void validateAndSave() {
        try {
            binder.writeBean(getEntity());
            fireEvent(new SaveEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    private void fillOutForm(List<Odpoved> odpovedy) {
        if (!odpovedy.isEmpty()) {
            var zaznamy = odpovedy.get(0).getZaznam();
            if (!zaznamy.isEmpty()) {
                var zaznam = zaznamy.get(0);
                var address = zaznam.getIdentifikace().getAdresaARES();
                var streetWithBuildingNumber = Stream.of(
                                address.getNazevUlice(),
                                address.getCisloDomovni() != null ? address.getCisloDomovni().toString() : null
                        )
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining(" "));
                name.setValue(zaznam.getObchodniFirma());
                CIN.setValue(zaznam.getICO());
                state.setValue("Česká republika");
                city.setValue(address.getNazevObce());
                street.setValue(Stream.of(streetWithBuildingNumber, address.getCisloOrientacni())
                        .filter(s -> s != null && !s.isEmpty())
                        .collect(Collectors.joining("/")));
                postCode.setValue(address.getPSC());
                NotificationService.success();
            } else
                NotificationService.error("Nebyl nalezen žádný odpovídající záznam");
        }
    }
}
