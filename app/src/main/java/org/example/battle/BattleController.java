package org.example.battle;

import java.util.Map;

import org.example.PokeAPI;
import org.example.pokemon.Move;
import org.example.pokemon.Pokemon;
import org.example.pokemon.PokemonState;

import com.google.gson.JsonObject;
import org.example.pokemon.tmp.MoveV2;

public class BattleController {
    
    private final int MAX_TURNS = 60;

    private int currentTurn = 1;
    private String gameStatus; // active or finished
    private PokemonState pokemon1;
    private PokemonState pokemon2;

    public BattleController(Pokemon pokemon1, Pokemon pokemon2) {
        this.gameStatus = "active";
        this.pokemon1 = new PokemonState(pokemon1);
        this.pokemon2 = new PokemonState(pokemon2);
    }

    private void startBattle() {
        while (this.gameStatus == "active") {
            MoveV2 move1 = getMove();
            MoveV2 move2 = getMove();
            this.pokemon1.setMove(move1);
            this.pokemon2.setMove(move2);

            addTurn();
        }
    }

    private void getFasterPokemon(PokemonState pokemon1, PokemonState pokemon2) {

    }

    private void addTurn() {
        this.currentTurn++;
        if (currentTurn > MAX_TURNS) {
            this.gameStatus = "finished";
        }
    }

    private MoveV2 getMove() {
        JsonObject jsonMove = PokeAPI.getRandomMove();
        return PokeMoveFactory.createPokeMove(jsonMove);
    }
}
