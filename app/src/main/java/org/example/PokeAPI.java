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
    
    public static void getRandomPokemon() {

    }

    public static void getPokemonByName(String name) {

    }

    public static JsonObject call() throws URISyntaxException, IOException, InterruptedException {
        Gson gson = new Gson();

        HttpRequest request = HttpRequest.newBuilder()
            .uri(new URI("https://pokeapi.co/api/v2/pokemon/pikachu"))
            .GET()
            .build();

        HttpResponse<String> response = HttpClient.newBuilder()
            .build()
            .send(request, BodyHandlers.ofString());

        JsonObject pokemonJson = gson.fromJson(response.body(), JsonObject.class);
        return pokemonJson;     
    }
}