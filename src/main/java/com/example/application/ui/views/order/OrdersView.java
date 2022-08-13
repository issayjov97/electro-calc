package com.example.application.ui.views.order;

import com.example.application.dto.DPH;
import com.example.application.persistence.entity.OrderEntity;
import com.example.application.persistence.repository.FileRepository;
import com.example.application.service.CustomerService;
import com.example.application.service.FinancialService;
import com.example.application.service.JobOrderService;
import com.example.application.service.OrderService;
import com.example.application.service.PdfGenerateServiceImpl;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.customer.CustomersView;
import com.example.application.ui.views.order.events.DeleteEvent;
import com.example.application.ui.views.order.events.SaveEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
public class OrdersView extends AbstractServicesView<OrderEntity, OrderEntity> implements HasUrlParameter<Long> {
    private final TextField            filterItem = new TextField();
    private final OrderService         orderService;
    private final CustomerService      customerService;
    private final Button               addButton  = new Button("Add Order");
    private final OrderDocumentsDialog orderDocumentsDialog;
    private final Span                 span       = new Span("Orders");
    private final OrderForm            orderForm;

    public OrdersView(
            OrderService orderService,
            JobOrderService jobOrderService, CustomerService customerService,
            FileRepository fileRepository,
            PdfGenerateServiceImpl pdfGenerateService
    ) {
        super(new Grid<>(OrderEntity.class), orderService);
        this.orderForm = new OrderForm(pdfGenerateService, jobOrderService, customerService);
        this.orderService = orderService;
        this.customerService = customerService;
        this.orderDocumentsDialog = new OrderDocumentsDialog(fileRepository);
        addClassName("orders-view");
        configureGrid();
        configureEvents();
        add(getToolBar(), getContent());
    }

    @Override
    protected void configureForm() {
        orderForm.setWidth("25em");
    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("items-grid");
        getItems().setSizeFull();
        getItems().setColumns("transportationCost", "materialsCost", "workHours", "createdAt");
        getItems().addColumn(FinancialService::calculateServicePriceWithoutVAT).setHeader("Price without VAT");
        getItems().addColumn(FinancialService::calculatePriceWithVat).setHeader("Price with VAT");
        getItems().addColumn(FinancialService::calculatePriceWithVat).setHeader("Total price");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setItems(orderService.getFirmOrders());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            orderForm.setEntity(event.getValue());
            orderForm.open("Edit order");
        });

        getItems().addComponentColumn((order) -> {
            Select<Integer> select = new Select<>();
            select.setItems(DPH.getRates());
            select.setValue(order.getVAT());
            select.addValueChangeListener(e -> {
                order.setVAT(select.getValue());
                orderService.save(order);
                updateList();
            });
            return select;
        }).setHeader("VAT");

        getItems().addComponentColumn((order) -> {
            final Button patternDetailsButton = new Button("Details");
            patternDetailsButton.addClickListener(e -> {
                UI.getCurrent().navigate(OrdersPatternsView.class, new RouteParameters("orderId", String.valueOf(order.getId())));
            });
            return patternDetailsButton;
        }).setHeader("Patterns");

        getItems().addComponentColumn((order) -> {
            final Button customerDetailsButton = new Button("Details");
            customerDetailsButton.addClickListener(e -> {
                if (order.getCustomerEntity() != null)
                    UI.getCurrent().navigate(CustomersView.class, order.getCustomerEntity().getId());
                else
                    NotificationService.error("Undefined customer");
            });
            return customerDetailsButton;
        }).setHeader("Customer");

        getItems().addComponentColumn((order) -> {
            final Button deleteButton = new Button("Attach");
            deleteButton.addClickListener(e -> {
                orderDocumentsDialog.setEntity(order);
                orderDocumentsDialog.open();
            });
            return deleteButton;
        }).setHeader("Documents");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");

        filterItem.getElement().setAttribute("theme", "test");
        filterItem.setPlaceholder("Filter ...");
        filterItem.setClearButtonVisible(true);
        filterItem.setValueChangeMode(ValueChangeMode.LAZY);

        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            orderForm.setEntity(new OrderEntity());
            orderForm.open("New order");
        });

        HorizontalLayout filterPart = new HorizontalLayout(filterItem, addButton);
        filterPart.addClassName("filterPart");

        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    @Override
    protected void configureEvents() {
        orderForm.addListener(SaveEvent.class, this::saveItem);
        orderForm.addListener(CloseEvent.class, e -> closeEditor());
        orderForm.addListener(DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        orderForm.setEntity(null);
        orderForm.close();
    }

    private void saveItem(SaveEvent event) {
        orderService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Order was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    protected void updateList() {
        getItems().setItems((orderService.getFirmOrders()));
    }

    private void deleteItem(DeleteEvent event) {
        orderService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Order was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long customerId) {
        if (customerId != null)
            getItems().setItems(orderService.getCustomerOrders(customerId));
    }

    public TextField getFilterItem() {
        return filterItem;
    }

    public Button getAddButton() {
        return addButton;
    }

    public OrderForm getOrderForm() {
        return orderForm;
    }
}
