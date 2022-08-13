package com.example.application.ui.views.offer;

import com.example.application.dto.DPH;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.repository.FileRepository;
import com.example.application.service.CustomerService;
import com.example.application.service.FinancialService;
import com.example.application.service.JobOrderService;
import com.example.application.service.OfferService;
import com.example.application.service.PdfGenerateServiceImpl;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.customer.CustomersView;
import com.example.application.ui.views.offer.events.DeleteEvent;
import com.example.application.ui.views.offer.events.SaveEvent;
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

@PageTitle("Offers")
@Route(value = "offers", layout = MainView.class)
public class OffersView extends AbstractServicesView<OfferEntity, OfferEntity> implements HasUrlParameter<String> {
    private final TextField            filterItem = new TextField();
    private final OfferService         offerService;
    private final CustomerService      customerService;
    private final JobOrderService      jobOrderService;
    private final Button               addButton  = new Button("Add offer");
    private final OfferDocumentsDialog offerDocumentsDialog;
    private final Span                 span       = new Span("Offers");
    private final OfferForm            offerForm;

    public OffersView(
            OfferService offerService,
            JobOrderService jobOrderService1, CustomerService customerService,
            FileRepository fileRepository,
            JobOrderService jobOrderService,
            PdfGenerateServiceImpl pdfGenerateService
    ) {
        super(new Grid<>(OfferEntity.class, false), offerService);
        this.jobOrderService = jobOrderService1;
        this.offerForm = new OfferForm(jobOrderService, customerService, pdfGenerateService);
        this.offerService = offerService;
        this.customerService = customerService;
        this.offerDocumentsDialog = new OfferDocumentsDialog(fileRepository);
        addClassName("offers-view");
        configureGrid();
        configureEvents();
        add(getToolBar(), getContent());
    }

    @Override
    protected void configureForm() {
        offerForm.setWidth("25em");
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
        getItems().setItems(offerService.getFirmOffers());
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                offerForm.setEntity(event.getValue());
                offerForm.open("Edit offer");
            }
        });

        getItems().addComponentColumn((offer) -> {
            Select<Integer> select = new Select<>();
            select.setItems(DPH.getRates());
            select.setValue(offer.getVAT());
            select.addValueChangeListener(e -> {
                offer.setVAT(select.getValue());
                offerService.save(offer);
                updateList();
            });
            return select;
        }).setHeader("VAT");

        getItems().addComponentColumn((offer) -> {
            final Button patternDetailsButton = new Button("Details");
            patternDetailsButton.addClickListener(e -> {
                UI.getCurrent().navigate(OffersPatternsView.class, new RouteParameters("offerId", String.valueOf(offer.getId())));
            });
            return patternDetailsButton;
        }).setHeader("Patterns");

        getItems().addComponentColumn((offer) -> {
            final Button customerDetailsButton = new Button("Details");
            customerDetailsButton.addClickListener(e -> {
                if (offer.getCustomerEntity() != null)
                    UI.getCurrent().navigate(CustomersView.class, offer.getCustomerEntity().getId());
                else
                    NotificationService.error("Customer is undefined");
            });
            return customerDetailsButton;
        }).setHeader("Customer");


        getItems().addComponentColumn((offer) -> {
            final Button deleteButton = new Button("Attach");
            deleteButton.addClickListener(e -> {
                offerDocumentsDialog.setEntity(offer);
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
            offerForm.setEntity(new OfferEntity());
            offerForm.open("New offer");
        });

        HorizontalLayout filterPart = new HorizontalLayout(filterItem, addButton);
        filterPart.addClassName("filterPart");

        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        offerForm.addListener(SaveEvent.class, this::saveItem);
        offerForm.addListener(CloseEvent.class, e -> closeEditor());
        offerForm.addListener(DeleteEvent.class, this::deleteItem);
    }

    @Override
    protected void closeEditor() {
        offerForm.setEntity(null);
        offerForm.close();
    }

    private void saveItem(SaveEvent event) {
        offerService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Offer was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    protected void updateList() {
        getItems().setItems((offerService.getFirmOffers()));
    }

    private void deleteItem(DeleteEvent event) {
        span.setClassName("headline");
        offerService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Offer was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @WildcardParameter String parameter) {
        Location location = beforeEvent.getLocation();
        QueryParameters queryParameters = location.getQueryParameters();
        Map<String, List<String>> parametersMap = queryParameters.getParameters();

        if (parametersMap.containsKey("jobOrderId")) {
            getItems().setItems(jobOrderService.getJobOrderWithOffers(Long.parseLong(parametersMap.get("jobOrderId").get(0))).getOfferEntities());
        } else if (parametersMap.containsKey("customerId"))
            customerService.load(Long.parseLong(parametersMap.get("customerId").get(0)));
    }

    public TextField getFilterItem() {
        return filterItem;
    }

    public Button getAddButton() {
        return addButton;
    }

}
