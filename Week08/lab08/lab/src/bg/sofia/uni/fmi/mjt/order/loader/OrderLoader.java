package bg.sofia.uni.fmi.mjt.order.loader;

import bg.sofia.uni.fmi.mjt.order.domain.Order;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Objects;

public class OrderLoader {

    /**
     * Returns a list of orders read from the source Reader.
     *
     * @param reader the Reader with orders
     * @throws IllegalArgumentException if the reader is null
     */
    public static List<Order> load(Reader reader) {
        if (reader == null) {
            throw new IllegalArgumentException("Reader cannot be null.");
        }

        try (var bufferedReader = new BufferedReader(reader)) {
            return bufferedReader.lines().skip(1).map(Order::of).filter(Objects::nonNull).toList();
        } catch (IOException | UncheckedIOException e) {
            throw new RuntimeException("Error while reading", e);
        }
    }
}