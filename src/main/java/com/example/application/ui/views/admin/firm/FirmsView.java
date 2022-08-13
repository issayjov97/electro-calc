package com.example.application.ui.views.admin.firm;

import com.example.application.persistence.entity.FirmEntity;
import com.example.application.service.AuthorityService;
import com.example.application.service.FirmService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.admin.firm.events.DeleteEvent;
import com.example.application.ui.views.admin.firm.events.SaveEvent;
import com.example.application.ui.views.settings.SettingsView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.access.annotation.Secured;

@PageTitle("Firms")
@Secured("ADMIN")
@Route(value = "Firms", layout = SettingsView.class)
public class FirmsView extends AbstractServicesView<FirmEntity, FirmEntity> {
    private final TextField name        = new TextField();
    private final TextField emailFilter = new TextField();
    private final FirmForm    firmForm       = new FirmForm();
    private final Button      addButton      = new Button("Add firm");
    private final Button      filterButton   = new Button("Filter");
    private final FirmService firmService;

    public FirmsView(FirmService firmService) {
        super(new Grid<>(FirmEntity.class), firmService);
        this.firmService = firmService;
        addClassName("Firms-view");
        configureGrid();
        add(getToolBar(), getContent(), firmForm);
        configureEvents();
    }

    @Override
    protected void configureForm() {

    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("firms-grid");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setColumns("CIN", "VATIN", "name", "email", "state", "city", "street","postCode", "phone", "mobile");
        getItems().setItems(firmService.loadAll());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                firmForm.setEntity(event.getValue());
                firmForm.open("Edit firm");
            }
        });
        getItems().addComponentColumn((firmEntity) -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                firmService.delete(firmEntity);
                updateList();
                var notification = Notification.show("Firm was deleted");
                notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                notification.setPosition(Notification.Position.TOP_CENTER);
            });
            return deleteButton;
        }).setHeader("Actions");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        name.getElement().setAttribute("theme", "test");
        emailFilter.getElement().setAttribute("theme", "test");

        name.setPlaceholder("name");
        name.setClearButtonVisible(true);
        name.setValueChangeMode(ValueChangeMode.LAZY);

        emailFilter.setPlaceholder("email");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);

        filterButton.addClickListener(e -> {
        });

        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            firmForm.setEntity(new FirmEntity());
            firmForm.open("Add firm");
        });
        HorizontalLayout toolbar = new HorizontalLayout(name, emailFilter, filterButton, addButton);
        toolbar.addClassName("toolbar");
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
        firmService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Firm was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    protected void updateList() {
        getItems().setItems(firmService.loadAll());
    }

    private void deleteItem(DeleteEvent event) {
        firmService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Firm was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public TextField getName() {
        return name;
    }

    public TextField getEmailFilter() {
        return emailFilter;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getFilterButton() {
        return filterButton;
    }
}
