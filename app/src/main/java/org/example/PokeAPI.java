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
    
    public static JsonObject getRandomPokemon() {
        int randomNumber = (int) (Math.random() * (1025 - 1) + 1);
        JsonObject pokemonJson = null;
        try {
            pokemonJson = call(randomNumber);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return pokemonJson;
    }

    public static void getPokemonByName(String name) {

    }

    public static JsonObject call(int randomNumber) throws URISyntaxException, IOException, InterruptedException {
        Gson gson = new Gson();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("https://pokeapi.co/api/v2/pokemon/" + randomNumber))
            .GET()
            .build();

        HttpResponse<String> response = HttpClient.newBuilder()
            .build()
            .send(request, BodyHandlers.ofString());

        JsonObject pokemonJson = gson.fromJson(response.body(), JsonObject.class);
        return pokemonJson;     
    }
}