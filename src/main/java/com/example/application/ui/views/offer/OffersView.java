package com.example.application.ui.views.offer;

import com.example.application.model.enums.OfferStatus;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.predicate.OfferSpecification;
import com.example.application.service.*;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.offer.events.DeleteEvent;
import com.example.application.ui.views.offer.events.SaveEvent;
import com.example.application.util.FormattingUtils;
import com.example.application.util.UIUtils;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.charts.model.style.SolidColor;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;
import org.springframework.core.convert.ConversionService;

import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@PageTitle("Nabídky")
@Route(value = "offers", layout = MainView.class)
public class OffersView extends AbstractServicesView<OfferEntity, OfferEntity> implements HasUrlParameter<Long> {
    private final TextField nameFilter = new TextField();
    private final ComboBox<String> customers = new ComboBox<>();
    private final ComboBox<OfferStatus> statuses = new ComboBox<>();
    private final Button addButton = new Button("Přidat nabídku");
    private final Button filterButton = new Button("Najít");
    private final Span span = new Span("Nabídky");
    private final OfferForm offerForm;
    private final ConversionService conversionService;
    private final OfferService offerService;
    private final UserService userService;
    private final CustomerService customerService;
    private final PdfGenerateService pdfGenerateService;

    public OffersView(
            OfferService offerService,
            UserService userService,
            PdfGenerateService pdfGenerateService,
            ConversionService conversionService,
            CustomerService customerService) {
        super(new Grid<>(), offerService);
        this.userService = userService;
        this.pdfGenerateService = pdfGenerateService;
        this.conversionService = conversionService;
        this.customerService = customerService;
        this.offerForm = new OfferForm();
        this.offerService = offerService;
        addClassName("paging-view");
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
        getItems().addColumn(OfferEntity::getName).setHeader("Nazev");
        getItems().addColumn(e -> conversionService.convert(e.getWorkDuration(), String.class)).setHeader("Doba montáže");
        getItems().addColumn(e -> conversionService.convert(e.getWorkCost(), String.class)).setHeader("Cena práce");
        getItems().addColumn(e -> conversionService.convert(e.getTransportationCost(), String.class)).setHeader("Cena dopravy");
        getItems().addColumn(e -> conversionService.convert(e.getMaterialsCost(), String.class)).setHeader("Cena materiálů");
        getItems().addColumn(new ComponentRenderer<>(e -> UIUtils.createLabel(conversionService.convert(e.getTotalPriceWithoutVAT(), String.class), SolidColor.GREEN)))
                .setHeader("Celkem bez DPH");
        getItems().addColumn(new ComponentRenderer<>(e -> UIUtils.createLabel(conversionService.convert(e.getTotalPriceWithVAT(), String.class), SolidColor.RED)))
                .setHeader("Celkem s DPH");
        getItems().addColumn(new LocalDateRenderer<>(OfferEntity::getCreatedDate, DateTimeFormatter.ofPattern("MMM dd, yyyy", FormattingUtils.APP_LOCALE)))
                .setHeader("Datum vystavení");
        getItems().addColumn(new ComponentRenderer<>(e -> orderStatusLabel(e.getStatus())))
                .setHeader("Status");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(OfferDetailsView.class, event.getValue().getId());
            }
        });

        getItems().addColumn(e -> e.getCustomerEntity() != null ? e.getCustomerEntity().getName() : "").setHeader("Zákaznik");

        getItems().addComponentColumn((offer) -> {
            final Button patternDetailsButton = new Button();
            patternDetailsButton.setIcon(VaadinIcon.TRASH.create());
            patternDetailsButton.addClickListener(e -> {
                offerService.delete(offer);
                updateList();
                NotificationService.success();
            });
            return new HorizontalLayout(patternDetailsButton, getDownloadAnchor(offer));
        }).setHeader("Akce");
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");

        nameFilter.getElement().setAttribute("theme", "test");
        nameFilter.setPlaceholder("Název");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        nameFilter.addKeyDownListener(Key.ENTER, (ComponentEventListener<KeyDownEvent>) keyDownEvent -> updateList());
        nameFilter.addFocusShortcut(Key.KEY_F, KeyModifier.ALT);

        statuses.getElement().setAttribute("theme", "test");
        statuses.setItems(OfferStatus.values());
        statuses.setPlaceholder("Status nabídky");
        statuses.setClearButtonVisible(true);

        customers.getElement().setAttribute("theme", "test");
        customers.setItems(customerService.getFirmCustomerNames());
        customers.setPlaceholder("Zakaznik");
        customers.setClearButtonVisible(true);

        addButton.setIcon(VaadinIcon.PLUS.create());
        addButton.addClickShortcut(Key.KEY_A, KeyModifier.ALT);
        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            offerForm.setEntity(new OfferEntity());
            offerForm.open("Nová nabídka");
        });

        filterButton.setIcon(VaadinIcon.SEARCH.create());
        filterButton.addClickListener(e -> updateList());

        HorizontalLayout filterPart = new HorizontalLayout(statuses, nameFilter, customers, filterButton, addButton);
        filterPart.setFlexGrow(1, nameFilter);
        filterPart.setFlexGrow(1, statuses);
        filterPart.setFlexGrow(1, customers);

        filterPart.addClassName("filterPart");

        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        offerForm.addListener(SaveEvent.class, this::saveItem);
        offerForm.addListener(DeleteEvent.class, this::deleteItem);
        offerForm.addListener(CloseEvent.class, e -> closeEditor());
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
        NotificationService.success();
    }

    @Override
    protected void updateList() {
        getItems().setItems(offerService.filter(getSpecification()));
    }

    private void deleteItem(DeleteEvent event) {
        offerService.delete(event.getItem());
        updateList();
        closeEditor();
        NotificationService.success();
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long customerId) {
        if (customerId != null)
            getItems().setItems(offerService.getCustomerOffers(customerId));
        else {
            DataProvider<OfferEntity, Void> dataProvider =
                    DataProvider.fromCallbacks(
                            query -> {
                                int offset = query.getOffset();
                                int limit = query.getLimit();
                                Set<OfferEntity> items = offerService.filter(getSpecification(), offset, limit);
                                return items.stream();
                            },
                            query -> Math.toIntExact(offerService.countAnyMatching(getSpecification())));
            getItems().setDataProvider(dataProvider);
        }
    }

    private OfferSpecification getSpecification() {
        return new OfferSpecification(userService.getUserFirm(AuthService.getUsername()).getId(), nameFilter.getValue(), statuses.getValue(), customers.getValue());
    }

    private Anchor getDownloadAnchor(OfferEntity offer) {
        Anchor download = new Anchor(new StreamResource("nabídka.pdf", (InputStreamFactory) () -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Nabídka");
            data.put("customer", offer.getCustomerEntity());
            data.put("firm", offer.getFirmEntity());
            data.put("firmAddress",
                    Stream.of(
                            offer.getFirmEntity().getStreet(),
                            Stream.of(offer.getFirmEntity().getPostCode(), offer.getFirmEntity().getCity())
                                    .filter(it -> it != null && !it.isEmpty()).collect(Collectors.joining(" ")),
                            offer.getFirmEntity().getState())
                            .filter(it -> it != null && !it.isEmpty())
                            .collect(Collectors.joining(", ")));
            data.put("createdDate", offer.getCreatedDate());
            data.put("workCost", conversionService.convert(offer.getWorkCost(), String.class));
            data.put("materialCost", conversionService.convert(offer.getMaterialsCost(), String.class));
            data.put("transportationCost", conversionService.convert(offer.getTransportationCost(), String.class));
            data.put("totalPriceWithoutVAT", conversionService.convert(offer.getTotalPriceWithoutVAT(), String.class));
            data.put("totalPriceWithVAT", conversionService.convert(offer.getTotalPriceWithVAT(), String.class));
            data.put("patterns", offer.getOfferPatterns());
            data.put("settings", offer.getFirmEntity().getFirmSettings());
            return pdfGenerateService.exportPdf("offer", data);
        }), "");
        download.add(new Button(new Icon(VaadinIcon.DOWNLOAD_ALT)));
        download.getElement().setAttribute("download", true);
        return download;
    }

    private Label orderStatusLabel(OfferStatus status) {
        switch (status) {
            case IN_PROGRESS:
                return UIUtils.createLabel(status.getValue(), SolidColor.YELLOW);
            case DONE:
                return UIUtils.createLabel(status.getValue(), SolidColor.GREEN);
            case CANCELED:
                return UIUtils.createLabel(status.getValue(), SolidColor.RED);
            case SENT:
                return UIUtils.createLabel(status.getValue(), SolidColor.BLUE);
            default:
                return UIUtils.createLabel(status.getValue(), SolidColor.WHITE);
        }
    }

    public TextField getNameFilter() {
        return nameFilter;
    }

    public Button getAddButton() {
        return addButton;
    }

    public OfferForm getOfferForm() {
        return offerForm;
    }

    public Button getFilterButton() {
        return filterButton;
    }
}
