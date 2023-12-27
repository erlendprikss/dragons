package org.dragon;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.dragon.model.*;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.InputStream;
import java.util.Properties;
public class DragonGameAPIService {

    private static final String API_BASE_URL;

    static {
        Properties properties = new Properties();
        try (InputStream input = DragonGameAPIService.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(input);
            API_BASE_URL = properties.getProperty("dragon.api.url") + "/api/v2/";
        } catch (Exception e) {
            throw new RuntimeException("Error loading application.properties", e);
        }
    }

    private final HttpClient httpClient;

    public DragonGameAPIService() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public Game startGame() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + "game/start"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return parseJsonResponse(response.body(), Game.class);
    }

    public Message[] getMessages(String gameId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + gameId + "/messages"))
                .header("Content-Type", "application/json")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return parseJsonResponse(response.body(), Message[].class);
    }

    public SolverResponse solveMessage(String gameId, String adId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + gameId + "/solve/" + adId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return parseJsonResponse(response.body(), SolverResponse.class);
    }

    public PurchaseItemResponse buyItem(String gameId, ShopItem.Id itemId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_BASE_URL + gameId + "/shop/buy/" + itemId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return parseJsonResponse(response.body(), PurchaseItemResponse.class);
    }

    private <T> T parseJsonResponse(String jsonResponse, Class<T> responseType) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(jsonResponse, responseType);
    }
}