package com.example.application.ui.views.demand;

import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.service.DemandService;
import com.example.application.service.PatternService;
import com.example.application.service.UserService;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.ServiceDetailsDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

@Route(value = "demand/:demandId", layout = MainView.class)
public class DemandsPatternsView extends AbstractServicesView<PatternEntity, DemandEntity> implements BeforeEnterObserver {
    private final DemandService        demandService;
    private final PatternService       patternService;
    private final UserService          userService;
    private final ServiceDetailsDialog serviceDetailsDialog;
    private final Button               addButton = new Button("Add pattern");
    private final Span                 span      = new Span("Patterns");

    private DemandEntity demandEntity;

    public DemandsPatternsView(DemandService demandService, PatternService patternService, UserService userService) {
        super(new Grid<>(PatternEntity.class, false), demandService);
        this.demandService = demandService;
        this.patternService = patternService;
        this.userService = userService;
        this.serviceDetailsDialog = new ServiceDetailsDialog();
        addClassName("Patterns-view");
        setSizeFull();
        configureGrid();
        configureEvents();
        add(getToolBar(), getContent());
    }

    @Override
    protected void configureForm() {

    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("order-details-grid");
        getItems().setSizeFull();
        getItems().setColumns("name", "description", "duration");
        getItems().addColumn(PatternEntity::getPriceWithoutVAT).setHeader("Price without VAT");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().addComponentColumn((patternEntity) -> {
            final Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                this.demandEntity = demandService.getById(demandEntity.getId());
                demandEntity.getPatterns().remove(patternEntity);
                demandService.save(demandEntity);
                getItems().setItems(demandEntity.getPatterns());
            });
            return deleteButton;
        }).setHeader("Patterns");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.addClassName("headline");
        addButton.addClickListener(e -> {
            serviceDetailsDialog.setPatterns(patternService.getFirmPatterns());
            getItems().asSingleSelect().clear();
            serviceDetailsDialog.open();
        });
        HorizontalLayout toolbar = new HorizontalLayout(addButton);
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.getStyle().set("max-width", "100%");
        toolbar.addClassName("toolbar");
        HorizontalLayout horizontalLayout = new HorizontalLayout(span, toolbar);
        horizontalLayout.setAlignItems(Alignment.BASELINE);
        return horizontalLayout;
    }

    @Override
    protected void configureEvents() {
        serviceDetailsDialog.addListener(ServiceDetailsDialog.SaveEvent.class, this::saveSelectedItems);
        serviceDetailsDialog.addListener(ServiceDetailsDialog.CloseEvent.class, e -> closeEditor());
    }

    @Override
    protected void closeEditor() {
        serviceDetailsDialog.close();
        serviceDetailsDialog.clearPatterns();
    }

    private void saveSelectedItems(ServiceDetailsDialog.SaveEvent event) {
        var selectedPatterns = event.getPatterns();
        demandService.addDemandPatterns(selectedPatterns, demandEntity);
        updateList();
        closeEditor();
        var notification = Notification.show("Patterns were added");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public void updateList() {
        getItems().setItems(demandService.getById(demandEntity.getId()).getPatterns());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var demandId = event.getRouteParameters().get("demandId").get();
        this.demandEntity = demandService.load(Long.parseLong(demandId));
        updateList();
    }

    public ServiceDetailsDialog getOrderDetailsDialog() {
        return serviceDetailsDialog;
    }

    public DemandEntity getOrderEntity() {
        return demandEntity;
    }

    public void setOrderEntity(DemandEntity demandEntity) {
        this.demandEntity = demandEntity;
    }

    public Button getAddButton() {
        return addButton;
    }
}