package com.example.application.ui.views.offer;

import com.example.application.persistence.entity.OfferEntity;
import com.example.application.service.FinancialService;
import com.example.application.ui.views.AbstractForm;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import org.springframework.core.convert.ConversionService;

@CssImport(value = "./views/menu-bar.css", themeFor = "vaadin-menu-bar")
public class OfferSummaryForm extends AbstractForm<OfferEntity> {
    private final TextField materialCost = new TextField();
    private final TextField transportationCost = new TextField();
    private final TextField workCost = new TextField();
    private final TextField workHours = new TextField();
    private final TextField totalPriceWithoutVAT = new TextField();
    private final TextField totalPriceWithVAT = new TextField();
    private final TextField saleWithVAT = new TextField();
    private final TextField saleWithoutVAT = new TextField();
    private final TextField priceWithVAT = new TextField();
    private final TextField priceWithoutVAT = new TextField();
    private final FinancialService financialService;

    private final ConversionService conversionService;

    public OfferSummaryForm(ConversionService conversionService, FinancialService financialService) {
        super(new BeanValidationBinder<>(OfferEntity.class));
        this.conversionService = conversionService;
        this.financialService = financialService;
        add(createDialogLayout());
    }

    @Override
    protected void setBinder() {
    }

    @Override
    protected VerticalLayout createDialogLayout() {
        materialCost.setReadOnly(true);
        transportationCost.setReadOnly(true);
        workCost.setReadOnly(true);
        workHours.setReadOnly(true);
        totalPriceWithoutVAT.setReadOnly(true);
        totalPriceWithVAT.setReadOnly(true);
        saleWithVAT.setReadOnly(true);
        saleWithoutVAT.setReadOnly(true);
        priceWithVAT.setReadOnly(true);
        priceWithoutVAT.setReadOnly(true);
        priceWithoutVAT.setSuffixComponent(new Span("bez DPH"));
        priceWithVAT.setSuffixComponent(new Span("s DPH"));
        saleWithoutVAT.setSuffixComponent(new Span("bez DPH"));
        saleWithVAT.setSuffixComponent(new Span("s DPH"));
        totalPriceWithoutVAT.setSuffixComponent(new Span("bez DPH"));
        totalPriceWithVAT.setSuffixComponent(new Span("s DPH"));
        totalPriceWithoutVAT.getElement().setAttribute("theme", "without-vat");
        totalPriceWithVAT.getElement().setAttribute("theme", "with-vat");


        var priceWithoutVATLabel = new Label("Celkem bez DPH");
        priceWithoutVATLabel.setWidth("120px");
        var priceWthVATLabel = new Label("Celkem s DPH");
        priceWthVATLabel.setWidth("120px");

        FormLayout formLayout = new FormLayout();
        formLayout.addFormItem(workCost, "Cena práce:");
        formLayout.addFormItem(workHours, "Doba práce:");
        formLayout.addFormItem(materialCost, "Cena materiálů:");
        formLayout.addFormItem(transportationCost, "Cena dopravy:");
        formLayout.addFormItem(priceWithoutVAT, "Cena před slevou:");
        formLayout.addFormItem(priceWithVAT, "");
        formLayout.addFormItem(saleWithoutVAT, "Výše slevy:");
        formLayout.addFormItem(saleWithVAT, "");
        formLayout.addFormItem(totalPriceWithoutVAT, "Celkem:");
        formLayout.addFormItem(totalPriceWithVAT, "");
        return new VerticalLayout(formLayout);
    }

    @Override
    protected void validateAndSave() {
    }

    public void fillOutOfferSummary() {
        workCost.setValue(conversionService.convert(getEntity().getWorkCost(), String.class));
        materialCost.setValue(conversionService.convert(getEntity().getMaterialsCost(), String.class));
        transportationCost.setValue(conversionService.convert(getEntity().getTransportationCost(), String.class));
        workHours.setValue(conversionService.convert(getEntity().getWorkDuration(), String.class));
        priceWithoutVAT.setValue(conversionService.convert(getEntity().getPriceWithoutVAT(), String.class));
        priceWithVAT.setValue(conversionService.convert(getEntity().getPriceWithVAT(), String.class));
        saleWithoutVAT.setValue(conversionService.convert(financialService.offerPriceWithSaleWithoutVAT(getEntity()), String.class));
        saleWithVAT.setValue(conversionService.convert(financialService.offerPriceWithSaleWithVAT(getEntity()), String.class));
        totalPriceWithoutVAT.setValue(conversionService.convert(getEntity().getTotalPriceWithoutVAT(), String.class));
        totalPriceWithVAT.setValue(conversionService.convert(getEntity().getTotalPriceWithVAT(), String.class));
    }
}