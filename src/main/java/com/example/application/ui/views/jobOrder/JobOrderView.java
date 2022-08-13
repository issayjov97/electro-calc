package com.example.application.ui.views.jobOrder;

import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.service.CustomerService;
import com.example.application.service.JobOrderService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.customer.CustomersView;
import com.example.application.ui.views.jobOrder.events.DeleteEvent;
import com.example.application.ui.views.jobOrder.events.SaveEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;

import java.util.List;
import java.util.Map;

@PageTitle("Job orders")
@Route(value = "jobOrders", layout = MainView.class)
public class JobOrderView extends AbstractServicesView<JobOrderEntity, JobOrderEntity> implements HasUrlParameter<Long> {
    private final TextField       filterItem = new TextField();
    private final JobOrderService jobOrderService;
    private final CustomerService customerService;
    private final Button          addButton  = new Button("Add job order");
    private final Span            span       = new Span("Job orders");
    private final JobOrderForm    jobOrderForm;

    public JobOrderView(
            JobOrderService jobOrderService,
            CustomerService customerService
    ) {
        super(new Grid<>(JobOrderEntity.class, false), jobOrderService);
        this.jobOrderForm = new JobOrderForm();
        this.jobOrderService = jobOrderService;
        this.customerService = customerService;
        addClassName("demands-view");
        span.setSizeFull();
        configureGrid();
        configureEvents();
        add(getToolBar(), getContent());
    }

    @Override
    protected void configureForm() {
        jobOrderForm.setWidth("25em");
    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("items-grid");
        getItems().setSizeFull();
        getItems().setColumns("jobName", "createdAt", "demandDate", "offerDate", "orderDate");
//        getItems().add("0").setHeader("Total cost");
//        getItems().addColumn("0").setHeader("Total revenue");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setItems(jobOrderService.getFirmJobOrders());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                jobOrderForm.setEntity(event.getValue());
                jobOrderForm.open("Edit job order");
            }
        });

        getItems().addComponentColumn((jobOrder) -> {
            final Button patternDetailsButton = new Button("Details");
            patternDetailsButton.addClickListener(e -> {
                UI.getCurrent().navigate("demands", new QueryParameters(Map.of("jobOrderId", List.of(String.valueOf(jobOrder.getId())))));
            });
            return patternDetailsButton;
        }).setHeader("Demands");

        getItems().addComponentColumn((jobOrder) -> {
            final Button customerDetailsButton = new Button("Details");
            customerDetailsButton.addClickListener(e -> {
                UI.getCurrent().navigate("offers", new QueryParameters(Map.of("jobOrderId", List.of(String.valueOf(jobOrder.getId())))));
            });
            return customerDetailsButton;
        }).setHeader("Offers");
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
            jobOrderForm.setEntity(new JobOrderEntity());
            jobOrderForm.open("New job order");
        });

        HorizontalLayout filterPart = new HorizontalLayout(filterItem, addButton);
        filterPart.addClassName("filterPart");

        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }


    @Override
    protected void configureEvents() {
        jobOrderForm.addListener(SaveEvent.class, this::saveItem);
        jobOrderForm.addListener(CloseEvent.class, e -> closeEditor());
        jobOrderForm.addListener(DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        jobOrderForm.setEntity(null);
        jobOrderForm.close();
    }

    private void saveItem(SaveEvent event) {
        jobOrderService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Job order was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    protected void updateList() {
        getItems().setItems((jobOrderService.getFirmJobOrders()));
    }

    private void deleteItem(DeleteEvent event) {
        jobOrderService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Job order was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long customerId) {
    }

    public TextField getFilterItem() {
        return filterItem;
    }

    public Button getAddButton() {
        return addButton;
    }

}
