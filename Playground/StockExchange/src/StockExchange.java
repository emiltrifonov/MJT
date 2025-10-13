public class StockExchange {
    public static int maxProfit(int[] prices) {
        int[] profits = new int[prices.length];

        for (int i = prices.length - 1; i >= 1 ; i--) {
            int additionalProfit = prices[i] - prices[i-1];
            profits[i-1] = Math.max(profits[i], profits[i] + additionalProfit);
        }

        return profits[0];
    }
}
