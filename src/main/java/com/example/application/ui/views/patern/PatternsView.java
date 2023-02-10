package com.example.application.ui.views.patern;

import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.predicate.PatternSpecification;
import com.example.application.service.AuthService;
import com.example.application.service.FinancialService;
import com.example.application.service.ImportService;
import com.example.application.service.PatternService;
import com.example.application.service.UserService;
import com.example.application.ui.components.NotificationService;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.example.application.ui.views.patern.events.DeleteEvent;
import com.example.application.ui.views.patern.events.SaveEvent;
import com.example.application.utils.UIUtils;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.KeyDownEvent;
import com.vaadin.flow.component.KeyModifier;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import org.springframework.core.convert.ConversionService;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@PageTitle("Items")
@CssImport(value = "./views/toolbar/text-field.css")
@CssImport(value = "./views/menu-bar.css", themeFor = "vaadin-menu-bar")
@Route(value = "items", layout = MainView.class)
@RouteAlias(value = "", layout = MainView.class)
public class PatternsView extends AbstractServicesView<PatternEntity, PatternEntity> {
    private final PatternService patternService;
    private final FinancialService financialService;
    private final UserService userService;
    private final Button addButton = new Button("Přidat položku");
    private final Button filterButton = new Button("Najít");
    final TextField nameFilter = new TextField();
    private final Span span = new Span("Položky");
    private final PatternForm patternForm = new PatternForm();
    private final ConversionService conversionService;

    public PatternsView(
            PatternService patternService,
            FinancialService financialService,
            UserService userService,
            ConversionService conversionService
    ) {
        super(new Grid<>(), patternService);
        this.patternService = patternService;
        this.financialService = financialService;
        this.userService = userService;
        this.conversionService = conversionService;
        configureEvents();
        configureGrid();
        add(getToolBar(), getContent());
    }

    @Override
    protected void configureForm() {
    }

    public void configureGrid() {
        getItems().addClassName("items-grid");
        getItems().addColumn(PatternEntity::getId).setHeader("Kód").setFlexGrow(0).setWidth("100px");
        getItems().addColumn(PatternEntity::getName).setHeader("Název").setComparator(Comparator.comparing(PatternEntity::getName));
        getItems().addColumn(e -> conversionService.convert(e.getPriceWithoutVAT(), String.class))
                .setHeader("Cena bez DPH").
                setFlexGrow(0)
                .setWidth("150px");
        getItems().addColumn(e -> conversionService.convert(e.getDuration(), String.class)).setHeader("Doba montáže")
                .setFlexGrow(0)
                .setWidth("200px");
        getItems().addColumn(PatternEntity::getDescription).setHeader("Popis").setFlexGrow(0).setWidth("250px");
        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(false));
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                patternForm.setEntity(event.getValue());
                patternForm.open("Editace");
            }
        });
//        getItems().setItems(patternService.filter(getSpecification()));
        DataProvider<PatternEntity, Void> dataProvider =
                DataProvider.fromCallbacks(
                        query -> {
                            int offset = query.getOffset();
                            int limit = query.getLimit();
                            query.getSortOrders();
                            Set<PatternEntity> items = patternService.filter(getSpecification(), offset, limit);
                            return items.stream();
                        },
                        query -> Math.toIntExact(patternService.countAnyMatching(getSpecification())));
        getItems().setDataProvider(dataProvider);
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");
        nameFilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("Název");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        nameFilter.addKeyDownListener(Key.ENTER, (ComponentEventListener<KeyDownEvent>) keyDownEvent -> updateList());
        nameFilter.addFocusShortcut(Key.KEY_F, KeyModifier.CONTROL);


        addButton.setIcon(VaadinIcon.PLUS.create());
        addButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            patternForm.setEntity(new PatternEntity());
            patternForm.open("Nová položka");
        });

        filterButton.setIcon(VaadinIcon.SEARCH.create());
        filterButton.addClickListener(e -> updateList());
        HorizontalLayout filterPart = new HorizontalLayout(nameFilter, filterButton, addButton);
        filterPart.addClassName("filterPart");
        filterPart.setFlexGrow(1, nameFilter);
        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        patternForm.addListener(SaveEvent.class, this::save);
        patternForm.addListener(CloseEvent.class, e -> closeEditor());
        patternForm.addListener(DeleteEvent.class, this::delete);
    }

    @Override
    protected void updateList() {
        getItems().setItems(patternService.filter(getSpecification()));
    }

    @Override
    protected void closeEditor() {
        patternForm.setEntity(null);
        patternForm.close();
    }

//    private MenuBar getOptionsMenuBar() {
//        MenuBar menuBar = new MenuBar();
//        menuBar.getElement().setAttribute("theme", "menu-button");
//        MenuItem options = menuBar.addItem("Ostatní");
//        SubMenu subItems = options.getSubMenu();
//
//        MemoryBuffer memoryBuffer = new MemoryBuffer();
//
//        Upload upload = new Upload(memoryBuffer);
//        upload.setDropAllowed(false);
//        upload.setAcceptedFileTypes("text/csv", ".csv");
//        upload.addFinishedListener(e -> {
//            InputStream inputStream = memoryBuffer.getInputStream();
//            importService.importPatterns(inputStream);
//        });
//        MenuItem importItem = subItems.addItem(upload);
//        importItem.setCheckable(true);
//        return menuBar;
//    }

    private void save(SaveEvent event) {
        patternService.save(event.getItem());
        closeEditor();
        updateList();
        NotificationService.success();
    }

    private void delete(DeleteEvent event) {
        patternService.delete(event.getItem());
        closeEditor();
        updateList();
        NotificationService.success();
    }

    private PatternSpecification getSpecification() {
        return new PatternSpecification(userService.getUserFirm(AuthService.getUsername()).getId(), nameFilter.getValue());
    }

    public PatternForm getPatternForm() {
        return patternForm;
    }
}
