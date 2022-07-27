package com.example.application.ui.views.demand;

import com.example.application.dto.DPH;
import com.example.application.persistence.entity.DemandEntity;
import com.example.application.persistence.repository.FileRepository;
import com.example.application.service.CustomerService;
import com.example.application.service.DemandService;
import com.example.application.service.FinancialService;
import com.example.application.service.JobOrderService;
import com.example.application.service.PdfGenerateServiceImpl;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.customer.CustomersView;
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
import com.vaadin.flow.router.Location;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.WildcardParameter;

import java.util.List;
import java.util.Map;

@PageTitle("Demands")
@Route(value = "demands", layout = MainView.class)
public class DemandsView extends AbstractServicesView<DemandEntity, DemandEntity> implements HasUrlParameter<String> {
    private final TextField             filterItem = new TextField();
    private final DemandService         demandService;
    private final CustomerService       customerService;
    private final JobOrderService       jobOrderService;
    private final Button                addButton  = new Button("Add demand");
    private final DemandDocumentsDialog offerDocumentsDialog;
    private final Span                  span       = new Span("Demands");
    private final DemandForm            demandForm;

    public DemandsView(
            DemandService demandService,
            CustomerService customerService,
            JobOrderService jobOrderService,
            FileRepository fileRepository,
            PdfGenerateServiceImpl pdfGenerateService
    ) {
        super(new Grid<>(DemandEntity.class, false), demandService);
        this.jobOrderService = jobOrderService;
        this.demandForm = new DemandForm(pdfGenerateService, jobOrderService, customerService);
        this.demandService = demandService;
        this.customerService = customerService;
        this.offerDocumentsDialog = new DemandDocumentsDialog(fileRepository);
        addClassName("demands-view");
        configureGrid();
        configureEvents();
        add(getToolBar(), getContent());
    }

    @Override
    protected void configureForm() {
        demandForm.setWidth("25em");
    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("demands-grid");
        getItems().setSizeFull();
        getItems().setColumns("transportationCost", "materialsCost", "workHours", "createdAt");
        getItems().addColumn(FinancialService::calculateServicePriceWithoutVAT).setHeader("Price without VAT");
        getItems().addColumn(FinancialService::calculatePriceWithVat).setHeader("Price with VAT");
        getItems().addColumn(FinancialService::calculatePriceWithVat).setHeader("Total price");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().setItems(demandService.getUserOffers());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                demandForm.setEntity(event.getValue());
                demandForm.open("Edit demand");
            }
        });

        getItems().addComponentColumn((demand) -> {
            Select<Integer> select = new Select<>();
            select.setItems(DPH.getRates());
            select.setValue(demand.getVAT());
            select.addValueChangeListener(e -> {
                demand.setVAT(select.getValue());
                demandService.save(demand);
                updateList();
            });
            return select;
        }).setHeader("VAT");

        getItems().addComponentColumn((demand) -> {
            final Button patternDetailsButton = new Button("Details");
            patternDetailsButton.addClickListener(e -> {
                UI.getCurrent().navigate(DemandsPatternsView.class, new RouteParameters("demandId", String.valueOf(demand.getId())));
            });
            return patternDetailsButton;
        }).setHeader("Patterns");

        getItems().addComponentColumn((demand) -> {
            final Button customerDetailsButton = new Button("Details");
            customerDetailsButton.addClickListener(e -> {
                UI.getCurrent().navigate(CustomersView.class, demand.getCustomerEntity().getId());
            });
            return customerDetailsButton;
        }).setHeader("Customer");


        getItems().addComponentColumn((demand) -> {
            final Button deleteButton = new Button("Attach");
            deleteButton.addClickListener(e -> {
                offerDocumentsDialog.setOfferEntity(demand);
                offerDocumentsDialog.open();
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
            demandForm.setEntity(new DemandEntity());
            demandForm.open("New demand");
        });

        HorizontalLayout filterPart = new HorizontalLayout(filterItem, addButton);
        filterPart.addClassName("filterPart");

        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        demandForm.addListener(DemandForm.SaveEvent.class, this::saveItem);
        demandForm.addListener(DemandForm.CloseEvent.class, e -> closeEditor());
        demandForm.addListener(DemandForm.DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        demandForm.setEntity(null);
        demandForm.close();
    }

    private void saveItem(DemandForm.SaveEvent event) {
        demandService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Demand was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    protected void updateList() {
        getItems().setItems((demandService.getUserOffers()));
    }

    private void deleteItem(DemandForm.DeleteEvent event) {
        demandService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Demand was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @WildcardParameter String parameter) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();

        if (parametersMap.containsKey("jobOrderId"))
            getItems().setItems(jobOrderService.load(Long.parseLong(parametersMap.get("jobOrderId").get(0))).getDemandEntities());
        else if (parametersMap.containsKey("customerId"))
            customerService.load(Long.parseLong(parametersMap.get("customerId").get(0)));
    }

    public TextField getFilterItem() {
        return filterItem;
    }

    public Button getAddButton() {
        return addButton;
    }

}
