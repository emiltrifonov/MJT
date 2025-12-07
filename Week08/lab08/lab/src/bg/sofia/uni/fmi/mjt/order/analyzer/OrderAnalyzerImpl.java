package bg.sofia.uni.fmi.mjt.order.analyzer;

import bg.sofia.uni.fmi.mjt.order.domain.Category;
import bg.sofia.uni.fmi.mjt.order.domain.Order;
import bg.sofia.uni.fmi.mjt.order.domain.PaymentMethod;
import bg.sofia.uni.fmi.mjt.order.domain.Status;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderAnalyzerImpl implements OrderAnalyzer {

    private final List<Order> orders;

    private static final Double SUSPICIOUS_SUM_CAP = 100.00;
    private static final Long MIN_SUSPICIOUS_ORDERS = 3L;

    public OrderAnalyzerImpl(List<Order> orders) {
        this.orders = orders;
    }

    @Override
    public List<Order> allOrders() {
        return List.copyOf(orders);
    }

    @Override
    public List<Order> ordersByCustomer(String customer) {
        return orders.stream()
                .filter(o -> o.customerName().equals(customer))
                .toList();
    }

    @Override
    public Map.Entry<LocalDate, Long> dateWithMostOrders() {
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

        return orders.stream()
                .collect(Collectors.groupingBy(Order::product, Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue()))
                .limit(n)
                .map(Map.Entry::getKey)
                .toList();
    }

    @Override
    public Map<Category, Double> revenueByCategory() {
        return orders.stream()
                .collect(Collectors.groupingBy(Order::category, Collectors.summingDouble(Order::totalSales)));
    }

    @Override
    public Set<String> suspiciousCustomers() {
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
        var map = orders.stream()
                .collect(Collectors.groupingBy(Order::category,
                        Collectors.groupingBy(Order::paymentMethod, Collectors.counting())));

        return map.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> e.getValue().entrySet().stream()
                                .max(Comparator.comparing(
                                                Map.Entry<PaymentMethod, Long>::getValue)
                                                .thenComparing(e1 -> e1.getKey().name())
                                )
                                .orElseThrow()
                                .getKey()
                ));
    }

    @Override
    public String locationWithMostOrders() {
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
