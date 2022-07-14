package com.example.application.ui.views.order;

import com.example.application.dto.OrderDTO;
import com.example.application.service.CustomerService;
import com.example.application.service.OrderService;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.customer.CustomersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

@PageTitle("Orders")
@Route(value = "orders", layout = MainView.class)
public class OrdersView extends VerticalLayout implements HasUrlParameter<Long> {
    private final Grid<OrderDTO> ordersGrid = new Grid(OrderDTO.class);
    private final TextField      filterItem = new TextField();
    private final OrderService    orderService;
    private final CustomerService customerService;
    private       OrderForm       orderForm;


    public OrdersView(OrderService orderService, CustomerService customerService) {
        this.orderService = orderService;
        this.customerService = customerService;
        addClassName("orders-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureEvents();
        add(getToolBar(), getContent());
        filterItem.getElement().setAttribute("theme", "test");
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(ordersGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        orderForm = new OrderForm();
        orderForm.setWidth("25em");
    }

    private void configureGrid() {
        ordersGrid.addClassName("items-grid");
        ordersGrid.setSizeFull();
        ordersGrid.setColumns("totalPrice", "priceWithoutVat", "transportationCost", "materialsCost", "workHours", "createdAt");
        ordersGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        ordersGrid.setItems(orderService.getUserOrders());
        ordersGrid.asSingleSelect().addValueChangeListener(event -> {
            orderForm.setOrderDTO(event.getValue());
            orderForm.open("Edit order");
        });

        ordersGrid.addComponentColumn((order) -> {
            Select<Long> select = new Select<>();
            select.setItems(21L, 15L, 10L);
            select.setValue(order.getVAT());
            select.addValueChangeListener(e -> {
                order.setVAT(select.getValue());
                orderService.updateOrder(order);
                updateList();
            });
            return select;
        }).setHeader("VAT");

        ordersGrid.addComponentColumn((order) -> {
            Button details = new Button("Details");
            details.addClickListener(e -> {
                UI.getCurrent().navigate(OrdersDetailsView.class, new RouteParameters("orderId", String.valueOf(order.getId())));
            });
            return details;
        }).setHeader("Patterns");

        ordersGrid.addComponentColumn((order) -> {
            Button editButton = new Button("Details");
            editButton.addClickListener(e -> {
                    UI.getCurrent().navigate(CustomersView.class, order.getCustomer().getId());
            });
            return editButton;
        }).setHeader("Customer");

        ordersGrid.addComponentColumn((order) -> {
            Button summaryDetails = new Button("Details");
            summaryDetails.addClickListener(e -> {

            });
            return summaryDetails;
        }).setHeader("Summary");
    }

    private HorizontalLayout getToolBar() {
        filterItem.getElement().setAttribute("theme", "test");
        filterItem.setPlaceholder("Filter ...");
        filterItem.setClearButtonVisible(true);
        filterItem.setValueChangeMode(ValueChangeMode.LAZY);

        Button addContactButton = new Button("Add Order");
        addContactButton.addClickListener(e -> {
            ordersGrid.asSingleSelect().clear();
            orderForm.setOrderDTO(new OrderDTO());
            orderForm.open("New order");
        });

        HorizontalLayout filterPart = new HorizontalLayout(filterItem, addContactButton);
        filterPart.addClassName("filterPart");

        HorizontalLayout toolbar = new HorizontalLayout(new Label("Orders"), filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureEvents() {
        orderForm.addListener(OrderForm.SaveEvent.class, this::saveItem);
        orderForm.addListener(OrderForm.CloseEvent.class, e -> closeEditor());
        orderForm.addListener(OrderForm.DeleteEvent.class, this::deleteItem);
    }

    private void closeEditor() {
        orderForm.setOrderDTO(null);
        orderForm.close();
    }

    private void saveItem(OrderForm.SaveEvent event) {
        orderService.saveOrder(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Order was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    private void updateList() {
        ordersGrid.setItems(orderService.getAll());
    }

    private void deleteItem(OrderForm.DeleteEvent event) {
        orderService.deleteOrder(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Order was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long customerId) {
        if (customerId != null)
            ordersGrid.setItems(customerService.getCustomerOrders(customerId));
    }
}
