package bg.sofia.uni.fmi.mjt.order.analyzer;

import bg.sofia.uni.fmi.mjt.order.date.DateParser;
import bg.sofia.uni.fmi.mjt.order.domain.Category;
import bg.sofia.uni.fmi.mjt.order.domain.Order;
import bg.sofia.uni.fmi.mjt.order.domain.PaymentMethod;
import bg.sofia.uni.fmi.mjt.order.domain.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderAnalyzerImplTest {

    @Test
    void testConstructorFiltersNullOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(null);
        orders.add(null);

        assertTrue(new OrderAnalyzerImpl(orders).allOrders().isEmpty());
    }

    @Test
    void testAllOrdersReturnsUnmodifiableList() {
        assertThrows(UnsupportedOperationException.class,
                () -> new OrderAnalyzerImpl(new ArrayList<>()).allOrders().add(null));
    }

    @Test
    void testAllOrdersReturnsEmptyListWhenConstructedWithNull() {
        assertTrue(new OrderAnalyzerImpl(null).allOrders().isEmpty());
    }

    @Test
    void testOrdersByCustomerReturnsEmptyListWhenOrdersAreEmpty() {
        assertTrue(new OrderAnalyzerImpl(Collections.emptyList()).ordersByCustomer("c").isEmpty());
    }

    @Test
    void testOrdersByCustomerWhenCustomerDoesNotExist() {
        String customer = "nonexistent";
        Order order = mock();
        when(order.customerName()).thenReturn("existing");
        List<Order> orders = new ArrayList<>();
        orders.add(order);
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertTrue(orderAnalyzer.ordersByCustomer(customer).isEmpty());
    }

    @Test
    void testOrdersByCustomerWhenCustomerExists() {
        String customer = "existing";
        Order order1 = mock();
        when(order1.customerName()).thenReturn("existing");
        Order order2 = mock();
        when(order2.customerName()).thenReturn("other");
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        OrderAnalyzerImpl orderAnalyzer = new OrderAnalyzerImpl(orders);

        assertEquals(1, orderAnalyzer.ordersByCustomer(customer).size());
    }

    @Test
    void testDateWithMostOrdersReturnsNullWhenOrdersAreEmpty() {
        assertNull(new OrderAnalyzerImpl(Collections.emptyList()).dateWithMostOrders());
    }

    @Test
    void testDateWithMostOrdersReturnsCorrectDateWithOneCandidate() {
        LocalDate date = DateParser.parse("03-06-25");
        Order order = mock();
        when(order.date()).thenReturn(date);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        assertEquals(Map.entry(date, 1L), new OrderAnalyzerImpl(orders).dateWithMostOrders());
    }

    @Test
    void testDateWithMostOrdersReturnsCorrectDateWithMultipleCandidates() {
        LocalDate date1 = DateParser.parse("03-06-25");
        Order order1 = mock();
        when(order1.date()).thenReturn(date1);
        Order order2 = mock();
        when(order2.date()).thenReturn(date1);

        LocalDate date2 = DateParser.parse("03-07-25");
        Order order3 = mock();
        when(order3.date()).thenReturn(date2);

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);

        assertEquals(Map.entry(date1, 2L), new OrderAnalyzerImpl(orders).dateWithMostOrders());
    }

    @Test
    void testDateWithMostOrdersReturnsCorrectDateWhenTied() {
        LocalDate date1 = DateParser.parse("03-06-25");
        Order order1 = mock();
        when(order1.date()).thenReturn(date1);

        LocalDate date2 = DateParser.parse("10-10-25");
        Order order2 = mock();
        when(order2.date()).thenReturn(date2);

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        assertEquals(Map.entry(date1, 1L), new OrderAnalyzerImpl(orders).dateWithMostOrders());
    }

    @Test
    void testTopNMostOrderedProductsThrowsIllegalArgumentWhenNIsNegative() {
        assertThrows(IllegalArgumentException.class,
                () -> new OrderAnalyzerImpl(null).topNMostOrderedProducts(-1));
    }

    @Test
    void testTopNMostOrderedProductsReturnsEmptyListWhenNIsZero() {
        assertTrue(new OrderAnalyzerImpl(null).topNMostOrderedProducts(0).isEmpty());
    }

    @Test
    void testTopNMostOrderedProductsReturnsEmptyListWhenOrdersAreEmpty() {
        assertTrue(new OrderAnalyzerImpl(null).topNMostOrderedProducts(1).isEmpty());
    }

    @Test
    void testTopNMostOrderedProductsReturnsCorrectWithOneCandidate() {
        String product = "product";
        Order order = mock();
        when(order.product()).thenReturn(product);
        List<Order> orders = new ArrayList<>();
        orders.add(order);

        var expected = List.of(product);

        assertEquals(Collections.emptyList(), new OrderAnalyzerImpl(orders).topNMostOrderedProducts(0));
        assertEquals(expected, new OrderAnalyzerImpl(orders).topNMostOrderedProducts(1));
        assertEquals(expected, new OrderAnalyzerImpl(orders).topNMostOrderedProducts(2));
        assertEquals(expected, new OrderAnalyzerImpl(orders).topNMostOrderedProducts(5));
    }

    @Test
    void testTopNMostOrderedProductsReturnsCorrectWithManyCandidates() {
        String productA = "productA";
        String productB = "productB";

        Order order1 = mock();
        when(order1.product()).thenReturn(productA);

        Order order2 = mock();
        when(order2.product()).thenReturn(productB);

        Order order3 = mock();
        when(order3.product()).thenReturn(productB);

        Order order4 = mock();
        when(order4.product()).thenReturn(productB);

        Order order5 = mock();
        when(order5.product()).thenReturn(productA);

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);

        var expectedAtN1 = List.of(productB);
        var expectedAtN2 = List.of(productB, productA);

        var analyzer = new OrderAnalyzerImpl(orders);

        assertEquals(Collections.emptyList(), new OrderAnalyzerImpl(orders).topNMostOrderedProducts(0));
        assertEquals(expectedAtN1, analyzer.topNMostOrderedProducts(1));
        assertEquals(expectedAtN2, analyzer.topNMostOrderedProducts(2));
        assertEquals(expectedAtN2, analyzer.topNMostOrderedProducts(5));
    }

    @Test
    void testTopNMostOrderedProductsReturnsCorrectWithManyCandidatesAndTie() {
        String productA = "productA";
        String productB = "productB";
        String productC = "productC";

        Order order1 = mock();
        when(order1.product()).thenReturn(productA);

        Order order2 = mock();
        when(order2.product()).thenReturn(productB);

        Order order3 = mock();
        when(order3.product()).thenReturn(productC);

        Order order4 = mock();
        when(order4.product()).thenReturn(productC);

        Order order5 = mock();
        when(order5.product()).thenReturn(productC);

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);
        orders.add(order5);

        var expectedAtN1 = List.of(productC);
        var expectedAtN2 = List.of(productC, productA);
        var expectedAtN5 = List.of(productC, productA, productB);

        var analyzer = new OrderAnalyzerImpl(orders);

        assertEquals(Collections.emptyList(), new OrderAnalyzerImpl(orders).topNMostOrderedProducts(0));
        assertEquals(expectedAtN1, analyzer.topNMostOrderedProducts(1));
        assertEquals(expectedAtN2, analyzer.topNMostOrderedProducts(2));
        assertEquals(expectedAtN5, analyzer.topNMostOrderedProducts(5));
    }

    @Test
    void testRevenueByCategoryReturnsEmptyMapWhenOrdersAreEmpty() {
        assertTrue(new OrderAnalyzerImpl(null).revenueByCategory().isEmpty());
    }

    @Test
    void testRevenueByCategoryReturnsCorrect() {
        Category c1 = Category.BOOKS;
        Category c2 = Category.ELECTRONICS;

        Order o1 = mock();
        when(o1.category()).thenReturn(c1);
        when(o1.totalSales()).thenReturn(1.00);
        Order o2 = mock();
        when(o2.category()).thenReturn(c1);
        when(o2.totalSales()).thenReturn(2.00);

        Order o3 = mock();
        when(o3.category()).thenReturn(c2);
        when(o3.totalSales()).thenReturn(3.00);
        Order o4 = mock();
        when(o4.category()).thenReturn(c2);
        when(o4.totalSales()).thenReturn(4.00);

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);
        orders.add(o4);

        var map = new OrderAnalyzerImpl(orders).revenueByCategory();

        assertEquals(2, map.size());
        assertEquals(3.00, map.get(c1), 0.001);
        assertEquals(7.00, map.get(c2), 0.001);
    }

    @Test
    void testSuspiciousCustomersReturnsEmptySetWhenOrdersAreEmpty() {
        assertTrue(new OrderAnalyzerImpl(null).suspiciousCustomers().isEmpty());
    }

    @Test
    void testSuspiciousCustomersReturnsEmptySetWhenThereAreNone() {
        Order order = mock();
        when(order.status()).thenReturn(Status.COMPLETED);
        when(order.totalSales()).thenReturn(110.00);

        assertTrue(new OrderAnalyzerImpl(List.of(order)).suspiciousCustomers().isEmpty());
    }

    @Test
    void testSuspiciousCustomersDetectsCorrectly() {
        Order order1 = mock();
        when(order1.status()).thenReturn(Status.CANCELLED);
        when(order1.totalSales()).thenReturn(1.00);
        when(order1.customerName()).thenReturn("name");

        Order order2 = mock();
        when(order2.status()).thenReturn(Status.CANCELLED);
        when(order2.totalSales()).thenReturn(2.00);
        when(order2.customerName()).thenReturn("name");

        Order order3 = mock();
        when(order3.status()).thenReturn(Status.CANCELLED);
        when(order3.totalSales()).thenReturn(98.00);
        when(order3.customerName()).thenReturn("name");

        Order order4 = mock();
        when(order4.status()).thenReturn(Status.CANCELLED);
        when(order4.totalSales()).thenReturn(99.00);
        when(order4.customerName()).thenReturn("name");

        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        orders.add(order4);

        assertEquals(1, new OrderAnalyzerImpl(orders).suspiciousCustomers().size());
    }

    @Test
    void testMostUsedPaymentMethodForCategoryReturnsEmptyMapWhenOrdersAreNull() {
        assertTrue(new OrderAnalyzerImpl(null).mostUsedPaymentMethodForCategory().isEmpty());
    }

    @Test
    void testMostUsedPaymentMethodForCategoryWithoutTie() {
        Category c = Category.BOOKS;
        PaymentMethod p1 = PaymentMethod.PAYPAL;
        PaymentMethod p2 = PaymentMethod.GIFT_CARD;

        Order o1 = mock();
        when(o1.category()).thenReturn(c);
        when(o1.paymentMethod()).thenReturn(p1);

        Order o2 = mock();
        when(o2.category()).thenReturn(c);
        when(o2.paymentMethod()).thenReturn(p1);

        Order o3 = mock();
        when(o3.category()).thenReturn(c);
        when(o3.paymentMethod()).thenReturn(p2);

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);

        var analyzer = new OrderAnalyzerImpl(orders);

        assertEquals(1, analyzer.mostUsedPaymentMethodForCategory().size());
        assertEquals(p1, analyzer.mostUsedPaymentMethodForCategory().get(c));
    }

    @Test
    void testMostUsedPaymentMethodForCategoryWithTie() {
        Category c = Category.BOOKS;
        PaymentMethod p1 = PaymentMethod.PAYPAL;
        PaymentMethod p2 = PaymentMethod.GIFT_CARD;

        Order o1 = mock();
        when(o1.category()).thenReturn(c);
        when(o1.paymentMethod()).thenReturn(p1);

        Order o2 = mock();
        when(o2.category()).thenReturn(c);
        when(o2.paymentMethod()).thenReturn(p1);

        Order o3 = mock();
        when(o3.category()).thenReturn(c);
        when(o3.paymentMethod()).thenReturn(p2);

        Order o4 = mock();
        when(o4.category()).thenReturn(c);
        when(o4.paymentMethod()).thenReturn(p2);

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);
        orders.add(o4);

        var analyzer = new OrderAnalyzerImpl(orders);

        assertEquals(p2, analyzer.mostUsedPaymentMethodForCategory().get(c));
    }

    @Test
    void testLocationWithMostOrdersReturnsNullWhenOrdersAreNull() {
        assertNull(new OrderAnalyzerImpl(null).locationWithMostOrders());
    }

    @Test
    void testLocationWithMostOrdersReturnsCorrectWithoutTie() {
        String lA = "lA";
        String lB = "lB";

        Order o1 = mock();
        when(o1.customerLocation()).thenReturn(lA);
        Order o2 = mock();
        when(o2.customerLocation()).thenReturn(lB);
        Order o3 = mock();
        when(o3.customerLocation()).thenReturn(lB);

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);

        var analyzer = new OrderAnalyzerImpl(orders);

        assertEquals(lB, analyzer.locationWithMostOrders());
    }

    @Test
    void testLocationWithMostOrdersReturnsCorrectWithTie() {
        String lA = "lA";
        String lB = "lB";

        Order o1 = mock();
        when(o1.customerLocation()).thenReturn(lA);
        Order o2 = mock();
        when(o2.customerLocation()).thenReturn(lB);

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);

        var analyzer = new OrderAnalyzerImpl(orders);

        assertEquals(lA, analyzer.locationWithMostOrders());
    }

    @Test
    void testGroupByCategoryAndStatusReturnsEmptyMapWhenOrdersAreEmpty() {
        assertTrue(new OrderAnalyzerImpl(null).groupByCategoryAndStatus().isEmpty());
    }

    @Test
    void testGroupByCategoryAndStatusReturnsCorrect() {
        Category c1 = Category.BOOKS;
        Category c2 = Category.CLOTHING;
        Status s1 = Status.PENDING;
        Status s2 = Status.COMPLETED;

        Order o1 = mock();
        when(o1.category()).thenReturn(c1);
        when(o1.status()).thenReturn(s1);

        Order o2 = mock();
        when(o2.category()).thenReturn(c2);
        when(o2.status()).thenReturn(s1);

        Order o3 = mock();
        when(o3.category()).thenReturn(c2);
        when(o3.status()).thenReturn(s2);

        List<Order> orders = new ArrayList<>();
        orders.add(o1);
        orders.add(o2);
        orders.add(o3);

        var analyzer = new OrderAnalyzerImpl(orders);

        assertEquals(2, analyzer.groupByCategoryAndStatus().size());
        assertEquals(1, analyzer.groupByCategoryAndStatus().get(c1).size());
        assertEquals(2, analyzer.groupByCategoryAndStatus().get(c2).size());
        assertEquals(1, analyzer.groupByCategoryAndStatus().get(c1).get(s1));
        assertEquals(1, analyzer.groupByCategoryAndStatus().get(c2).get(s1));
        assertEquals(1, analyzer.groupByCategoryAndStatus().get(c2).get(s2));

    }

}