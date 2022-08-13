package com.example.application.ui.views.customer;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.predicate.CustomerSpecification;
import com.example.application.service.CustomerService;
import com.example.application.service.UserService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.customer.events.DeleteEvent;
import com.example.application.ui.views.customer.events.SaveEvent;
import com.example.application.ui.views.order.OrdersView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.vaadin.klaudeta.PaginatedGrid;

import java.util.Set;


@PageTitle("Customers")
@CssImport(value = "./views/toolbar/text-field.css", themeFor = "vaadin-text-field")
@Route(value = "customers", layout = MainView.class)
public class CustomersView extends AbstractServicesView<CustomerEntity, CustomerEntity> implements HasUrlParameter<Long> {
    private final TextField       nameFilter   = new TextField();
    private final TextField       emailFilter  = new TextField();
    private final CustomerService customerService;
    private final UserService     userService;
    private final CustomerForm    customerForm;
    private final Button          addButton    = new Button("Add customer");
    private final Button          filterButton = new Button("Filter");
    private final Span            span         = new Span("Customers");

    public CustomersView(CustomerService customerService, UserService userService) {
        super(new PaginatedGrid<>(CustomerEntity.class), customerService);
        this.customerService = customerService;
        this.userService = userService;
        customerForm = new CustomerForm();
        addClassName("paging-view");
        configureGrid();
        configureForm();
        configureEvents();
        add(getToolBar(), getContent());
    }

    @Override
    protected void configureForm() {
        customerForm.setWidth("25em");
    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("customers-grid");
        getItems().setSizeFull();
        getItems().setColumns("name", "email", "phone");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setItems(customerService.findFirmCustomers());
        getItems().asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                customerForm.setEntity(e.getValue());
                customerForm.open("Edit customer");
            }
        });
        getItems().addComponentColumn((customerDTO) -> {
            Button editButton = new Button("Details");
            editButton.addClickListener(e -> {
                UI.getCurrent().navigate(OrdersView.class, customerDTO.getId());
            });
            return editButton;
        }).setHeader("Orders");

        DataProvider<CustomerEntity, Void> dataProvider =
                DataProvider.fromCallbacks(
                        query -> {
                            int offset = query.getOffset();
                            int limit = query.getLimit();
                            Set<CustomerEntity> items = customerService.filter(getSpecification(), offset, limit);
                            return items.stream();
                        },
                        query -> Math.toIntExact(customerService.countAnyMatching(getSpecification())));
        getItems().setDataProvider(dataProvider);
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");
        nameFilter.getElement().setAttribute("theme", "test");
        emailFilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("Name");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        emailFilter.setPlaceholder("Email");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);

        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            customerForm.setEntity(new CustomerEntity());
            customerForm.open("New customer");
        });

        filterButton.addClickListener(e -> getItems().setItems(customerService.filter(getSpecification())));

        HorizontalLayout filterPart = new HorizontalLayout(nameFilter, emailFilter, filterButton, addButton);
        filterPart.addClassName("filterPart");
        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        customerForm.addListener(SaveEvent.class, this::saveItem);
        customerForm.addListener(CloseEvent.class, e -> closeEditor());
        customerForm.addListener(DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        customerForm.setEntity(null);
        customerForm.close();
    }

    private void saveItem(SaveEvent event) {
        customerService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Customer was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    protected void updateList() {
        getItems().setItems(customerService.findFirmCustomers());
    }

    private void deleteItem(DeleteEvent event) {
        customerService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Customer was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long customerId) {
        if (customerId != null) {
            this.getItems().setItems(customerService.load(customerId));
        }
    }

    private CustomerSpecification getSpecification() {
        return new CustomerSpecification(userService.getUserFirm().getId(), emailFilter.getValue(), nameFilter.getValue());
    }

    public TextField getNameFilter() {
        return nameFilter;
    }

    public TextField getEmailFilter() {
        return emailFilter;
    }

    public CustomerForm getCustomerForm() {
        return customerForm;
    }

    public Button getAddButton() {
        return addButton;
    }

    public Button getFilterButton() {
        return filterButton;
    }
}
