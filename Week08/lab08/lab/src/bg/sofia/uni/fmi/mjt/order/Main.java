package bg.sofia.uni.fmi.mjt.order;

import bg.sofia.uni.fmi.mjt.order.analyzer.OrderAnalyzerImpl;
import bg.sofia.uni.fmi.mjt.order.loader.OrderLoader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class Main {
    public static void main(String[] args) throws FileNotFoundException, IOException {
        String filePath = "amazon_sales_data 2025.csv";

        Reader reader = new FileReader(filePath);

        var orders = OrderLoader.load(reader);
        System.out.println(orders.size());
        System.out.println(orders);

        var analyzer = new OrderAnalyzerImpl(orders);

        System.out.println(analyzer.suspiciousCustomers());
    }
}