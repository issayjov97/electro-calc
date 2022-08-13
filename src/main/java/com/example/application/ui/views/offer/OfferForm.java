package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.CustomerEntity;
import com.example.application.persistence.entity.JobOrderEntity;
import com.example.application.persistence.entity.OfferEntity;
import com.example.application.service.CustomerService;
import com.example.application.service.JobOrderService;
import com.example.application.service.PdfGenerateServiceImpl;
import com.example.application.ui.events.CloseEvent;
import com.example.application.ui.views.AbstractForm;
import com.example.application.ui.views.offer.events.DeleteEvent;
import com.example.application.ui.views.offer.events.SaveEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.server.InputStreamFactory;
import com.vaadin.flow.server.StreamResource;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@CssImport(value = "./views/menu-bar.css", themeFor = "vaadin-menu-bar")
public class OfferForm extends AbstractForm<OfferEntity> {
    private final BigDecimalField        materialsCost      = new BigDecimalField("Materials cost");
    private final BigDecimalField        transportationCost = new BigDecimalField("Transportation cost");
    private final NumberField            workHours          = new NumberField("Work hours");
    private final Select<JobOrderEntity> jobOrdersSelect    = new Select<>();
    private final Select<CustomerEntity> customersSelect    = new Select<>();
    private final JobOrderService        jobOrderService;
    private final CustomerService        customerService;

    private final PdfGenerateServiceImpl pdfGenerateService;

    public OfferForm(JobOrderService jobOrderService, CustomerService customerService, PdfGenerateServiceImpl pdfGenerateService) {
        super(new BeanValidationBinder<>(OfferEntity.class));
        this.jobOrderService = jobOrderService;
        this.customerService = customerService;
        this.pdfGenerateService = pdfGenerateService;
        setBinder();
        materialsCost.setWidthFull();
        transportationCost.setWidthFull();
        workHours.setWidthFull();
        configureSelect();
        dialog.add(createDialogLayout());
    }

    @Override
    protected void setBinder() {
        binder.forField(materialsCost).asRequired("Materials cost is required")
                .bind(OfferEntity::getMaterialsCost, OfferEntity::setMaterialsCost);
        binder.forField(transportationCost).asRequired("Transportation cost is required")
                .bind(OfferEntity::getTransportationCost, OfferEntity::setTransportationCost);
        binder.forField(workHours).asRequired("Work hours is reuired")
                .bind(OfferEntity::getWorkHours, OfferEntity::setWorkHours);
        binder.forField(jobOrdersSelect)
                .bind(OfferEntity::getJobOrderEntity, OfferEntity::setJobOrderEntity);
        binder.forField(customersSelect)
                .bind(OfferEntity::getCustomerEntity, OfferEntity::setCustomerEntity);
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        VerticalLayout fieldLayout = new VerticalLayout(materialsCost, transportationCost, workHours, jobOrdersSelect, customersSelect, getOptionsMenuBar());
        fieldLayout.setSpacing(false);
        fieldLayout.setPadding(false);
        fieldLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        HorizontalLayout buttonLayout = createButtonsLayout();
        buttonLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        VerticalLayout dialogLayout = new VerticalLayout(headline, fieldLayout, buttonLayout);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        return dialogLayout;
    }

    private MenuBar getOptionsMenuBar() {
        MenuBar menuBar = new MenuBar();
        menuBar.getElement().setAttribute("theme", "menu-button");
        menuBar.addItem("Other options");
        var downIcon = new Icon(VaadinIcon.CHEVRON_DOWN);
        downIcon.getStyle().set("width", "16px");
        downIcon.getStyle().set("height", "16px");
        MenuItem item = menuBar.addItem(downIcon);
        SubMenu subItems = item.getSubMenu();
        Anchor download = new Anchor(new StreamResource("offer.pdf", (InputStreamFactory) () -> {
            Map<String, Object> data = new HashMap<>();
            data.put("title", "Offer");
            data.put("customer", getEntity().getCustomerEntity());
            data.put("service", getEntity());
            data.put("patterns", getEntity().getPatterns());
            data.put("timestamp", LocalDateTime.now());
            return pdfGenerateService.exportReceiptPdf("service", data);
        }), "");

        download.add(new Button("Download", new Icon(VaadinIcon.DOWNLOAD_ALT)));
        download.getElement().setAttribute("download", true);
        subItems.addItem(download);
        return menuBar;
    }

    @Override
    protected HorizontalLayout createButtonsLayout() {
        saveButton.addClickListener(event -> validateAndSave());
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, getEntity())));
        cancelButton.addClickListener(event -> fireEvent(new CloseEvent(this, false)));
        return new HorizontalLayout(saveButton, deleteButton, cancelButton);
    }

    private void configureSelect() {
        jobOrdersSelect.setLabel("Job orders");
        jobOrdersSelect.addClassName("label");
        jobOrdersSelect.setItemLabelGenerator(JobOrderEntity::getJobName);
        jobOrdersSelect.setItems(jobOrderService.getFirmJobOrders());

        customersSelect.setLabel("Customers");
        customersSelect.addClassName("label");
        customersSelect.setItemLabelGenerator(CustomerEntity::getName);
        customersSelect.setItems(customerService.getFirmCustomers());
    }

    @Override
    protected void validateAndSave() {
        try {
            binder.writeBean(getEntity());
            fireEvent(new SaveEvent(this, getEntity()));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    public BigDecimalField getMaterialsCost() {
        return materialsCost;
    }

    public BigDecimalField getTransportationCost() {
        return transportationCost;
    }

    public NumberField getWorkHours() {
        return workHours;
    }
}
