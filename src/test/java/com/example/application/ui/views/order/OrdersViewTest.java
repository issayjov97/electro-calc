//package com.example.application.ui.views.order;
//
//import com.example.application.persistence.entity.OrderEntity;
//import com.example.application.service.CustomerService;
//import com.example.application.service.JobOrderService;
//import com.example.application.service.OrderService;
//import com.example.application.service.PatternService;
//import com.example.application.service.UserService;
//import com.vaadin.flow.component.UI;
//import com.vaadin.flow.component.grid.Grid;
//import com.vaadin.flow.data.provider.ListDataProvider;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertFalse;
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.junit.jupiter.api.Assertions.assertNull;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//
//@SpringBootTest
//class OrdersViewTest {
//
//    private final UI           ui = new UI();
//    @Autowired
//    private       OrderService orderService;
//
//    @Autowired
//    private CustomerService customerService;
//
//    @Autowired
//    private PatternService patternService;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private JobOrderService jobOrderService;
//
//    private OrdersView ordersView;
//
//    @BeforeEach
//    void init() {
//        UI.setCurrent(ui);
//        this.ordersView = new OrdersView(orderService, jobOrderService, customerService, null,null);
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void formShownWhenOrderSelected() {
//        Grid<OrderEntity> grid = ordersView.getItems();
//        OrderEntity orderEntity = getFirstOrder(grid);
//
//        OrderForm form = ordersView.getOrderForm();
//
//        assertFalse(ordersView.getOrderForm().getDialog().isOpened());
//        grid.asSingleSelect().setValue(orderEntity);
//        assertTrue(ordersView.getOrderForm().getDialog().isOpened());
//        assertEquals(orderEntity.getMaterialsCost(), form.getMaterialsCost().getValue());
//        assertEquals(orderEntity.getTransportationCost(), form.getTransportationCost().getValue());
//        assertEquals(orderEntity.getWorkHours(), form.getWorkHours().getValue());
//
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void formShownWhenAddOrderClicked() {
//        OrderForm form = ordersView.getOrderForm();
//
//        assertFalse(ordersView.getOrderForm().getDialog().isOpened());
//        ordersView.getAddButton().click();
//
//        assertTrue(ordersView.getOrderForm().getDialog().isOpened());
//        assertNotNull(form.getEntity());
//        assertNull(form.getMaterialsCost().getValue());
//        assertEquals(0, form.getWorkHours().getValue());
//        assertNull(form.getTransportationCost().getValue());
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void formShownWhenPatternDetailsClicked() {
//        OrdersPatternsView ordersPatternsView = new OrdersPatternsView(orderService, patternService, userService);
//
//
//        ordersView.getItems().getColumns();
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void formShownWhenCustomerDetailsClicked() {
//        OrderForm form = ordersView.getOrderForm();
//
//        assertFalse(ordersView.getOrderForm().getDialog().isOpened());
//        ordersView.getAddButton().click();
//
//        assertTrue(ordersView.getOrderForm().getDialog().isOpened());
//        assertNotNull(form.getEntity());
//        assertNull(form.getMaterialsCost().getValue());
//        assertEquals(0, form.getWorkHours().getValue());
//        assertNull(form.getTransportationCost().getValue());
//    }
//
//    @Test
//    @WithMockUser(username = "tester123")
//    public void filteredGridShownWhenFilterClicked() {
////        Grid<UserEntity> grid = ordersView.getItemsGrid();
////
////        ordersView.getUsernameFilter().setValue("tester123");
////
////        assertEquals(3, (int) grid.getDataProvider().fetch(new Query<>()).count());
////        ordersView.getFilterButton().click();
////
////        assertEquals(1, (int) grid.getDataProvider().fetch(new Query<>()).count());
//    }
//
//    private OrderEntity getFirstOrder(Grid<OrderEntity> grid) {
//        return ((ListDataProvider<OrderEntity>) grid.getDataProvider()).getItems().iterator().next();
//    }
//}