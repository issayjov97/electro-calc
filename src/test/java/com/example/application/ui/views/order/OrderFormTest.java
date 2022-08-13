package com.example.application.ui.views.order;

import com.example.application.persistence.entity.OrderEntity;
import com.example.application.service.CustomerService;
import com.example.application.service.JobOrderService;
import com.example.application.service.PdfGenerateServiceImpl;
import com.example.application.ui.views.order.events.DeleteEvent;
import com.example.application.ui.views.order.events.SaveEvent;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class OrderFormTest {

    final BigDecimal materialCost       = new BigDecimal(140);
    final BigDecimal transportationCost = new BigDecimal(193);
    final double     workHours          = 12.0;

    @Autowired
    private PdfGenerateServiceImpl pdfGenerateService;

    @Autowired
    private JobOrderService jobOrderService;

    @Autowired
    private CustomerService customerService;

    @Test
    public void formFieldsPopulated() {
        OrderForm orderForm = new OrderForm(pdfGenerateService, jobOrderService, customerService);
        OrderEntity orderEntity = getOrder();

        orderForm.setEntity(orderEntity);

        assertEquals(materialCost, orderForm.getMaterialsCost().getValue());
        assertEquals(transportationCost, orderForm.getTransportationCost().getValue());
        assertEquals(workHours, orderForm.getWorkHours().getValue());
    }

    @Test
    public void saveEventHasCorrectValues() {
        OrderForm orderForm = new OrderForm(pdfGenerateService, jobOrderService, customerService);
        OrderEntity orderEntity = new OrderEntity();
        orderForm.setEntity(orderEntity);
        orderForm.getMaterialsCost().setValue(materialCost);
        orderForm.getTransportationCost().setValue(transportationCost);
        orderForm.getWorkHours().setValue(workHours);

        AtomicReference<OrderEntity> savedContactRef = new AtomicReference<>(null);
        orderForm.addListener(SaveEvent.class, e -> {
            savedContactRef.set(e.getItem());
        });

        orderForm.getSaveButton().click();

        OrderEntity savedOrderEntity = savedContactRef.get();

        assertEquals(materialCost, savedOrderEntity.getMaterialsCost());
        assertEquals(transportationCost, savedOrderEntity.getTransportationCost());
        assertEquals(workHours, savedOrderEntity.getWorkHours());
    }

    @Test
    public void deleteEventHasCorrectValues() {
        OrderForm orderForm = new OrderForm(pdfGenerateService, jobOrderService, customerService);
        OrderEntity orderEntity = getOrder();

        AtomicReference<OrderEntity> deleteRef = new AtomicReference<>(null);
        orderForm.addListener(DeleteEvent.class, e -> {
            deleteRef.set(e.getItem());
        });

        orderForm.setEntity(orderEntity);
        orderForm.getDeleteButton().click();

        OrderEntity deletedOrderEntity = deleteRef.get();

        assertEquals(materialCost, deletedOrderEntity.getMaterialsCost());
        assertEquals(transportationCost, deletedOrderEntity.getTransportationCost());
        assertEquals(workHours, deletedOrderEntity.getWorkHours());
    }

    private OrderEntity getOrder() {
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setMaterialsCost(materialCost);
        orderEntity.setTransportationCost(transportationCost);
        orderEntity.setWorkHours(workHours);
        return orderEntity;
    }

}