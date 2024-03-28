package org.example.battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.PokeAPI;
import org.example.Pokemon;

import com.google.gson.JsonObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class Battle {

    Map<User, Pokemon> usersPokemons = new HashMap<>();
    SlashCommandInteractionEvent event;

    public Battle(SlashCommandInteractionEvent event) {
        this.event = event;
    }

    public void start() {
        User user1 = event.getOption("user1").getAsUser();
        User user2 = event.getOption("user2").getAsUser();
        JsonObject pokemonJson1 = PokeAPI.getRandomPokemon();
        JsonObject pokemonJson2 = PokeAPI.getRandomPokemon();
        Pokemon pokemon1 = Pokemon.create(pokemonJson1);
        Pokemon pokemon2 = Pokemon.create(pokemonJson2);

        List<MessageEmbed> embeds = getEmbeds(user1.getAvatarUrl(), user2.getAvatarUrl(), pokemon1.getSpriteUrl(), pokemon2.getSpriteUrl());
        EmbedBuilder embedInfo1 = getEmbedPokemonInfo(pokemon1, user1);
        EmbedBuilder embedInfo2 = getEmbedPokemonInfo(pokemon2, user2);
        event.getHook().sendMessageEmbeds(embedInfo1.build()).queue();
        event.getHook().sendMessageEmbeds(embedInfo2.build()).queue();
        //event.getHook().sendMessageEmbeds(embeds).queue();
    }

    public void test() {
        this.event.getHook().sendMessage("Test!!").queue();
    }

    private List<MessageEmbed> getEmbeds(String user1ImageUrl, String user2ImageUrl, String pokemon1ImageUrl, String pokemon2ImageUrl) {
        List<MessageEmbed> embedsList = new ArrayList<>();

        EmbedBuilder embed1 = new EmbedBuilder();
        embed1.setUrl("https://www.pokemon.com").setImage(user1ImageUrl);
        EmbedBuilder embed2 = new EmbedBuilder();
        embed2.setUrl("https://www.pokemon.com").setImage(user2ImageUrl);
        EmbedBuilder embed3 = new EmbedBuilder();
        embed3.setUrl("https://www.pokemon.com").setImage(pokemon1ImageUrl);
        EmbedBuilder embed4 = new EmbedBuilder();
        embed4.setUrl("https://www.pokemon.com").setImage(pokemon2ImageUrl);

        embedsList.add(embed1.build());
        embedsList.add(embed2.build());
        embedsList.add(embed3.build());
        embedsList.add(embed4.build());

        return embedsList;
    }

    private EmbedBuilder getEmbedPokemonInfo(Pokemon pokemon, User user) {
        EmbedBuilder embed = new EmbedBuilder();
        embed.setThumbnail(user.getAvatarUrl());
        embed.addField("", "**User: **" + user.getAsMention(), false);
        embed.addField("", "Name: " + pokemon.getName(), false);
        embed.addField("**HP**", ":heart_decoration: HP: " + pokemon.getHp(), true);
        embed.addField("**Attack**", ":crossed_swords: Attack: " + pokemon.getAttack(), true);
        embed.addField("**Defense**", ":shield: Defense: " + pokemon.getDefense(), true);
        embed.addField("**Sp. Attack**", ":magic_wand: Sp. Attack: " + pokemon.getSpecialAttack(), true);
        embed.addField("**Sp. Defense**", ":dna: Sp. Defense: " + pokemon.getSpecialDefense(), true);
        embed.addField("**Speed**", ":horse_racing: Speed: " + pokemon.getSpeed(), true);
        embed.setImage(pokemon.getSpriteUrl());

        return embed;
    }
}
