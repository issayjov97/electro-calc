package com.example.application.ui.views.settings.admin.firm;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.service.FirmService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.settings.SettingsView;
import com.example.application.ui.views.settings.admin.firm.events.DeleteEvent;
import com.example.application.ui.views.settings.admin.firm.events.SaveEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@PageTitle("Firmy")
@Secured("ADMIN")
@Route(value = "Firms", layout = SettingsView.class)
public class FirmsView extends AbstractServicesView<FirmEntity, FirmEntity> {
    private final FirmForm    firmForm  = new FirmForm();
    private final Button      addButton = new Button("Přidat firmu");
    private final FirmService firmService;

    public FirmsView(FirmService firmService) {
        super(new Grid<>(), firmService);
        this.firmService = firmService;
        setSizeFull();
        configureGrid();
        add(getToolBar(), getContent(), firmForm);
        configureEvents();
    }

    @Override
    protected void configureForm() {

    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("items-grid");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().addColumn(FirmEntity::getName).setHeader("Název");
        getItems().addColumn(FirmEntity::getCIN).setHeader("IČO");
        getItems().addColumn(FirmEntity::getVATIN).setHeader("DIČ");
        getItems().addColumn(FirmEntity::getEmail).setHeader("Email");
        getItems().addColumn(FirmEntity::getState).setHeader("Stát");
        getItems().addColumn(FirmEntity::getCity).setHeader("Město");
        getItems().addColumn(FirmEntity::getStreet).setHeader("Ulice");
        getItems().addColumn(FirmEntity::getPostCode).setHeader("PSČ");
        getItems().addColumn(FirmEntity::getPhone).setHeader("Telefonní číslo");
        getItems().setItems(firmService.loadAll());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                firmForm.setEntity(event.getValue());
                firmForm.open("Editace firmy");
            }
        });
    }

    @Override
    protected HorizontalLayout getToolBar() {
        addButton.setIcon(VaadinIcon.PLUS.create());

        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            firmForm.setEntity(new FirmEntity());
            firmForm.open("Přidat firmu");
        });
        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.addClassName("filterPart");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        firmForm.addListener(SaveEvent.class, this::saveItem);
        firmForm.addListener(CloseEvent.class, e -> closeEditor());
        firmForm.addListener(DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        firmForm.setEntity(null);
        firmForm.close();
    }

    private void saveItem(SaveEvent event) {
        var firm = firmService.save(event.getItem());
        if (firm.isCopyDefaultPatterns()) {
            firmService.copyDefaultPatterns(firm.getId());
            firm = firmService.load(firm.getId());
            firm.setCopyDefaultPatterns(false);
            firmService.save(firm);
        }
        updateList();
        closeEditor();
        NotificationService.success();
    }

    @Override
    protected void updateList() {
        getItems().setItems(firmService.loadAll());
    }

    private void deleteItem(DeleteEvent event) {
        firmService.delete(event.getItem());
        updateList();
        closeEditor();
        NotificationService.success();
    }

    public Button getAddButton() {
        return addButton;
    }

}
