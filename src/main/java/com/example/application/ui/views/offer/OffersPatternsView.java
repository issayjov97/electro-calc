package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.service.OfferService;
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

@Route(value = "offer/:offerId", layout = MainView.class)
public class OffersPatternsView extends AbstractServicesView<PatternEntity, OfferEntity> implements BeforeEnterObserver {
    private final OfferService         offerService;
    private final PatternService       patternService;
    private final UserService          userService;
    private final ServiceDetailsDialog serviceDetailsDialog;
    private final Button               addButton = new Button("Add pattern");
    private final Span                 span      = new Span("Patterns");
    private       OfferEntity          offerEntity;

    public OffersPatternsView(OfferService offerService, PatternService patternService, UserService userService) {
        super(new Grid<>(PatternEntity.class, false), offerService);
        this.offerService = offerService;
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
                this.offerEntity = offerService.load(offerEntity.getId());
                offerEntity.getPatterns().remove(patternEntity);
                offerService.save(offerEntity);
                getItems().setItems(offerEntity.getPatterns());
            });
            return deleteButton;
        }).setHeader("Patterns");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");
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
        offerService.addOfferPatterns(selectedPatterns, offerEntity);
        updateList();
        closeEditor();
        var notification = Notification.show("Patterns were added");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }


    public void updateList() {
        getItems().setItems(offerService.load(offerEntity.getId()).getPatterns());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var offerId = event.getRouteParameters().get("offerId").get();
        this.offerEntity = offerService.load(Long.parseLong(offerId));
        updateList();
    }

    public ServiceDetailsDialog getOrderDetailsDialog() {
        return serviceDetailsDialog;
    }

    public OfferEntity getOrderEntity() {
        return offerEntity;
    }

    public void setOrderEntity(OfferEntity orderEntity) {
        this.offerEntity = orderEntity;
    }

    public Button getAddButton() {
        return addButton;
    }
}