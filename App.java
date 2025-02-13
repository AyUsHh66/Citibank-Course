package stock_price_app;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;
import java.util.LinkedList;
import java.util.Queue;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class App {
    private static final String API_KEY = "NXQFGSS2G9DLMSDK"; // Replace with your Alpha Vantage API key
    private static final String SYMBOL = "DJI"; // Dow Jones Industrial Average symbol

    public static void main(String[] args) {
        Queue<String> stockQueue = new LinkedList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (true) {
            try {
                String price = getStockPrice();
                LocalDateTime now = LocalDateTime.now();
                String entry = now.format(formatter) + " - $" + price;
                
                stockQueue.offer(entry);
                System.out.println("Added to queue: " + entry);

                // Keep only the last 10 entries
                if (stockQueue.size() > 10) {
                    stockQueue.poll();
                }

                // Print current queue
                System.out.println("Current queue:");
                for (String item : stockQueue) {
                    System.out.println(item);
                }

                Thread.sleep(5000);  // Wait for 5 seconds
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String getStockPrice() throws Exception {
        String url = "https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=" + SYMBOL + "&apikey=" + API_KEY;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONObject json = new JSONObject(response.body());
        JSONObject globalQuote = json.getJSONObject("Global Quote");
        return globalQuote.getString("05. price");
    }
}