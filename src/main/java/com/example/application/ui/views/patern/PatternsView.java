package com.example.application.ui.views.patern;

import com.example.application.dto.DPH;
import com.example.application.persistence.entity.PatternEntity;
import com.example.application.persistence.repository.ImageRepository;
import com.example.application.service.AuthService;
import com.example.application.service.FinancialService;
import com.example.application.service.ImportService;
import com.example.application.service.PatternService;
import com.example.application.service.UserService;
import com.example.application.ui.views.AbstractServicesView;
import com.example.application.ui.views.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MemoryBuffer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.InputStream;

@PageTitle("Items")
@CssImport(value = "./views/toolbar/text-field.css")
@Route(value = "items", layout = MainView.class)
public class PatternsView extends AbstractServicesView<PatternEntity, PatternEntity> {
    private final TextField           nameFilter          = new TextField();
    private final NumberField         priceFilter         = new NumberField();
    private final NumberField         durationFilter      = new NumberField();
    private final Button              filterButton        = new Button("Filter");
    private final Button              addPatternButton    = new Button("Add pattern");
    private final PatternService      patternService;
    private final UserService         userService;
    private final ImportService       importService;
    private final PatternForm         patternForm         = new PatternForm();
    private final Span                span                = new Span("Patterns");
    private final DialogContainerTabs dialogContainerTabs = new DialogContainerTabs();
    private final PatternImagesDialog patternImagesDialog;

    public PatternsView(PatternService patternService, UserService userService, ImportService importService, ImageRepository imageRepository) {
        super(new Grid<>(PatternEntity.class, false), patternService);
        this.patternService = patternService;
        this.userService = userService;
        this.importService = importService;
        this.patternImagesDialog = new PatternImagesDialog(imageRepository, patternService);
        addClassName("Patterns-view");
        setSizeFull();
        configureGrid();
        add(getToolBar(), getContent(), patternForm);
        configureEvents();
    }

    @Override
    protected void configureForm() {

    }

    @Override
    protected void configureGrid() {
        getItems().addClassName("items-grid");
        getItems().setSizeFull();
        getItems().setColumns("name", "description", "duration");
        getItems().addColumn(PatternEntity::getPriceWithoutVAT).setHeader("Price without VAT");
        getItems().addColumn(FinancialService::calculatePriceWithVat).setHeader("Price with VAT");
        getItems().addComponentColumn((pattern) -> {
            Select<Integer> select = new Select<>();
            select.setItems(DPH.getRates());
            select.setValue(pattern.getVAT());
            select.addValueChangeListener(e -> {
                pattern.setVAT(select.getValue());
                patternService.save(pattern);
                updateList();
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
        updateList();
    }

    @Override
    protected HorizontalLayout getToolBar() {
        span.setClassName("headline");

        nameFilter.getElement().setAttribute("theme", "test");
        durationFilter.getElement().setAttribute("theme", "test");
        priceFilter.getElement().setAttribute("theme", "test");

        nameFilter.setPlaceholder("Name");
        nameFilter.setClearButtonVisible(true);
        nameFilter.setValueChangeMode(ValueChangeMode.LAZY);

        durationFilter.setPlaceholder("Duration");
        durationFilter.setClearButtonVisible(true);
        durationFilter.setValueChangeMode(ValueChangeMode.LAZY);

        priceFilter.setPlaceholder("Price");
        priceFilter.setClearButtonVisible(true);
        priceFilter.setValueChangeMode(ValueChangeMode.LAZY);


        addPatternButton.addClickListener(e -> {
            getItems().asSingleSelect().clear();
            patternForm.setEntity(new PatternEntity());
            patternForm.open("Create pattern");
        });

        filterButton.addClickListener(e -> {
            dialogContainerTabs.openDialog();
            updateList();
        });
        HorizontalLayout filterPart = new HorizontalLayout(nameFilter, durationFilter, priceFilter, filterButton, addPatternButton, getOptionsMenuBar());
        filterPart.addClassName("filterPart");
        HorizontalLayout toolbar = new HorizontalLayout(span, filterPart);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    @Override
    protected void configureEvents() {
        patternForm.addListener(PatternForm.SaveEvent.class, this::save);
        patternForm.addListener(PatternForm.CloseEvent.class, e -> closeEditor());
        patternForm.addListener(PatternForm.DeleteEvent.class, this::delete);
    }

    @Override
    protected void updateList() {
        getItems().setItems(patternService.filter(userService.getUserFirm(), nameFilter.getValue(), priceFilter.getValue(), durationFilter.getValue()));
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

    private void save(PatternForm.SaveEvent event) {
        patternService.save(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Pattern was saved");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    private void delete(PatternForm.DeleteEvent event) {
        patternService.delete(event.getItem());
        updateList();
        closeEditor();
        var notification = Notification.show("Pattern was deleted");
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_CENTER);
    }

    public PatternForm getPatternForm() {
        return patternForm;
    }

    public Button getAddPatternButton() {
        return addPatternButton;
    }

    public Button getFilterButton() {
        return filterButton;
    }

    public TextField getNameFilter() {
        return nameFilter;
    }

    public NumberField getPriceFilter() {
        return priceFilter;
    }

    public NumberField getDurationFilter() {
        return durationFilter;
    }
}
