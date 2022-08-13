package com.example.application.ui.views.patern;

import com.example.application.dto.DPH;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.ImageRepository;
import com.example.application.predicate.PatternSpecification;
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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.jpa.domain.Specification;
import org.vaadin.klaudeta.PaginatedGrid;

import java.io.InputStream;
import java.util.Set;

@PageTitle("Items")
@CssImport(value = "./views/toolbar/text-field.css")
@Route(value = "items", layout = MainView.class)
public class PatternsView extends AbstractServicesView<PatternEntity, PatternEntity> {
    private final PatternService      patternService;
    private final UserService         userService;
    private final ImportService       importService;
    private final PatternForm         patternForm = new PatternForm();
    private final PatternImagesDialog patternImagesDialog;
    Grid<PatternEntity> patterns = new PaginatedGrid<>(PatternEntity.class);
    private boolean sidebarCollapsed;
    Button button         = new Button();
    Icon   leftArrowIcon  = VaadinIcon.ARROW_LEFT.create();
    Icon   rightArrowIcon = VaadinIcon.ARROW_RIGHT.create();
    private final SplitLayout splitLayout = new SplitLayout();

    public PatternsView(PatternService patternService, UserService userService, ImportService importService, ImageRepository imageRepository) {
        super(new PaginatedGrid<>(PatternEntity.class), patternService);
        this.patternService = patternService;
        this.userService = userService;
        this.importService = importService;
        this.patternImagesDialog = new PatternImagesDialog(imageRepository, patternService);
        addClassName("paging-view");
        configureGrid();
        configureDefaultGrid();
        configureEvents();
        Div masterContainer = new Div();
        masterContainer.getStyle().set("overflow", "hidden");

        button.addClickListener(event -> {
            sidebarCollapsed = !sidebarCollapsed;
            updateSidebar();
        });
        button.getElement().setAttribute("aria-label", "Expand/collapse sidebar");
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        button.getStyle().set("float", "right");
        masterContainer.add(button, getUser());
        splitLayout.addToPrimary(masterContainer);
        splitLayout.addToSecondary(getDefault());
        updateSidebar();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.setWidth("100%");
        horizontalLayout.add(splitLayout);
        add(horizontalLayout, patternForm);

    }

    @Override
    protected void configureForm() {

    }

    @Override
    protected void configureGrid() {
        getItems().setSizeFull();
        getItems().addClassName("items-grid");
        getItems().setColumns("PLU", "name", "duration");
        getItems().addColumn(PatternEntity::getDescription).setHeader("Description");
        getItems().addColumn(PatternEntity::getPriceWithoutVAT).setHeader("Price without VAT");
        getItems().addColumn(FinancialService::calculatePriceWithVat).setHeader("Price with VAT");
        getItems().addComponentColumn((pattern) -> {
            Select<Integer> select = new Select<>();
            select.setItems(DPH.getRates());
            select.setValue(pattern.getVAT());
            select.addValueChangeListener(e -> {
                pattern.setVAT(select.getValue());
                patternService.save(pattern);
                getItems().getDataProvider().refreshItem(pattern);
            });
            return select;
        }).setHeader("VAT");

        getItems().addComponentColumn((patternEntity) -> {
            final Button deleteButton = new Button("Details");
            deleteButton.addClickListener(e -> {
                patternImagesDialog.setPatternEntity(patternEntity);
                patternImagesDialog.open();
            });
            return deleteButton;
        }).setHeader("Images");

        getItems().getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        getItems().asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                patternForm.setEntity(event.getValue());
                patternForm.open("Edit pattern");
            }
        });
        DataProvider<PatternEntity, Void> dataProvider =
                DataProvider.fromCallbacks(
                        query -> {
                            int offset = query.getOffset();
                            int limit = query.getLimit();
                            Set<PatternEntity> items = patternService.filter(getSpecification(), offset, limit);
                            return items.stream();
                        },
                        query -> Math.toIntExact(patternService.countAnyMatching(getSpecification())));
        getItems().setDataProvider(dataProvider);

    }

    protected void configureDefaultGrid() {
        patterns.setColumns("PLU", "name", "duration");
        patterns.setItems(patternService.loadAll());
        patterns.addClassName("items-grid");
        patterns.setPageSize(20);
        patterns.setColumns("PLU", "name", "duration");
        patterns.getColumns().forEach(itemColumn -> itemColumn.setAutoWidth(true));
        patterns.setItems(patternService.loadAll());
        patterns.addComponentColumn((patternEntity) -> {
            final Button addButton = new Button(VaadinIcon.PLUS.create());
            addButton.addClickListener(e -> {
                patternService.save(patternEntity);
                NotificationService.success("Patter was added");
                patterns.setItems(patternService.getFirmDefaultPatterns());
                updateDefaultPatterns();
            });
            return addButton;
        }).setHeader("Actions");
    }

    protected Component getUser() {
        VerticalLayout verticalLayout = new VerticalLayout(getUserToolBar(), getContent());
        HorizontalLayout horizontalLayout = new HorizontalLayout(verticalLayout);
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    protected Component getDefault() {
        VerticalLayout verticalLayout = new VerticalLayout(getDefaultToolBar(), patterns);
        HorizontalLayout horizontalLayout = new HorizontalLayout(verticalLayout);
        horizontalLayout.setSizeFull();
        return horizontalLayout;
    }

    private void updateSidebar() {
        if (sidebarCollapsed) {
            button.setIcon(rightArrowIcon);
            splitLayout.setSplitterPosition(1.5);
        } else {
            button.setIcon(leftArrowIcon);
            splitLayout.setSplitterPosition(100);
            updateDefaultPatterns();
        }
    }

    @Override
    protected HorizontalLayout getToolBar() {
//        final TextField nameFilter = new TextField();
//        final TextField plufilter = new TextField();
//        final NumberField durationFilter = new NumberField();
//        nameFilter.getElement().setAttribute("theme", "test");
//        durationFilter.getElement().setAttribute("theme", "test");
//        plufilter.getElement().setAttribute("theme", "test");
//
//        nameFilter.setPlaceholder("Name");
//        nameFilter.setClearButtonVisible(true);
//        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);
//
//        plufilter.setPlaceholder("PLU kod");
//        plufilter.setClearButtonVisible(true);
//        plufilter.setValueChangeMode(ValueChangeMode.LAZY);
//
//
//        addPatternButton.addClickListener(e -> {
//            getItems().asSingleSelect().clear();
//            patternForm.setEntity(new PatternEntity());
//            patternForm.open("Create pattern");
//        });
//
//        filterButton.addClickListener(e -> {
//            updateList();
//        });
//        HorizontalLayout filterPart = new HorizontalLayout(getOptionsMenuBar());
//        filterPart.addClassName("filterPart");
//
//        HorizontalLayout toolbar = new HorizontalLayout(filterPart);
//        toolbar.addClassName("toolbar");

        return null;
    }

    public HorizontalLayout getUserToolBar() {
        final Button filterButton = new Button("Filter");
        final Button addPatternButton = new Button("Add pattern");
        final TextField nameFilter = new TextField();
        final TextField plufilter = new TextField();
        final NumberField durationFilter = new NumberField();
        final Span span = new Span("Firm patterns");
        span.setWidth("100%");
        span.addClassName("headline");

        nameFilter.getElement().setAttribute("theme", "test");
        durationFilter.getElement().setAttribute("theme", "test");
        plufilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("Name");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        plufilter.setPlaceholder("PLU kod");
        plufilter.setClearButtonVisible(true);
        plufilter.setValueChangeMode(ValueChangeMode.LAZY);


        addPatternButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            patternForm.setEntity(new PatternEntity());
            patternForm.open("Create pattern");
        });

        filterButton.addClickListener(e -> {
            updateFirmPatterns(new PatternSpecification(userService.getUserFirm().getId(), plufilter.getValue(), nameFilter.getValue()));
        });
        HorizontalLayout filterPart = new HorizontalLayout(span, nameFilter, plufilter, filterButton, addPatternButton, getOptionsMenuBar());
        filterPart.addClassName("filterPart");
        return filterPart;
    }

    public HorizontalLayout getDefaultToolBar() {
        final Button filterButton = new Button("Filter");
        final TextField nameFilter = new TextField();
        final TextField plufilter = new TextField();
        final NumberField durationFilter = new NumberField();

        nameFilter.getElement().setAttribute("theme", "test");
        durationFilter.getElement().setAttribute("theme", "test");
        plufilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("Name");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        plufilter.setPlaceholder("PLU kod");
        plufilter.setClearButtonVisible(true);
        plufilter.setValueChangeMode(ValueChangeMode.LAZY);


        filterButton.addClickListener(e -> {
            updateDefaultPatterns();
        });
        Span span = new Span("Default patterns");
        span.setWidth("100%");
        span.addClassName("headline");
        HorizontalLayout filterPart = new HorizontalLayout(span, nameFilter, plufilter, filterButton);
        filterPart.addClassName("filterPart");
        return filterPart;
    }

    @Override
    protected void configureEvents() {
        patternForm.addListener(SaveEvent.class, this::save);
        patternForm.addListener(CloseEvent.class, e -> closeEditor());
        patternForm.addListener(DeleteEvent.class, this::delete);
    }

    @Override
    protected void updateList() {
        getItems().setItems(patternService.filter(new PatternSpecification(userService.getUserFirm().getId())));
    }

    protected void updateFirmPatterns(Specification specification) {
        getItems().setItems(patternService.filter(specification));
    }

    protected void updateDefaultPatterns() {
        patterns.setItems(patternService.getFirmDefaultPatterns());
    }

    @Override
    protected void closeEditor() {
        patternForm.setEntity(null);
        patternForm.close();
    }

    private MenuBar getOptionsMenuBar() {
        MenuBar menuBar = new MenuBar();
        MenuItem options = menuBar.addItem("Other options");
        SubMenu subItems = options.getSubMenu();

        MemoryBuffer memoryBuffer = new MemoryBuffer();


        Upload upload = new Upload(memoryBuffer);
        upload.setDropAllowed(false);
        upload.setAcceptedFileTypes("text/csv", ".csv");
        upload.addFinishedListener(e -> {
            InputStream inputStream = memoryBuffer.getInputStream();
            importService.importPatterns(inputStream);
        });
        MenuItem importItem = subItems.addItem(upload);
        importItem.setCheckable(true);
        return menuBar;
    }

    private void save(SaveEvent event) {
        var id = event.getItem().getId();
        patternService.save(event.getItem());
//        if (id != null)
//            getItems().getDataProvider().refreshAll();
//        else
        updateFirmPatterns(new PatternSpecification(userService.getUserFirm().getId()));
        closeEditor();
        NotificationService.success("Pattern was saved");
    }

    private void delete(DeleteEvent event) {
        patternService.delete(event.getItem());
        NotificationService.error("Pattern was deleted");
        updateFirmPatterns(new PatternSpecification(userService.getUserFirm().getId()));
        closeEditor();
    }

    private PatternSpecification getSpecification() {
        return new PatternSpecification(userService.getUserFirm().getId());
    }

    public PatternForm getPatternForm() {
        return patternForm;
    }
}
