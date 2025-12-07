package bg.sofia.uni.fmi.mjt.order.domain;

import bg.sofia.uni.fmi.mjt.order.date.DateParser;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public record Order(String id, LocalDate date, String product, Category category,
                          double price, int quantity, double totalSales, String customerName,
                          String customerLocation, PaymentMethod paymentMethod, Status status) {

    static final String DELIMITER = ",";
    static final int FIELDS_COUNT = 11;
    private static final int ID_INDEX = 0;
    private static final int DATE_INDEX = 1;
    private static final int PRODUCT_INDEX = 2;
    private static final int CATEGORY_INDEX = 3;
    private static final int PRICE_INDEX = 4;
    private static final int QUANTITY_INDEX = 5;
    private static final int TOTAL_SALES_INDEX = 6;
    private static final int CUSTOMER_NAME_INDEX = 7;
    private static final int CUSTOMER_LOCATION_INDEX = 8;
    private static final int PAYMENT_METHOD_INDEX = 9;
    private static final int STATUS_INDEX = 10;

    public static Order of(String line) {
        if (line == null) {
            return null;
        }

        String[] tokens = line.split(DELIMITER);

        if (Arrays.stream(tokens).anyMatch(String::isBlank)) {
            return null;
        }

        if (tokens.length != FIELDS_COUNT) {
            return null;
        }

        return new Order(tokens[ID_INDEX],
                DateParser.parse(tokens[DATE_INDEX]),
                tokens[PRODUCT_INDEX],
                getCategory(tokens[CATEGORY_INDEX]),
                Double.parseDouble(tokens[PRICE_INDEX]),
                Integer.parseInt(tokens[QUANTITY_INDEX]),
                Double.parseDouble(tokens[TOTAL_SALES_INDEX]),
                tokens[CUSTOMER_NAME_INDEX],
                tokens[CUSTOMER_LOCATION_INDEX],
                getPaymentMethod(tokens[PAYMENT_METHOD_INDEX]),
                getStatus(tokens[STATUS_INDEX]));
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Order that)) return false;
        return Objects.equals(id(), that.id());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id());
    }

    private static Category getCategory(String string) {
        return Category.valueOf(toEnumFormat(string));
    }

    private static PaymentMethod getPaymentMethod(String string) {
        return PaymentMethod.valueOf(toEnumFormat(string));
    }

    private static Status getStatus(String string) {
        return Status.valueOf(toEnumFormat(string));
    }

    private static String toEnumFormat(String string) {
        return string.toUpperCase().replace(' ', '_');
    }

}
