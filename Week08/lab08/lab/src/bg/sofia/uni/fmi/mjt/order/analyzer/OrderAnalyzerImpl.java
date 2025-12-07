package bg.sofia.uni.fmi.mjt.order.analyzer;

import bg.sofia.uni.fmi.mjt.order.domain.Category;
import bg.sofia.uni.fmi.mjt.order.domain.Order;
import bg.sofia.uni.fmi.mjt.order.domain.PaymentMethod;
import bg.sofia.uni.fmi.mjt.order.domain.Status;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderAnalyzerImpl implements OrderAnalyzer {

    private final List<Order> orders;

    private static final Double SUSPICIOUS_SUM_CAP = 100.00;
    private static final Long MIN_SUSPICIOUS_ORDERS = 3L;

    public OrderAnalyzerImpl(List<Order> orders) {
        if (orders == null) {
            this.orders = new ArrayList<>();
        } else {
            this.orders = orders.stream()
                    .filter(Objects::nonNull)
                    .toList();
        }
    }

    @Override
    public List<Order> allOrders() {
        return List.copyOf(orders);
    }

    @Override
    public List<Order> ordersByCustomer(String customer) {
        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .filter(o -> o.customerName().equals(customer))
                .toList();
    }

    @Override
    public Map.Entry<LocalDate, Long> dateWithMostOrders() {
        if (orders.isEmpty()) {
            return null;
        }

        var map = orders.stream()
                .collect(Collectors.groupingBy(Order::date, Collectors.counting()));

        Long maxCount = map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getValue();

        return map.entrySet().stream()
                .filter(e -> e.getValue().equals(maxCount))
                .min(Map.Entry.comparingByKey())
                .orElse(null);
    }

    @Override
    public List<String> topNMostOrderedProducts(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("N cannot be negative.");
        }

        if (orders.isEmpty()) {
            return Collections.emptyList();
        }

        return orders.stream()
                .collect(Collectors.groupingBy(Order::product, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> {
                    if (Long.compare(e1.getValue(), e2.getValue()) == 0) {
                        return e1.getKey().compareTo(e2.getKey());
                    } else {
                        return Long.compare(e2.getValue(), e1.getValue());
                    }
                })
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public Map<Category, Double> revenueByCategory() {
        if (orders.isEmpty()) {
            return Collections.emptyMap();
        }

        return orders.stream()
                .collect(Collectors.groupingBy(Order::category, Collectors.summingDouble(Order::totalSales)));
    }

    @Override
    public Set<String> suspiciousCustomers() {
        if (orders.isEmpty()) {
            return Collections.emptySet();
        }

        return orders.stream()
                .filter(o -> o.status() == Status.CANCELLED &&
                        Double.compare(o.totalSales(), SUSPICIOUS_SUM_CAP) < 0)
                .collect(Collectors.groupingBy(Order::customerName, Collectors.counting()))
                .entrySet().stream()
                .filter(e -> e.getValue() > MIN_SUSPICIOUS_ORDERS)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());
    }

    @Override
    public Map<Category, PaymentMethod> mostUsedPaymentMethodForCategory() {
        if (orders.isEmpty()) {
            return Collections.emptyMap();
        }

        var map = orders.stream()
                .collect(Collectors.groupingBy(Order::category,
                        Collectors.groupingBy(Order::paymentMethod, Collectors.counting())));

        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().entrySet().stream()
                                .max(Comparator.comparing(
                                                Map.Entry<PaymentMethod, Long>::getValue)
                                                .thenComparing(Map.Entry::getKey)
                                )
                                .orElseThrow()
                                .getKey()
                ));
    }

    @Override
    public String locationWithMostOrders() {
        if (orders.isEmpty()) {
            return null;
        }

        var map = orders.stream()
                .collect(Collectors.groupingBy(Order::customerLocation, Collectors.counting()));

        Long maxCount = map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElseThrow()
                .getValue();

        return map.entrySet().stream()
                .filter(e -> e.getValue().equals(maxCount))
                .min(Map.Entry.comparingByKey())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    @Override
    public Map<Category, Map<Status, Long>> groupByCategoryAndStatus() {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::category,
                        Collectors.groupingBy(Order::status, Collectors.counting())));
    }

}
