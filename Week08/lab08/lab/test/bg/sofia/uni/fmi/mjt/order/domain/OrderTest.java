package bg.sofia.uni.fmi.mjt.order.domain;

import bg.sofia.uni.fmi.mjt.order.date.DateParser;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderTest {

    @Test
    void testFactoryMethodOfReturnsNullWhenLineIsNull() {
        assertNull(Order.of(null));
    }

    @Test
    void testFactoryMethodOfReturnsNullWhenFieldCountIsIncorrect() {
        assertNull(Order.of(""));
    }

    @Test
    void testFactoryMethodOf() {
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

        Order order = Order.of(line);

        assertEquals("1", order.id());
        assertEquals(DateParser.parse("03-06-25"), order.date());
        assertEquals("Microwave", order.product());
        assertEquals(Category.ELECTRONICS, order.category());
        assertEquals(67.41, order.price(), 0.001);
        assertEquals(1, order.quantity());
        assertEquals(67.41, order.totalSales(), 0.001);
        assertEquals("Buyer", order.customerName());
        assertEquals("Lidl", order.customerLocation());
        assertEquals(PaymentMethod.CREDIT_CARD, order.paymentMethod());
        assertEquals(Status.PENDING, order.status());

    }

}