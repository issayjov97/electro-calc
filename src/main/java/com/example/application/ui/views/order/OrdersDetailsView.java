package com.example.application.ui.views.order;

import com.example.application.dto.OrderDTO;
import com.example.application.dto.PatternDTO;
import com.example.application.service.OrderService;
import com.example.application.service.PatternService;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.patern.PatternForm;
import com.google.common.collect.Sets;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;

import java.util.Objects;
import java.util.Set;

@Route(value = "order/:orderId", layout = MainView.class)
public class OrdersDetailsView extends VerticalLayout implements BeforeEnterObserver {
    private final Grid<PatternDTO>   itemsGrid = new Grid(PatternDTO.class, true);
    private final OrderService       orderService;
    private final PatternService     patternService;
    private       OrderDTO           orderDTO;
    private       OrderDetailsDialog orderDetailsDialog;

    public OrdersDetailsView(OrderService orderService, PatternService patternService) {
        this.orderService = orderService;
        this.patternService = patternService;
        this.orderDetailsDialog = new OrderDetailsDialog();
        addClassName("Patterns-view");
        setSizeFull();
        configureGrid();
        configureEvents();
        add(getToolBar(), getContent());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(itemsGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureGrid() {
        itemsGrid.addClassName("order-details-grid");
        itemsGrid.setSizeFull();
        itemsGrid.setColumns("name", "description", "duration", "priceWithoutVat");
        itemsGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        itemsGrid.addComponentColumn((patternDTO) -> {
            Button deleteButton = new Button("Delete");
            deleteButton.addClickListener(e -> {
                orderDTO.getPatterns().remove(patternDTO);
                orderService.deletePatterns(orderDTO);
                updateList();
            });
            return deleteButton;
        }).setHeader("Patterns");
    }

    private HorizontalLayout getToolBar() {
        Button addPatternButton = new Button("Add pattern");
        addPatternButton.addClickListener(e -> {
            orderDetailsDialog.setPatterns(patternService.getAll());
            itemsGrid.asSingleSelect().clear();
            orderDetailsDialog.open();
        });
        HorizontalLayout toolbar = new HorizontalLayout(addPatternButton);
        toolbar.setJustifyContentMode(JustifyContentMode.BETWEEN);
        toolbar.setAlignItems(Alignment.BASELINE);
        toolbar.getStyle().set("max-width", "100%");
        toolbar.addClassName("toolbar");
        return new HorizontalLayout(new Label("Order Patterns"), toolbar);
    }

//    private VerticalLayout getSummary() {
//        H2 summary = new H2("Order summary");
//        Span priceWithVAT = new Span("Price with VAT:");
//        Span priceWithoutVAT = new Span("Price without VAT:");
//        Span transportation = new Span("Transportation:");
//        VerticalLayout content = new VerticalLayout(priceWithVAT, priceWithoutVAT, transportation);
//        content.setSpacing(false);
//        content.setPadding(false);
//        return content;
//    }

    private void configureEvents() {
        orderDetailsDialog.addListener(OrderDetailsDialog.SaveEvent.class, this::saveSelectedItems);
        orderDetailsDialog.addListener(OrderDetailsDialog.CloseEvent.class, e -> closeEditor());
    }

    private void saveSelectedItems(OrderDetailsDialog.SaveEvent event) {
        System.out.println("Selected items:" + event.getPatterns());
        var selectedPatterns = event.getPatterns();
        patternService.addOrderPatterns(selectedPatterns, orderDTO);
        updateList();
        closeEditor();
        var notification = Notification.show("Patterns were added");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    private void deleteItem(PatternForm.DeleteEvent event) {
        System.out.println("Delete event triggered");
        System.out.println(event.getItem());
    }

    private void editItem(PatternDTO item) {
        if (Objects.isNull(item)) {
            System.out.println("Item is null");
        } else {
//            patternForm.setPatternDTO(item);
        }
    }

    private void closeEditor() {
        orderDetailsDialog.close();
        orderDetailsDialog.clearPatterns();
    }


    public void updateList() {
        this.orderDTO = orderService.getOrderById(orderDTO.getId());
        itemsGrid.setItems(orderDTO.getPatterns());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        var orderId = event.getRouteParameters().get("orderId").get();
        this.orderDTO = orderService.getOrderById(Long.valueOf(orderId));
        itemsGrid.setItems(orderDTO.getPatterns());
    }

}