package com.example;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class GetPoints {      //Handeling API Response to ID
    public static void main(String id) throws IOException, InterruptedException {

        String APi = "https://receipts/" + id + "/points";    // Enter API URL for testing

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(APi)).header("Accept", "application/json").GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        responseReader(response.body());
    }

    private static void responseReader(String response) throws IOException, JsonProcessingException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(response);

        String receiptUUID = jsonNode.get("id").asText();
        int points = jsonNode.get("points").asInt();

        System.out.println("points: " + points);
    }
}