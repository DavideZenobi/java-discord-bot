package org.example.battle;

import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.example.PokeAPI;
import org.example.discord.DiscordHandler;
import org.example.pokemon.Pokemon;
import org.example.pokemon.PokemonState;
import org.example.pokemon.moves.MoveV2;
import org.example.pokemon.moves.PokeMoveFactory;

import java.util.*;

public class BattleControllerV2 {

    private final UUID uuid;
    private final SlashCommandInteractionEvent event;
    private final DiscordHandler discordHandler;
    private final LoggerV2 loggerV2;
    private final int MAX_TURNS = 100;
    private int currentTurn;
    private boolean hasGameFinished;
    private Map<User, List<PokemonState>> usersPokemons;
    private Map<User, PokemonState> userActivePokemon1;
    private Map<User, PokemonState> userActivePokemon2;
    private final User user1;
    private final User user2;
    private PokemonState activePokemon1;
    private PokemonState activePokemon2;
    private int totalPokemons;

    public BattleControllerV2(SlashCommandInteractionEvent event, DiscordHandler discordHandler, LoggerV2 loggerV2, int totalPokemons) {
        this.uuid = UUID.randomUUID();
        this.event = event;
        this.discordHandler = discordHandler;
        this.loggerV2 = loggerV2;
        this.totalPokemons = totalPokemons;
        this.currentTurn = 1;
        this.hasGameFinished = false;
        this.usersPokemons = new LinkedHashMap<>();
        this.user1 = event.getOption("user1").getAsUser();
        this.user2 = event.getOption("user2").getAsUser();
    }

    public void setup() {
        List<JsonObject> pokemonsJson1 = PokeAPI.getMultipleRandomPokemons(this.totalPokemons);
        List<JsonObject> pokemonsJson2 = PokeAPI.getMultipleRandomPokemons(this.totalPokemons);

        List<PokemonState> pokemons1 = new ArrayList<>();
        List<PokemonState> pokemons2 = new ArrayList<>();

        for (JsonObject pokemonJson : pokemonsJson1) {
            PokemonState pokemon = new PokemonState(Pokemon.create(pokemonJson));
            pokemons1.add(pokemon);
        }

        for (JsonObject pokemonJson : pokemonsJson2) {
            PokemonState pokemon = new PokemonState(Pokemon.create(pokemonJson));
            pokemons2.add(pokemon);
        }

        this.usersPokemons.put(this.user1, pokemons1);
        this.usersPokemons.put(this.user2, pokemons2);
        this.activePokemon1 = pokemons1.getFirst();
        this.activePokemon2 = pokemons2.getFirst();
        this.discordHandler.initialSetup(this.usersPokemons, this.user1, this.user2, this.activePokemon1, this.activePokemon2);
        this.discordHandler.sendPokemonInfoEmbed();
    }

    public void run() {
        this.loggerV2.setNewBattle(this.activePokemon1.getName(), this.activePokemon2.getName());
        //this.loggerV2.setNewBattle(this.currentTurn, this.activePokemon1.getName(), this.activePokemon2.getName());

        while (this.currentTurn <= this.MAX_TURNS) {
            this.loggerV2.saveNewTurn(this.currentTurn);
            boolean isActivePokemon1Dead = false;
            boolean isActivePokemon2Dead = false;

            MoveV2 move1 = getMove();
            MoveV2 move2 = getMove();
            this.activePokemon1.setMove(move1);
            this.activePokemon2.setMove(move2);

            var pokemonsOrder = getFasterPokemon();

            Turn turn = new Turn(this.uuid, this.activePokemon1, this.activePokemon2, this.loggerV2, this.discordHandler, this.currentTurn);

            /** FIRST MOVE **/
            turn.applyMove(pokemonsOrder.first, pokemonsOrder.second);

            isActivePokemon1Dead = isPokemonDead(this.activePokemon1);
            if (isActivePokemon1Dead) {
                boolean canChange = canChange(this.user1);

                if (canChange) {
                    this.activePokemon1 = changePokemon(this.user1);
                    turn.cleanFlinchStatus(this.activePokemon1, this.activePokemon2);
                    this.loggerV2.savePokemonChange(this.user1, this.activePokemon1.getName());
                    this.loggerV2.savePokemonChange(this.currentTurn, this.user1, this.activePokemon1.getName());
                    this.discordHandler.update(this.currentTurn);
                    turn.applyEndOfTurnDamage(this.activePokemon1);
                    turn.applyEndOfTurnDamage(this.activePokemon2);
                    turn.updateAilmentsTurns(this.activePokemon1);
                    turn.updateAilmentsTurns(this.activePokemon2);
                    this.currentTurn++;
                    continue;
                } else {
                    handleGameFinished(this.user2);
                    break;
                }
            }

            isActivePokemon2Dead = isPokemonDead(this.activePokemon2);
            if (isActivePokemon2Dead) {
                boolean canChange = canChange(this.user2);

                if (canChange) {
                    this.activePokemon2 = changePokemon(this.user2);
                    turn.cleanFlinchStatus(this.activePokemon1, this.activePokemon2);
                    this.loggerV2.savePokemonChange(this.user2, this.activePokemon2.getName());
                    this.loggerV2.savePokemonChange(this.currentTurn, this.user2, this.activePokemon2.getName());
                    this.discordHandler.update(this.currentTurn);
                    turn.applyEndOfTurnDamage(this.activePokemon1);
                    turn.applyEndOfTurnDamage(this.activePokemon2);
                    turn.updateAilmentsTurns(this.activePokemon1);
                    turn.updateAilmentsTurns(this.activePokemon2);
                    this.currentTurn++;
                    continue;
                } else {
                    handleGameFinished(this.user1);
                    break;
                }
            }

            /** SECOND MOVE **/
            turn.applyMove(pokemonsOrder.second, pokemonsOrder.first);

            isActivePokemon1Dead = isPokemonDead(this.activePokemon1);
            if (isActivePokemon1Dead) {
                boolean canChange = canChange(this.user1);

                if (canChange) {
                    this.activePokemon1 = changePokemon(this.user1);
                    turn.cleanFlinchStatus(this.activePokemon1, this.activePokemon2);
                    this.loggerV2.savePokemonChange(this.user1, this.activePokemon1.getName());
                    this.loggerV2.savePokemonChange(this.currentTurn, this.user1, this.activePokemon1.getName());
                    this.discordHandler.update(this.currentTurn);
                    turn.applyEndOfTurnDamage(this.activePokemon1);
                    turn.applyEndOfTurnDamage(this.activePokemon2);
                    turn.updateAilmentsTurns(this.activePokemon1);
                    turn.updateAilmentsTurns(this.activePokemon2);
                    this.currentTurn++;
                    continue;
                } else {
                    handleGameFinished(this.user2);
                    break;
                }
            }

            isActivePokemon2Dead = isPokemonDead(this.activePokemon2);
            if (isActivePokemon2Dead) {
                boolean canChange = canChange(this.user2);

                if (canChange) {
                    this.activePokemon2 = changePokemon(this.user2);
                    turn.cleanFlinchStatus(this.activePokemon1, this.activePokemon2);
                    this.loggerV2.savePokemonChange(this.user2, this.activePokemon2.getName());
                    this.loggerV2.savePokemonChange(this.currentTurn, this.user2, this.activePokemon2.getName());
                    this.discordHandler.update(this.currentTurn);
                    turn.applyEndOfTurnDamage(this.activePokemon1);
                    turn.applyEndOfTurnDamage(this.activePokemon2);
                    turn.updateAilmentsTurns(this.activePokemon1);
                    turn.updateAilmentsTurns(this.activePokemon2);
                    this.currentTurn++;
                    continue;
                } else {
                    handleGameFinished(this.user1);
                    break;
                }
            }

            /** APLICAR DAÑOS FINAL DE TURNO (BURN, POSION, ETC) **/
            turn.applyEndOfTurnDamage(this.activePokemon1);
            turn.applyEndOfTurnDamage(this.activePokemon2);
            turn.updateAilmentsTurns(this.activePokemon1);
            turn.updateAilmentsTurns(this.activePokemon2);

            isActivePokemon1Dead = isPokemonDead(this.activePokemon1);
            if (isActivePokemon1Dead) {
                boolean canChange = canChange(this.user1);

                if (canChange) {
                    this.activePokemon1 = changePokemon(this.user1);
                    turn.cleanFlinchStatus(this.activePokemon1, this.activePokemon2);
                    this.loggerV2.savePokemonChange(this.user1, this.activePokemon1.getName());
                    this.loggerV2.savePokemonChange(this.currentTurn, this.user1, this.activePokemon1.getName());
                    this.discordHandler.update(this.currentTurn);
                    this.currentTurn++;
                    continue;
                } else {
                    handleGameFinished(this.user2);
                    break;
                }
            }

            isActivePokemon2Dead = isPokemonDead(this.activePokemon2);
            if (isActivePokemon2Dead) {
                boolean canChange = canChange(this.user2);

                if (canChange) {
                    this.activePokemon2 = changePokemon(this.user2);
                    turn.cleanFlinchStatus(this.activePokemon1, this.activePokemon2);
                    this.loggerV2.savePokemonChange(this.user2, this.activePokemon2.getName());
                    this.loggerV2.savePokemonChange(this.currentTurn, this.user2, this.activePokemon2.getName());
                    this.discordHandler.update(this.currentTurn);
                    this.currentTurn++;
                    continue;
                } else {
                    handleGameFinished(this.user1);
                    break;
                }
            }

            turn.cleanFlinchStatus(this.activePokemon1, this.activePokemon2);
            this.discordHandler.update(this.currentTurn);
            this.currentTurn++;
        }

        this.loggerV2.showAllLogs();
    }

    private PokemonState changePokemon(User user) {
        List<PokemonState> pokemons = this.usersPokemons.get(user);
        for (PokemonState pokemon : pokemons) {
            if (pokemon.getCurrentHp() > 0) {
                this.discordHandler.updateActivePokemon(user, pokemon);
                return pokemon;
            }
        }

        return null;
    }

    private boolean canChange(User user) {
        List<PokemonState> pokemons = this.usersPokemons.get(user);
        for (PokemonState pokemon : pokemons) {
            if (pokemon.getCurrentHp() > 0) {
                return true;
            }
        }

        return false;
    }

    record Tuple(PokemonState first, PokemonState second) { }

    private BattleControllerV2.Tuple getFasterPokemon() {
        /** Primero se controlan las prioridades de los movimientos
         *  Luego las velocidades de los pokemons */
        if (this.activePokemon1.getMove().priority() > this.activePokemon2.getMove().priority()) {
            return new BattleControllerV2.Tuple(this.activePokemon1, this.activePokemon2);
        } else if (this.activePokemon1.getMove().priority() < this.activePokemon2.getMove().priority()) {
            return new BattleControllerV2.Tuple(this.activePokemon2, this.activePokemon1);
        } else if (this.activePokemon1.getCurrentSpeed() > this.activePokemon2.getCurrentSpeed()) {
            return new BattleControllerV2.Tuple(this.activePokemon1, this.activePokemon2);
        } else if (this.activePokemon1.getCurrentSpeed() < this.activePokemon2.getCurrentSpeed()) {
            return new BattleControllerV2.Tuple(this.activePokemon2, this.activePokemon1);
        }

        Random random = new Random();
        if (random.nextBoolean()) {
            return new BattleControllerV2.Tuple(this.activePokemon1, this.activePokemon2);
        } else {
            return new BattleControllerV2.Tuple(this.activePokemon2, this.activePokemon1);
        }
    }

    private boolean isPokemonDead(PokemonState pokemon) {
        if (pokemon.getCurrentHp() <= 0) {
            this.loggerV2.savePokemonDied(pokemon.getName());
            this.loggerV2.savePokemonDied(this.currentTurn, pokemon.getName());
            return true;
        } else {
            return false;
        }
    }

    private void handleGameFinished(User winnerUser) {
        this.loggerV2.saveWinnerUser(winnerUser);
        this.loggerV2.saveWinnerUser(this.currentTurn, winnerUser);
        this.discordHandler.lastUpdate(this.currentTurn);
    }

    private MoveV2 getMove() {
        JsonObject jsonMove = PokeAPI.getRandomMove();
        return PokeMoveFactory.createPokeMove(jsonMove);
    }
}
