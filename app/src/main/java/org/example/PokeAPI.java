package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

public class PokeAPI {

    private static final String MOVEMENT_URL = "https://pokeapi.co/api/v2/move/";
    private static final String POKEMON_URL = "https://pokeapi.co/api/v2/pokemon/";
    private static final int MAX_POKEMON_LIST = 1025;
    private static final int MAX_MOVE_LIST = 919;
    /** Movimientos especiales difíciles de gestionar para el combate */
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
    /** Estos movimientos no tienen el campo meta en la api, por lo tanto no se pueden gestionar */
    private static final List<Integer> NO_META_INFO_MOVES_EXCLUDED = new ArrayList<>();
    static {
        int start = 827;
        int end = 919;
        for (int i = start; i <= end; i++) {
            NO_META_INFO_MOVES_EXCLUDED.add(i);
        }
    }
    /** Movimientos relacionados con otros movimientos excluidos */
    private static final List<Integer> RELATED_MOVES_EXCLUDED = Arrays.asList(256);


    /** Movimientos específicos que no están bien clasificados en la API, por lo tanto difíciles de gestionar
     * Ejemplo: ID: 162     Nombre: Superdiente       Categoria: damage   Power: null
     * Efecto: Inflige daño igual a la mitad de salud restante del pokemon.
     * Problema: La info del movimiento está en formato texto. No hay un flag que te indique que el power no es un
     * número como tal, sino que podría ser un %. Como no existe, la api devuelve null en el campo power*/
    private static final List<Integer> PERSONAL_MOVES_EXCLUDED = Arrays.asList(49, 67, 68, 69, 73, 82, 101, 117, 149, 162,
            175, 179, 213, 216, 217, 218, 222, 243, 251, 255, 283, 360, 363, 368, 374, 376, 378, 382, 386, 447, 462, 484,
            486, 515, 535, 622, 623, 624, 625, 626, 627, 628, 629, 630, 631, 632, 633, 634, 635, 636, 637, 638, 639,
            640, 641, 642, 643, 644, 645, 646, 647, 648, 649, 650, 651, 652, 653, 654, 655, 656, 657, 698, 717, 732,
            741);

    private static final List<Integer> FORBIDDEN_AILMENT_MOVES_EXCLUDED = Arrays.asList(193, 195, 213, 259, 275, 281,
            316, 357, 373, 377, 477, 749);

    static {
        int bannedMoves = ONE_HIT_KO_MOVES_EXCLUDED.size() + WHOLE_FIELD_EFFECT_MOVES_EXCLUDED.size() +
                FIELD_EFFECT_MOVES_EXCLUDED.size() + FORCE_SWITCH_MOVES_EXCLUDED.size() +
                UNIQUE_MOVES_EXCLUDED.size() + NO_META_INFO_MOVES_EXCLUDED.size() +
                RELATED_MOVES_EXCLUDED.size() + PERSONAL_MOVES_EXCLUDED.size();
        int availableMoves = MAX_MOVE_LIST - bannedMoves;
        System.out.println("Available Moves: " + availableMoves);
        System.out.println("Total Moves BANNED: " + bannedMoves);
    }

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
                NO_META_INFO_MOVES_EXCLUDED.contains(randomNumber) ||
                RELATED_MOVES_EXCLUDED.contains(randomNumber) ||
                PERSONAL_MOVES_EXCLUDED.contains(randomNumber) ||
                FORBIDDEN_AILMENT_MOVES_EXCLUDED.contains(randomNumber));

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

        JsonObject bodyJson = gson.fromJson(response.body(), JsonObject.class);
        return bodyJson;
    }
}