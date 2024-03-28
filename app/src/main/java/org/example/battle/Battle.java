package org.example.battle;

import java.util.HashMap;
import java.util.Map;

import org.example.PokeAPI;
import org.example.Pokemon;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Battle {

    Map<User, Pokemon> usersPokemons = new HashMap<>();
    SlashCommandInteractionEvent event;

    public Battle(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void start() {
        try {
            User user1 = event.getOption("user1").getAsUser();
            User user2 = event.getOption("user2").getAsUser();
            JsonObject pokemonJson1 = PokeAPI.call();
            JsonObject pokemonJson2 = PokeAPI.call();
            Pokemon pokemon1 = Pokemon.create(pokemonJson1);
            Pokemon pokemon2 = Pokemon.create(pokemonJson2);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void test() {
        this.event.getHook().sendMessage("Test!!").queue();
    }
}
