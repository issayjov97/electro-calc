package com.example.application.ui.views.customer;

import com.example.application.dto.CustomerDTO;
import com.example.application.service.CustomerService;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.order.OrdersDetailsView;
import com.example.application.ui.views.order.OrdersView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.shared.Registration;

import java.util.Objects;
import java.util.Optional;


@PageTitle("Customers")
@CssImport(value = "./views/toolbar/text-field.css", themeFor = "vaadin-text-field")
@Route(value = "customers", layout = MainView.class)
public class CustomersView extends VerticalLayout implements HasUrlParameter<Long> {
    private final Grid<CustomerDTO> customersGrid = new Grid(CustomerDTO.class);
    private final TextField         nameFilter    = new TextField();
    private final TextField         emailFilter   = new TextField();
    private final CustomerService   customerService;
    private       CustomerForm      customerForm;

    public CustomersView(CustomerService customerService) {
        this.customerService = customerService;
        addClassName("customers-view");
        setSizeFull();
        configureGrid();
        configureForm();
        configureEvents();
        add(getToolBar(), getContent());
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(customersGrid);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        customerForm = new CustomerForm();
        customerForm.setWidth("25em");
    }

    private void configureGrid() {
        customersGrid.addClassName("customers-grid");
        customersGrid.setSizeFull();
        customersGrid.setColumns("name", "email", "phone");
        customersGrid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        customersGrid.setItems(customerService.findUserCustomers());
        customersGrid.asSingleSelect().addValueChangeListener(e -> {
            if (e.getValue() != null) {
                customerForm.setCustomerDTO(e.getValue());
                customerForm.open("Edit customer");
            }
        });
        customersGrid.addComponentColumn((customerDTO) -> {
            Button editButton = new Button("Details");
            editButton.addClickListener(e -> {
                UI.getCurrent().navigate(OrdersView.class, customerDTO.getId());
            });
            return editButton;
        }).setHeader("Orders");
    }

    private HorizontalLayout getToolBar() {
        nameFilter.getElement().setAttribute("theme", "test");
        emailFilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("Name");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        emailFilter.setPlaceholder("Email");
        emailFilter.setClearButtonVisible(true);
        emailFilter.setValueChangeMode(ValueChangeMode.LAZY);

        Button addCustomerButton = new Button("Add customer");
        Button filterButton = new Button("Filter");

        addCustomerButton.addClickListener(e -> {
            customersGrid.asSingleSelect().clear();
            customerForm.setCustomerDTO(new CustomerDTO());
            customerForm.open("New customer");
        });

        filterButton.addClickListener(e -> customersGrid.setItems(customerService.filter(nameFilter.getValue(), emailFilter.getValue())));

        HorizontalLayout filterPart = new HorizontalLayout(nameFilter, emailFilter, filterButton, addCustomerButton);
        filterPart.addClassName("filterPart");
        HorizontalLayout toolbar = new HorizontalLayout(new Label("Customers"), filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void configureEvents() {
        customerForm.addListener(CustomerForm.SaveEvent.class, this::saveItem);
        customerForm.addListener(CustomerForm.CloseEvent.class, e -> closeEditor());
        customerForm.addListener(CustomerForm.DeleteEvent.class, this::deleteItem);
    }

    private void closeEditor() {
        customerForm.setCustomerDTO(null);
        customerForm.close();
    }

    private void saveItem(CustomerForm.SaveEvent event) {
        customerService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Customer was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    private void updateList() {
        customersGrid.setItems(customerService.findUserCustomers());
    }

    private void deleteItem(CustomerForm.DeleteEvent event) {
        customerService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Customer was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    private void editItem(CustomerDTO item) {
        if (Objects.isNull(item)) {
            System.out.println("Item is null");
        } else {
            customerForm.setCustomerDTO(item);
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long customerId) {
        if (customerId != null) {
            this.customersGrid.setItems(customerService.findById(customerId));
        }
    }

    public static abstract class CustomerOrdersEvent extends ComponentEvent<CustomersView> {
        private CustomerDTO item;

        protected CustomerOrdersEvent(CustomersView source, CustomerDTO item) {
            super(source, false);
            this.item = item;
        }

        public CustomerDTO getItem() {
            return item;
        }
    }

    public static class EditEvent extends CustomerOrdersEvent {
        EditEvent(CustomersView source, CustomerDTO contact) {
            super(source, contact);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
    }
}
