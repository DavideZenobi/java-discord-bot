package org.example.battle;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.example.PokeAPI;
import org.example.discord.DiscordHandler;
import org.example.pokemon.Pokemon;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class BattleHandler {

    public static final int MAX_ROUNDS = 10;
    private Map<User, Pokemon> usersPokemons = new HashMap<>();
    private User user1;
    private Pokemon pokemon1;
    private User user2;
    private Pokemon pokemon2;
    private final SlashCommandInteractionEvent event;
    private final DiscordHandler discordHandler;
    private int counter = 1;
    private List<Field> fields = new ArrayList<>();

    public BattleHandler(DiscordHandler discordHandler, SlashCommandInteractionEvent event) {
        this.discordHandler = discordHandler;
        this.event = event;
    }

    public void startSetup() {
        this.user1 = this.event.getOption("user1").getAsUser();
        this.user2 = this.event.getOption("user2").getAsUser();
        JsonObject pokemonJson1 = PokeAPI.getRandomPokemon();
        JsonObject pokemonJson2 = PokeAPI.getRandomPokemon();
        this.pokemon1 = Pokemon.create(pokemonJson1);
        this.pokemon2 = Pokemon.create(pokemonJson2);
        this.usersPokemons.put(this.user1, this.pokemon1);
        this.usersPokemons.put(this.user2, this.pokemon2);

        EmbedBuilder embedInfo1 = getEmbedPokemonInfo(pokemon1, user1);
        EmbedBuilder embedInfo2 = getEmbedPokemonInfo(pokemon2, user2);
        discordHandler.sendMessageEmbeds(embedInfo1.build());
        discordHandler.sendMessageEmbeds(embedInfo2.build());
        System.out.println("Type 1: " + this.pokemon1.getTypes());
        System.out.println("Type 2: " + this.pokemon2.getTypes());
    }

    public void run() {
        BattleController battleController = new BattleController(this.pokemon1, this.pokemon2);
        battleController.startBattle();
        /*EmbedBuilder embed = new EmbedBuilder();
        embed.setColor(Color.GREEN);
        embed.setTitle("**Logs: **");

        this.event.getHook().sendMessageEmbeds(embed.build()).queue(message -> {
            while (this.counter <= MAX_ROUNDS) {
                EmbedBuilder updatedEmbed = new EmbedBuilder();
                updatedEmbed.setTitle("**Logs: **");

                Field newField = new Field("", this.counter + ": Test", false);
                handleNewField(newField);

                for (Field field : this.fields) {
                    updatedEmbed.addField(field);
                }

                if (this.counter == MAX_ROUNDS) {
                    updatedEmbed.setColor(Color.RED);
                } else {
                    updatedEmbed.setColor(Color.GREEN);
                }

                message.editMessageEmbeds(updatedEmbed.build()).queue();
                this.counter++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    System.out.println(e);
                }
            }
        });*/
    }

    private EmbedBuilder getEmbedPokemonInfo(Pokemon pokemon, User user) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setThumbnail(user.getAvatarUrl());
        embed.addField("", "**User: **" + user.getAsMention(), false);
        embed.addField("", "**Name: **" + pokemon.getName(), false);
        embed.addField("**HP**", ":heart_decoration: HP: " + pokemon.getHp(), true);
        embed.addField("**Attack**", ":crossed_swords: Attack: " + pokemon.getAttack(), true);
        embed.addField("**Defense**", ":shield: Defense: " + pokemon.getDefense(), true);
        embed.addField("**Sp. Attack**", ":magic_wand: Sp. Attack: " + pokemon.getSpecialAttack(), true);
        embed.addField("**Sp. Defense**", ":dna: Sp. Defense: " + pokemon.getSpecialDefense(), true);
        embed.addField("**Speed**", ":horse_racing: Speed: " + pokemon.getSpeed(), true);
        embed.setImage(pokemon.getSpriteUrl());

        return embed;
    }

    private void handleNewField(Field field) {
        if (this.fields.size() >= 5) {
            this.fields.remove(0);
        }

        this.fields.add(field);
    }
}
