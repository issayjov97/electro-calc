package com.example.application.ui.views.settings;

import com.example.application.client.MFCRInfoClient;
import com.example.application.persistence.entity.FirmEntity;
import com.example.application.service.AuthService;
import com.example.application.service.FirmService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import cz.mfcr.wwwinfo.ares.xml_doc.schemas.ares.ares_answer.v_1_0.Odpoved;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PageTitle("Firm Details")
@Route(value = "firmDetails", layout = SettingsView.class)
public class FirmDetailsView extends Div {
    private final FirmService firmService;
    private final UserService userService;
    private final TextField name = new TextField("Název");
    private final TextField street = new TextField("Ulice");
    private final TextField postCode = new TextField("PSČ");
    private final TextField state = new TextField("Stát");
    private final TextField city = new TextField("Město");
    private final TextField CIN = new TextField("IČO");
    private final TextField VATIN = new TextField("DIČ");
    private final TextField phone = new TextField("Telefon");
    private final EmailField email = new EmailField("Email");
    private final MFCRInfoClient mvcrInfoClient;

    public FirmDetailsView(FirmService firmService, UserService userService, MFCRInfoClient mvcrInfoClient) {
        this.firmService = firmService;
        this.userService = userService;
        this.mvcrInfoClient = mvcrInfoClient;
        setSizeFull();
        addClassName("edit-form");
        add(getProfileContent());
    }

    private VerticalLayout getProfileContent() {
        final VerticalLayout settingsContent = new VerticalLayout();
        final HorizontalLayout b1 = new HorizontalLayout();
        Button saveButton = new Button("Uložit");
        saveButton.addClickShortcut(Key.KEY_S, KeyModifier.ALT);
        b1.setSizeFull();
        final Binder<FirmEntity> binder = new BeanValidationBinder<>(FirmEntity.class);
        final FirmEntity firmEntity = userService.getUserFirm(AuthService.getUsername());
        var helperText = new Span("Pro vyhledávání v registru MFČR zadejte vaše IČO a zmáčkněte ENTER");
        helperText.addClassName("helper-text");
        CIN.setHelperComponent(helperText);
        CIN.addKeyPressListener(Key.ENTER, event -> {
            var aresOdpovedi = mvcrInfoClient.getAresResponses(CIN.getValue());
            if (aresOdpovedi != null) {
                fillOutForm(aresOdpovedi.getOdpoved());
            }
        });

        name.setWidth("20%");
        street.setWidth("20%");
        postCode.setWidth("10%");
        state.setWidth("20%");
        city.setWidth("9.5%");
        postCode.setWidth("9.5%");
        CIN.setWidth("20%");
        VATIN.setWidth("20%");
        phone.setWidth("20%");
        email.setWidth("20%");

        binder.forField(name).asRequired("Název je povinný")
                .withValidator(
                        e -> e.length() >= 4,
                        "Min 4 znaků"
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
        settingsContent.add(name, street, b1, state, CIN, VATIN, phone, email, saveButton);
        return settingsContent;
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