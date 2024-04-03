package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PokeAPI {

    private static final String MOVEMENT_URL = "https://pokeapi.co/api/v2/move/";
    private static final String POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final int MAX_POKEMON_LIST = 1025;
    private static final int MAX_MOVE_LIST = 919;
    // Movimientos especiales difíciles de gestionar para el combate
    private static final List<Integer> ONE_HIT_KO_MOVES_EXCLUDED = Arrays.asList(12, 32, 90, 329);
    private static final List<Integer> WHOLE_FIELD_EFFECT_MOVES_EXCLUDED = Arrays.asList(114, 201, 240, 241, 258, 300,
            346, 356, 433, 472, 478, 569, 580, 581, 587, 604, 678);
    private static final List<Integer> FIELD_EFFECT_MOVES_EXCLUDED = Arrays.asList(54, 113, 115, 191, 219, 366, 381,
            390, 446, 469, 501, 561, 564, 578, 694);
    private static final List<Integer> FORCE_SWITCH_MOVES_EXCLUDED = Arrays.asList(18, 46);
    private static final List<Integer> UNIQUE_MOVES_EXCLUDED = Arrays.asList(50, 100, 102, 116, 118, 119, 144, 150, 156,
            160, 164, 166, 169, 170, 174, 176, 180, 182, 187, 194, 197, 199, 203, 212, 214, 215, 220, 226, 227, 244,
            248, 254, 262, 266, 267, 269, 270, 271, 272, 273, 274, 277, 278, 285, 286, 287, 288, 289, 293, 312, 335,
            353, 361, 367, 375, 379, 380, 383, 384, 385, 388, 391, 392, 393, 415, 432, 461, 470, 471, 476, 487, 493,
            494, 495, 502, 504, 511, 513, 516, 567, 571, 576, 579, 582, 588, 596, 600, 603, 606, 607, 661, 668, 671,
            673, 683, 685, 689, 743, 750, 752, 753, 756, 792, 810, 816);
    // Movimientos relacionados con otros movimientos excluidos
    private static final List<Integer> RELATED_MOVES_EXCLUDED = Arrays.asList(256);

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
        int randomNumber;
        do {
            randomNumber = (int) (Math.random() * (MAX_MOVE_LIST - 1) + 1);
        } while (ONE_HIT_KO_MOVES_EXCLUDED.contains(randomNumber) ||
                WHOLE_FIELD_EFFECT_MOVES_EXCLUDED.contains(randomNumber) ||
                FIELD_EFFECT_MOVES_EXCLUDED.contains(randomNumber) ||
                FORCE_SWITCH_MOVES_EXCLUDED.contains(randomNumber) ||
                UNIQUE_MOVES_EXCLUDED.contains(randomNumber) ||
                RELATED_MOVES_EXCLUDED.contains(randomNumber));

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