package bg.sofia.uni.fmi.mjt.order.loader;

import bg.sofia.uni.fmi.mjt.order.domain.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderLoaderTest {

    String header =
            "Order ID," +
                    "Date," +
                    "Product," +
                    "Category," +
                    "Price," +
                    "Quantity," +
                    "Total Sales," +
                    "Customer Name," +
                    "Customer Location," +
                    "Payment Method," +
                    "Status";

    String line =
            "1," +
                    "03-06-25," +
                    "Microwave," +
                    "Electronics," +
                    "67.41," +
                    "1," +
                    "67.41," +
                    "Buyer," +
                    "Lidl," +
                    "Credit Card," +
                    "Pending"
            ;

    @Test
    void testLoadThrowsIllegalArgumentWhenReaderIsNull() {
        assertThrows(IllegalArgumentException.class,
                () -> OrderLoader.load(null));
    }

    @Test
    void testLoadIgnoresHeader() throws IOException {
        String file = header + System.lineSeparator() + line;
        Reader reader = new StringReader(file);
        List<Order> orderList = OrderLoader.load(reader);

        assertEquals(1, orderList.size());
        assertEquals(orderList.getFirst(), Order.of(line));
    }

    @Test
    void testReturnsEmptyListWhenHeaderIsOnlyLine() throws IOException {
        Reader reader = new StringReader(header);
        List<Order> orderList = OrderLoader.load(reader);

        assertTrue(orderList.isEmpty());
    }

}