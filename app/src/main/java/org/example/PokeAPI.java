package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PokeAPI {

    private static final String MOVEMENT_URL = "https://pokeapi.co/api/v2/move/";
    private static final String POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final int MAX_POKEMON_LIST = 1025;
    private static final int MAX_MOVE_LIST = 919;
    
    public static JsonObject getRandomPokemon() {
        int randomNumber = (int) (Math.random() * (MAX_POKEMON_LIST - 1) + 1);
        JsonObject pokemonJson = null;
        try {
            pokemonJson = call(POKEMON_URL, randomNumber);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return pokemonJson;
    }

    public static JsonObject getRandomMove() {
        int randomNumber = (int) (Math.random() * (MAX_MOVE_LIST - 1) + 1);
        JsonObject pokemonJson = null;
        try {
            pokemonJson = call(MOVEMENT_URL, randomNumber);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return pokemonJson;
    }


    private static JsonObject call(String url, int randomNumber) throws URISyntaxException, IOException, InterruptedException {
        Gson gson = new Gson();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI(url + randomNumber))
            .GET()
            .build();

        HttpResponse<String> response = HttpClient.newBuilder()
            .build()
            .send(request, BodyHandlers.ofString());

        JsonObject pokemonJson = gson.fromJson(response.body(), JsonObject.class);
        return pokemonJson;     
    }
}