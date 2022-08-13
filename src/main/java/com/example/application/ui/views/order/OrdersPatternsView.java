package com.example.application.ui.views.order;

import com.example.application.persistence.entity.OrderEntity;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.service.OrderService;
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

@Route(value = "order/:orderId", layout = MainView.class)
public class OrdersPatternsView extends AbstractServicesView<PatternEntity, OrderEntity> implements BeforeEnterObserver {
    private final OrderService         orderService;
    private final PatternService       patternService;
    private final UserService          userService;
    private final ServiceDetailsDialog orderDetailsDialog;
    private final Button               addButton = new Button("Add pattern");
    private final Span                 span      = new Span("Patterns");
    private       OrderEntity          orderEntity;

    public OrdersPatternsView(OrderService orderService, PatternService patternService, UserService userService) {
        super(new Grid<>(PatternEntity.class, false), orderService);
        this.orderService = orderService;
        this.patternService = patternService;
        this.userService = userService;
        this.orderDetailsDialog = new ServiceDetailsDialog();
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
                this.orderEntity = orderService.getOrder(orderEntity.getId());
                orderEntity.getPatterns().remove(patternEntity);
                orderService.save(orderEntity);
                getItems().setItems(orderEntity.getPatterns());
            });
            return deleteButton;
        }).setHeader("Patterns");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");
        addButton.addClickListener(e -> {
            orderDetailsDialog.setPatterns(patternService.getFirmPatterns());
            getItems().asSingleSelect().clear();
            orderDetailsDialog.open();
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
        orderDetailsDialog.addListener(ServiceDetailsDialog.SaveEvent.class, this::saveSelectedItems);
        orderDetailsDialog.addListener(ServiceDetailsDialog.CloseEvent.class, e -> closeEditor());
    }

    @Override
    protected void closeEditor() {
        orderDetailsDialog.close();
        orderDetailsDialog.clearPatterns();
    }

    private void saveSelectedItems(ServiceDetailsDialog.SaveEvent event) {
        var selectedPatterns = event.getPatterns();
        orderService.addOrderPatterns(selectedPatterns, orderEntity);
        updateList();
        closeEditor();
        var notification = Notification.show("Patterns were added");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public void updateList() {
        getItems().setItems(orderService.getOrder(orderEntity.getId()).getPatterns());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var orderId = event.getRouteParameters().get("orderId").get();
        this.orderEntity = orderService.load(Long.parseLong(orderId));
        updateList();
    }

    public ServiceDetailsDialog getOrderDetailsDialog() {
        return orderDetailsDialog;
    }

    public OrderEntity getOrderEntity() {
        return orderEntity;
    }

    public void setOrderEntity(OrderEntity orderEntity) {
        this.orderEntity = orderEntity;
    }

    public Button getAddButton() {
        return addButton;
    }
}