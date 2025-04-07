package com.gamesaver.service;

import com.gamesaver.model.GameDeal;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CheapSharkService {
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final String API_URL = "https://www.cheapshark.com/api/1.0/deals";
    private static final OkHttpClient client = new OkHttpClient();

    public List<GameDeal> searchDeals(String gameTitle) throws IOException {
        HttpUrl.Builder urlBuilder = HttpUrl.parse(API_URL).newBuilder();
        urlBuilder.addQueryParameter("title", gameTitle);
        urlBuilder.addQueryParameter("pageSize", "20");  // np. max 20 wyników

        Request request = new Request.Builder()
                .url(urlBuilder.build())
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                System.out.println("API error: " + response.code());
                return List.of();  // pusta lista
            }

            String json = response.body().string();
            GameDeal[] deals = mapper.readValue(json, GameDeal[].class);
            return Arrays.asList(deals);

        } catch (IOException e) {
            e.printStackTrace();
            return List.of();  // pusta lista w razie błędu
        }
    }
}