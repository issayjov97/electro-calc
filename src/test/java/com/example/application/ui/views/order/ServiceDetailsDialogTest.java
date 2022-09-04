//package com.example.application.ui.views.order;
//
//import com.example.application.persistence.entity.PatternEntity;
//import com.example.application.ui.views.ServiceDetailsDialog;
//import com.vaadin.flow.data.provider.Query;
//import org.junit.jupiter.api.Test;
//
//import java.math.BigDecimal;
//import java.util.Set;
//import java.util.concurrent.atomic.AtomicReference;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//class ServiceDetailsDialogTest {
//    final String     name            = "test_pattern";
//    final String     description     = "Test pattern description";
//    final Double     duration        = 12.0;
//    final BigDecimal priceWithoutVAT = new BigDecimal(12);
//
//    @Test
//    public void gridsPopulated() {
//        ServiceDetailsDialog serviceDetailsDialog = new ServiceDetailsDialog();
//        serviceDetailsDialog.setPatterns(getPatterns());
//        assertEquals(getPatterns().size(), serviceDetailsDialog.getOrderPatternsGrid().getDataProvider().fetch(new Query<>()).count());
//    }
//
//    @Test
//    public void saveEventHasCorrectValues() {
//        ServiceDetailsDialog serviceDetailsDialog = new ServiceDetailsDialog();
//        serviceDetailsDialog.setPatterns(getPatterns());
//
//        serviceDetailsDialog.getOrderPatternsGrid().asMultiSelect().select(getPatterns());
//
//        AtomicReference<Set<PatternEntity>> saveRef = new AtomicReference<>(null);
//        serviceDetailsDialog.addListener(ServiceDetailsDialog.SaveEvent.class, e -> {
//            saveRef.set(e.getPatterns());
//        });
//        serviceDetailsDialog.getSaveButton().click();
//
//        Set<PatternEntity> savedPatterns = saveRef.get();
//
//        assertEquals(savedPatterns.size(), getPatterns().size());
//    }
//
//    private Set<PatternEntity> getPatterns() {
//        PatternEntity pattern = new PatternEntity();
//        pattern.setName(name);
//        pattern.setDescription(description);
//        pattern.setDuration(duration);
//        pattern.setPriceWithoutVAT(priceWithoutVAT);
//        return Set.of(pattern);
//    }
//
//}