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
import com.example.application.ui.views.offer.events.DeleteOfferPatternEvent;
import com.example.application.ui.views.offer.events.SaveOfferDetailsEvent;
import com.example.application.ui.views.offer.events.UpdateOfferPatternEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
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
    private final PatternService patternService;
    private final UserService userService;
    private final Button filterButton = new Button("Najít");
    final TextField nameFilter = new TextField();
    private final Span span = new Span("Položky");
    private final OfferPatternForm offerPatternForm = new OfferPatternForm();
    private final OfferPatternsView offerPatterns;
    private final Binder<OfferEntity> binder = new BeanValidationBinder<>(OfferEntity.class);
    private final OfferDetailsForm offerDetailsForm;
    private final OfferSummaryForm offerSummaryForm;
    private final OfferService offerService;
    private OfferEntity offerEntity;
    private PatternEntity selectedPattern;
    private OfferPattern selectedOfferPattern;
    private final ConversionService conversionService;

    public OfferDetailsView(
            PatternService patternService,
            UserService userService,
            CustomerService customerService,
            FinancialService financialService,
            OfferService offerService,
            ConversionService conversionService) {
        super(new Grid<>(), patternService);
        this.patternService = patternService;
        this.userService = userService;
        this.offerService = offerService;
        this.conversionService = conversionService;
        this.offerDetailsForm = new OfferDetailsForm(customerService.getFirmCustomers());
        this.offerPatterns = new OfferPatternsView(offerPatternForm, conversionService);
        this.offerSummaryForm = new OfferSummaryForm(conversionService, financialService);
        configureGrid();
        configureEvents();
        configureForm();
        add(getContent());
    }

    @Override
    protected void configureForm() {
        offerDetailsForm.getSaveButton().addClickListener(e -> offerService.save(offerEntity));
        offerPatternForm.getSaveButton().addClickListener(e -> {
            if (selectedPattern != null) {
                offerService.addOfferPattern(selectedPattern, offerEntity, offerPatternForm.getCount().getValue(), offerPatternForm.getIsCabelCrossSection().getValue());
                selectedPattern = null;
                offerPatterns.getItems().setItems(offerEntity.getOfferPatterns());
            } else if (selectedOfferPattern != null) {
                selectedOfferPattern.setCount(offerPatternForm.getCount().getValue());
                selectedOfferPattern.setWithCabelCrossSection(offerPatternForm.getIsCabelCrossSection().getValue());
                offerService.updateOfferPatternsCount(selectedOfferPattern);
                selectedOfferPattern = null;
            }
            offerPatternForm.close();
            offerPatterns.getItems().setItems(offerEntity.getOfferPatterns());
            updateOffer();
            offerSummaryForm.fillOutOfferSummary();
            NotificationService.success();
        });
    }

    @Override
    protected void configureGrid() {
        getItems().setSizeFull();
        getItems().addColumn(PatternEntity::getName).setHeader("Název");
        getItems().addColumn(e -> conversionService.convert(e.getPriceWithoutVAT(), String.class)).setHeader("Cena bez DPH");
        getItems().addColumn(e -> conversionService.convert(e.getDuration(), String.class)).setHeader("Doba trvání");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().addItemDoubleClickListener(event -> {
            var item = event.getItem();
            if (item != null) {
                offerPatternForm.getIsCabelCrossSection().setValue(item.containsCabelCrossSection());
                selectedPattern = item;
                if (offerPatterns.containsItem(item)) {
                    NotificationService.error("Nabídka už obsahuje danou položku");
                } else {
                    offerPatternForm.open("");
                    offerPatternForm.getCount().setValue(1);
                }
            }
        });
        getItems().setItems(patternService.filter(getSpecification()));
    }

    @Override
    public Component getContent() {
        final VerticalLayout verticalLayout = new VerticalLayout();
        final VerticalLayout main = new VerticalLayout();
        H3 headline = new H3("Detaily nabídky");
        headline.setWidthFull();
        HorizontalLayout header = new HorizontalLayout(headline);
        header.setWidthFull();
        verticalLayout.add(header, offerPatterns, offerDetailsForm, new H3("Přehled cen"), offerSummaryForm);
        main.add(getToolBar(), super.getContent(), verticalLayout);
        final HorizontalLayout horizontalLayout = new HorizontalLayout(main, verticalLayout);
        horizontalLayout.getStyle().set("background-color", "white");
        horizontalLayout.setWidthFull();
        horizontalLayout.addClassName("offer-details");
        return horizontalLayout;
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");
        nameFilter.getElement().setAttribute("theme", "search");
        nameFilter.setPlaceholder("Název");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
        nameFilter.addKeyDownListener(Key.ENTER, (ComponentEventListener<KeyDownEvent>) keyDownEvent -> updateList());
        nameFilter.addFocusShortcut(Key.KEY_F, KeyModifier.ALT);

        filterButton.setIcon(VaadinIcon.SEARCH.create());
        filterButton.addClickListener(e -> updateList());
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
        offerPatterns.addListener(DeleteOfferPatternEvent.class, this::deleteOfferPattern);
        offerPatterns.addListener(UpdateOfferPatternEvent.class, this::setSelectedOfferPattern);
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
        offerSummaryForm.fillOutOfferSummary();
        NotificationService.success();
    }

    private void setSelectedOfferPattern(UpdateOfferPatternEvent event) {
        this.selectedOfferPattern = event.getItem();
    }

    private void deleteOfferPattern(DeleteOfferPatternEvent event) {
        offerService.deleteOfferPattern(event.getItem());
        updateOffer();
        offerPatterns.getItems().setItems(offerEntity.getOfferPatterns());
        offerSummaryForm.fillOutOfferSummary();
        offerService.calculateOfferDetails(offerEntity);
        NotificationService.success();
    }

    private void updateOffer() {
        this.offerEntity = offerService.getOfferWithFullPatterns(offerEntity.getId());
        offerPatterns.getItems().setItems(this.offerEntity.getOfferPatterns());
        offerSummaryForm.setEntity(offerEntity);
        offerDetailsForm.setEntity(offerEntity);
    }

    private PatternSpecification getSpecification() {
        return new PatternSpecification(userService.getUserFirm(AuthService.getUsername()).getId(), nameFilter.getValue());
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Long offerId) {
        if (offerId != null) {
            this.offerEntity = offerService.getOfferWithFullPatterns(offerId);
            offerDetailsForm.setEntity(offerEntity);
            offerSummaryForm.setEntity(offerEntity);
            binder.readBean(offerEntity);
            offerPatterns.getItems().setItems(offerEntity.getOfferPatterns());
            offerSummaryForm.fillOutOfferSummary();
        }
    }

    public OfferDetailsForm getOfferDetailsForm() {
        return offerDetailsForm;
    }
}
