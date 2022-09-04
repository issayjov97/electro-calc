package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.persistence.entity.OfferPattern;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.predicate.PatternSpecification;
import com.example.application.service.AuthService;
import com.example.application.service.CustomerService;
import com.example.application.service.FinancialService;
import com.example.application.service.OfferService;
import com.example.application.service.PatternService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.offer.events.SaveOfferDetailsEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.OptionalParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.core.convert.ConversionService;

@PageTitle("Items")
@CssImport(value = "./views/toolbar/text-field.css", themeFor = "vaadin-text-field")
@CssImport(value = "./views/menu-bar.css", themeFor = "vaadin-menu-bar")
@Route(value = "item", layout = MainView.class)
public class OfferDetailsView extends AbstractServicesView<PatternEntity, PatternEntity> implements HasUrlParameter<Long> {
    private final PatternService      patternService;
    private final UserService         userService;
    private final Button              filterButton         = new Button("Najít");
    final         TextField           nameFilter           = new TextField();
    private final Span                span                 = new Span("Položky");
    private       Dialog              dialog               = new Dialog();
    final         IntegerField        count                = new IntegerField("Počet");
    private final Button              saveButton           = new Button("Uložit");
    private final Button              saveCountButton      = new Button("Uložit");
    private final Grid<OfferPattern>  grid                 = new Grid<>();
    private final Binder<OfferEntity> binder               = new BeanValidationBinder<>(OfferEntity.class);
    private final TextField           materialCost         = new TextField();
    private final TextField           transportationCost   = new TextField();
    private final TextField           workCost             = new TextField();
    private final TextField           workHours            = new TextField();
    private final TextField           totalPriceWithoutDPH = new TextField();
    private final TextField           totalPriceWithDPH    = new TextField();
    private final TextField           saleWithVAT          = new TextField();
    private final TextField           saleWithoutVAT       = new TextField();
    private final TextField           priceWithDPH         = new TextField();
    private final TextField           priceWithoutDPH      = new TextField();
    private final OfferDetailsForm    offerDetailsForm;
    private final CustomerService     customerService;
    private final FinancialService    financialService;
    private final OfferService        offerService;
    private       OfferEntity         offerEntity;

    private       PatternEntity       selectedPattern;
    private       OfferPattern        selectedOfferPattern;
    private final ConversionService   conversionService;

    public OfferDetailsView(
            PatternService patternService,
            UserService userService,
            CustomerService customerService,
            FinancialService financialService, OfferService offerService,
            ConversionService conversionService) {
        super(new Grid<>(), patternService);
        this.patternService = patternService;
        this.userService = userService;
        this.customerService = customerService;
        this.financialService = financialService;
        this.offerService = offerService;
        this.conversionService = conversionService;
        this.offerDetailsForm = new OfferDetailsForm(this.customerService);
        configureGrid();
        configureEvents();
        configureForm();
        add(getContent());
    }

    private void fillOfferSummary() {
        workCost.setValue(conversionService.convert(offerEntity.getWorkCost(), String.class));
        materialCost.setValue(conversionService.convert(offerEntity.getMaterialsCost(), String.class));
        transportationCost.setValue(conversionService.convert(offerEntity.getTransportationCost(), String.class));
        workHours.setValue(conversionService.convert(offerEntity.getWorkDuration(), String.class));
        priceWithoutDPH.setValue(conversionService.convert(offerEntity.getPriceWithoutVAT(), String.class));
        priceWithDPH.setValue(conversionService.convert(offerEntity.getPriceWithVAT(), String.class));
        totalPriceWithoutDPH.setValue(conversionService.convert(offerEntity.getTotalPriceWithoutVAT(), String.class));
        totalPriceWithDPH.setValue(conversionService.convert(offerEntity.getTotalPriceWithVAT(), String.class));
        saleWithoutVAT.setValue(conversionService.convert(financialService.offerSaleValueWithoutDPH(offerEntity), String.class));
        saleWithVAT.setValue(conversionService.convert(financialService.offerSaleValueWithDPH(offerEntity), String.class));
    }

    @Override
    protected void configureForm() {
        VerticalLayout verticalLayout = new VerticalLayout();
        verticalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        verticalLayout.setAlignItems(Alignment.CENTER);
        verticalLayout.add(count, saveCountButton);
        count.setValue(1);
        count.setStep(1);
        count.setMin(1);
        count.setMax(100);
        count.setHasControls(true);
        dialog.add(verticalLayout);
    }

    @Override
    protected void configureGrid() {
        getItems().setSizeFull();
        getItems().addColumn(PatternEntity::getName).setHeader("Název");
        getItems().addColumn(e -> conversionService.convert(e.getPriceWithoutVAT(), String.class)).setHeader("Cena bez DPH");
        getItems().addColumn(e -> conversionService.convert(e.getDuration(), String.class)).setHeader("Doba trvání");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().asSingleSelect().addValueChangeListener(event -> {
            var value = event.getValue();
            if (value != null) {
                selectedPattern = value;
                if (grid.getDataProvider().fetch(new Query<>()).anyMatch(e -> e.getPatternEntity().equals(value))) {
                    NotificationService.error("Nabídka už obsahuje danou položku");
                } else {
                    dialog.open();
                    count.setValue(1);
                }
            }
        });

        saveButton.addClickListener(e -> {
            offerService.save(offerEntity);
        });

        saveCountButton.addClickListener(e -> {
            if (selectedPattern != null) {
                offerService.addOfferPattern(selectedPattern, offerEntity, count.getValue());
                selectedPattern = null;
                grid.setItems(offerEntity.getPatterns());
            } else if (selectedOfferPattern != null) {
                selectedOfferPattern.setCount(count.getValue());
                offerService.updateOfferPatternsCount(selectedOfferPattern);
                selectedOfferPattern = null;
            }
            dialog.close();
            updateOffer();
            grid.setItems(offerEntity.getOfferPatterns());
            fillOfferSummary();
            NotificationService.success();
        });
        getItems().
                setItems(patternService.filter(getSpecification()));
    }

    @Override
    public Component getContent() {
        final VerticalLayout verticalLayout = new VerticalLayout();
        final VerticalLayout main = new VerticalLayout();
        H3 headline = new H3("Detail nabídky");
        headline.setWidthFull();
        HorizontalLayout header = new HorizontalLayout(headline);
        header.setWidthFull();
        verticalLayout.add(header, configureOfferPatternsGrid(), offerDetailsForm, getOrderDetails());
        main.add(getToolBar(), super.getContent(), verticalLayout);
        final HorizontalLayout horizontalLayout = new HorizontalLayout(main, verticalLayout);
        horizontalLayout.getStyle().set("background-color", "white");
        horizontalLayout.setSizeFull();
        horizontalLayout.addClassName("offer-details");
        return horizontalLayout;
    }

    public Component getOrderDetails() {
        materialCost.setReadOnly(true);
        transportationCost.setReadOnly(true);
        workCost.setReadOnly(true);
        workHours.setReadOnly(true);
        totalPriceWithoutDPH.setReadOnly(true);
        totalPriceWithDPH.setReadOnly(true);
        saleWithVAT.setReadOnly(true);
        saleWithoutVAT.setReadOnly(true);
        priceWithDPH.setReadOnly(true);
        priceWithoutDPH.setReadOnly(true);
        priceWithoutDPH.setSuffixComponent(new Span("bez DPH"));
        priceWithDPH.setSuffixComponent(new Span("s DPH"));
        saleWithoutVAT.setSuffixComponent(new Span("bez DPH"));
        saleWithVAT.setSuffixComponent(new Span("s DPH"));
        totalPriceWithoutDPH.setSuffixComponent(new Span("bez DPH"));
        totalPriceWithDPH.setSuffixComponent(new Span("s DPH"));
        totalPriceWithoutDPH.getElement().setAttribute("theme", "without-vat");
        totalPriceWithDPH.getElement().setAttribute("theme", "with-vat");


        var priceWithoutVATLabel = new Label("Celkem bez DPH");
        priceWithoutVATLabel.setWidth("120px");
        var priceWthVATLabel = new Label("Celkem s DPH");
        priceWthVATLabel.setWidth("120px");

        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(workCost, "Cena montáže:");
        formLayout.addFormItem(workHours, "Doba montáže:");
        formLayout.addFormItem(materialCost, "Cena materiálů:");
        formLayout.addFormItem(transportationCost, "Cena dopravy:");
        formLayout.addFormItem(priceWithoutDPH, "Cena před slevou:");
        formLayout.addFormItem(priceWithDPH, "");
        formLayout.addFormItem(saleWithoutVAT, "Výše slevy:");
        formLayout.addFormItem(saleWithVAT, "");
        formLayout.addFormItem(totalPriceWithoutDPH, "Celkem:");
        formLayout.addFormItem(totalPriceWithDPH, "");
        return new VerticalLayout(formLayout);
    }

    public Component configureOfferPatternsGrid() {
        grid.setWidthFull();
        grid.addColumn(OfferPattern::getCount).setHeader("Počet").setFlexGrow(0).setWidth("80px");
        grid.addColumn(e -> e.getPatternEntity().getName()).setHeader("Název").setFlexGrow(0).setWidth("200px");
        grid.addColumn(e -> conversionService.convert(e.getMaterialsCost(), String.class)).setHeader("Cena materiálů").setFlexGrow(0).setWidth("100px");
        grid.addColumn(e -> conversionService.convert(e.getWorkCost(), String.class)).setHeader("Cena práce").setFlexGrow(0).setWidth("100px");

        grid.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        GridContextMenu<OfferPattern> menu = grid.addContextMenu();
        menu.addItem("Upravit", event -> {
            if (event.getItem().isPresent()) {
                selectedOfferPattern = event.getItem().get();
                dialog.open();
                count.setValue(1);
            }
        });
        menu.addItem("Odstranit", event -> {
            var item = event.getItem();
            if (item.isPresent()) {
                this.offerEntity = offerService.deleteOfferPattern(item.get());
                grid.setItems(offerEntity.getPatterns());
                fillOfferSummary();
                NotificationService.success();
            }
        });
        return grid;
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");

        nameFilter.setPlaceholder("Název");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        nameFilter.addKeyDownListener(Key.ENTER, (ComponentEventListener<KeyDownEvent>) keyDownEvent -> updateList());
        nameFilter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);

        filterButton.setIcon(VaadinIcon.SEARCH.create());
        filterButton.addClickListener(e -> {
            updateList();
        });
        HorizontalLayout filterPart = new HorizontalLayout(nameFilter, filterButton);
        filterPart.setFlexGrow(1, nameFilter);

        filterPart.addClassName("filterPart");

        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        offerDetailsForm.addListener(SaveOfferDetailsEvent.class, this::saveDetailsItem);
    }

    @Override
    protected void updateList() {
        getItems().setItems(patternService.filter(new PatternSpecification(userService.getUserFirm(AuthService.getUsername()).getId(), nameFilter.getValue())));
    }

    @Override
    protected void closeEditor() {
    }

    private void saveDetailsItem(SaveOfferDetailsEvent event) {
        offerService.save(event.getItem());
        closeEditor();
        updateOffer();
        fillOfferSummary();
        NotificationService.success();
    }

    private void updateOffer() {
        this.offerEntity = offerService.getOfferWithFullPatterns(offerEntity.getId());
    }


    private PatternSpecification getSpecification() {
        return new PatternSpecification(userService.getUserFirm(AuthService.getUsername()).getId(), nameFilter.getValue());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long offerId) {
        if (offerId != null) {
            this.offerEntity = offerService.getOfferWithFullPatterns(offerId);
            offerDetailsForm.setEntity(offerEntity);
            binder.readBean(offerEntity);
            grid.setItems(offerEntity.getPatterns());
            fillOfferSummary();
        }
    }
}
