package org.example.battle;

import com.google.gson.JsonObject;
import org.example.PokeAPI;
import org.example.discord.DiscordHandler;
import org.example.pokemon.Pokemon;
import org.example.pokemon.PokemonState;
import org.example.pokemon.moves.MoveV2;
import org.example.pokemon.moves.PokeMoveFactory;

import java.util.Random;
import java.util.UUID;

public class BattleController {

    UUID uuid;
    private final int MAX_TURNS = 30;

    private int currentTurn = 1;
    private String gameStatus; // active or finished
    private PokemonState pokemon1;
    private PokemonState pokemon2;

    public BattleController(UUID uuid, Pokemon pokemon1, Pokemon pokemon2) {
        this.uuid = uuid;
        this.pokemon1 = new PokemonState(pokemon1);
        this.pokemon2 = new PokemonState(pokemon2);
    }

    public void startBattle() {
        /*Logger.setNewBattle(this.uuid, this.pokemon1.getName(), this.pokemon2.getName());

        while (this.gameStatus.equals("active") && this.currentTurn <= this.MAX_TURNS) {
            Logger.saveNewTurn(this.uuid, this.currentTurn);
            Logger.savePokemonCurrentHp(this.uuid, this.pokemon1.getName(), this.pokemon2.getName(), this.pokemon1.getCurrentHp(), this.pokemon2.getCurrentHp());

            boolean isPokemonDead = false;

            MoveV2 move1 = getMove();
            MoveV2 move2 = getMove();
            this.pokemon1.setMove(move1);
            this.pokemon2.setMove(move2);

            // Quien va primero
            var pokemonsOrder = getFasterPokemon();

            Turn turn = new Turn(this.uuid, this.pokemon1, this.pokemon2, this.currentTurn);

            // Aplicar primer dano
            turn.applyMove(pokemonsOrder.first, pokemonsOrder.second);

            // Check vida
            isPokemonDead = isPokemonDead(pokemonsOrder.first);
            if (isPokemonDead) {
                break;
            }
            isPokemonDead = isPokemonDead(pokemonsOrder.second);
            if (isPokemonDead) {
                break;
            }

            // Aplicar segundo dano
            turn.applyMove(pokemonsOrder.second, pokemonsOrder.first);

            // Check vida
            isPokemonDead = isPokemonDead(pokemonsOrder.first);
            if (isPokemonDead) {
                break;
            }
            isPokemonDead = isPokemonDead(pokemonsOrder.second);
            if (isPokemonDead) {
                break;
            }

            // Aplicar efectos finales
            turn.applyEndOfTurnDamage(pokemonsOrder.second);
            turn.applyEndOfTurnDamage(pokemonsOrder.first);

            turn.updateAilmentsTurns(pokemonsOrder.first);
            turn.updateAilmentsTurns(pokemonsOrder.second);

            isPokemonDead = isPokemonDead(pokemonsOrder.first);
            if (isPokemonDead) {
                break;
            }
            isPokemonDead = isPokemonDead(pokemonsOrder.second);
            if (isPokemonDead) {
                break;
            }

            turn.cleanFlinchStatus(pokemonsOrder.first, pokemonsOrder.second);
            this.currentTurn++;
        }

        Logger.showAllLogs(this.uuid);*/
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

    private boolean isPokemonDead(PokemonState pokemon) {
        if (pokemon.getCurrentHp() <= 0) {
            Logger.savePokemonDied(this.uuid, pokemon.getName());
            return true;
        } else {
            return false;
        }
    }

    private MoveV2 getMove() {
        JsonObject jsonMove = PokeAPI.getRandomMove();
        return PokeMoveFactory.createPokeMove(jsonMove);
    }

}
