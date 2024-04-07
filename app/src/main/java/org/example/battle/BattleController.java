package org.example.battle;

import com.google.gson.JsonObject;
import org.example.PokeAPI;
import org.example.pokemon.Pokemon;
import org.example.pokemon.PokemonState;
import org.example.pokemon.moves.MoveV2;

import java.util.Random;

public class BattleController {

    private final int MAX_TURNS = 1;

    private int currentTurn = 1;
    private String gameStatus; // active or finished
    private PokemonState pokemon1;
    private PokemonState pokemon2;

    public BattleController(Pokemon pokemon1, Pokemon pokemon2) {
        this.gameStatus = "active";
        this.pokemon1 = new PokemonState(pokemon1);
        this.pokemon2 = new PokemonState(pokemon2);
    }

    public void startBattle() {
        while (this.gameStatus.equals("active") && this.currentTurn <= this.MAX_TURNS) {
            Turn turn = new Turn(this.pokemon1, this.pokemon2, this.currentTurn);

            System.out.println("CURRENT TURN: " + this.currentTurn);

            MoveV2 move1 = getMove();
            MoveV2 move2 = getMove();
            this.pokemon1.setMove(move1);
            this.pokemon2.setMove(move2);

            // Quien va primero
            var pokemonsOrder = getFasterPokemon();

            // Aplicar primer dano
            turn.applyMove(pokemonsOrder.first, pokemonsOrder.second);
            turn.applyStageModifiers(pokemonsOrder.first, pokemonsOrder.second);

            // Check vida
            checkPokemonHp();

            // Aplicar segundo dano
            turn.applyMove(pokemonsOrder.second, pokemonsOrder.first);
            turn.applyStageModifiers(pokemonsOrder.first, pokemonsOrder.second);

            // Check vida
            checkPokemonHp();

            // Aplicar otros efectos
            turn.applyFinalEffects(pokemonsOrder.second);
            turn.applyFinalEffects(pokemonsOrder.first);

            System.out.println("Pokemon 1 HP: " + this.pokemon1.getCurrentHp());
            System.out.println("Pokemon 2 HP: " + this.pokemon2.getCurrentHp());

            this.currentTurn++;
        }
    }

    record Tuple(PokemonState first, PokemonState second) { }

    private Tuple getFasterPokemon() {
        /** Primero se controlan las prioridades de los movimientos
         *  Luego las velocidades de los pokemons */
        if (this.pokemon1.getMove().priority() > this.pokemon2.getMove().priority()) {
            return new Tuple(this.pokemon1, this.pokemon2);
        } else if (this.pokemon1.getMove().priority() < this.pokemon2.getMove().priority()) {
            return new Tuple(this.pokemon2, this.pokemon1);
        } else if (this.pokemon1.getCurrentSpeed() > this.pokemon2.getCurrentSpeed()) {
            return new Tuple(this.pokemon1, this.pokemon2);
        } else if (this.pokemon1.getCurrentSpeed() < this.pokemon2.getCurrentSpeed()) {
            return new Tuple(this.pokemon2, this.pokemon1);
        }

        Random random = new Random();
        if (random.nextBoolean()) {
            return new Tuple(this.pokemon1, this.pokemon2);
        } else {
            return new Tuple(this.pokemon2, this.pokemon1);
        }
    }

    private void checkPokemonHp() {
        if (this.pokemon1.getCurrentHp() <= 0) {
            this.gameStatus = "finished";
        } else if (this.pokemon2.getCurrentHp() <= 0) {
            this.gameStatus = "finished";
        }
    }

    private MoveV2 getMove() {
        JsonObject jsonMove = PokeAPI.getRandomMove();
        return PokeMoveFactory.createPokeMove(jsonMove);
    }

}
