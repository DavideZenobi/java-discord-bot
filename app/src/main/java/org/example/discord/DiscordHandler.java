package org.example.discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.InteractionHook;
import org.example.battle.LoggerV2;
import org.example.pokemon.PokemonState;
import org.example.pokemon.enums.Types;
import org.example.utils.EmojiConverter;
import org.example.utils.TextFormatter;

import java.awt.*;
import java.util.*;
import java.util.List;

public class DiscordHandler {

    private final InteractionHook hook;
    private final LoggerV2 loggerV2;
    private Map<User, List<PokemonState>> usersPokemons;
    private Map<User, PokemonState> usersActivePokemons;
    private PokemonState activePokemon1;
    private PokemonState activePokemon2;
    /**
     *  El mensaje devuelto una vez se envia por primera vez el embed/respuesta.
     *  Guardarlo sirve para editarlo más tarde.
     * */
    private Message message;

    public DiscordHandler(InteractionHook hook, LoggerV2 loggerV2) {
        this.hook = hook;
        this.loggerV2 = loggerV2;
        this.usersActivePokemons = new LinkedHashMap<>();
    }

    public void initialSetup(Map<User, List<PokemonState>> usersPokemons, User user1, User user2, PokemonState activePokemon1, PokemonState activePokemon2) {
        this.usersPokemons = usersPokemons;
        this.usersActivePokemons.put(user1, activePokemon1);
        this.usersActivePokemons.put(user2, activePokemon2);
        this.activePokemon1 = activePokemon1;
        this.activePokemon2 = activePokemon2;
    }

    public void sendPokemonInfoEmbed() {
        /** Se crea el embed y se settean ciertos parámetros */
        EmbedBuilder originalEmbed = new EmbedBuilder();
        originalEmbed.setUrl("https://www.pokemon.com");
        originalEmbed.setColor(Color.GREEN);

        /** Se crean los campos de los equipos de pokemons de los jugadores. Luego se añaden al embed */
        List<MessageEmbed.Field> fields = new ArrayList<>();
        for (Map.Entry<User, List<PokemonState>> userPokemons : this.usersPokemons.entrySet()) {
            MessageEmbed.Field field = getTeamField(userPokemons.getKey(), userPokemons.getValue());
            fields.add(field);
        }

        /** Field necesario para que los siguientes aparezcan en la linea de abajo */
        fields.add(new MessageEmbed.Field("", "", true));

        for (MessageEmbed.Field field : fields) {
            originalEmbed.addField(field);
        }

        originalEmbed.addField(getPokemonActiveAndCurrentHpField(this.activePokemon1));
        originalEmbed.addField(getPokemonActiveAndCurrentHpField(this.activePokemon2));

        originalEmbed.setImage(this.activePokemon1.getSpriteUrl());

        /** Finalmente se crea un segundo embed para settear la segunda imagen y la url.
         *  De esta manera, se juntan los 2 embeds en 1. */
        EmbedBuilder secondEmbed = new EmbedBuilder();
        secondEmbed.setUrl("https://www.pokemon.com");
        secondEmbed.setImage(this.activePokemon2.getSpriteUrl());

        this.message = this.hook.sendMessageEmbeds(originalEmbed.build(), secondEmbed.build()).complete();
    }

    public void update(int turnNumber) {
        EmbedBuilder updatedEmbedBuilder = new EmbedBuilder();
        updatedEmbedBuilder.setUrl("https://www.pokemon.com");
        updatedEmbedBuilder.setColor(Color.GREEN);

        List<MessageEmbed.Field> updatedFields = new ArrayList<>();
        for (Map.Entry<User, List<PokemonState>> userPokemons : this.usersPokemons.entrySet()) {
            MessageEmbed.Field field = getTeamField(userPokemons.getKey(), userPokemons.getValue());
            updatedFields.add(field);
        }

        updatedFields.add(new MessageEmbed.Field("", "", true));

        for (MessageEmbed.Field updatedField : updatedFields) {
            updatedEmbedBuilder.addField(updatedField);
        }

        for (Map.Entry<User, PokemonState> userActivePokemon : usersActivePokemons.entrySet()) {
            updatedEmbedBuilder.addField(getPokemonActiveAndCurrentHpField(userActivePokemon.getValue()));
        }

        updatedEmbedBuilder.addField(new MessageEmbed.Field("", "", true));

        updatedEmbedBuilder.addField(getLogsField(turnNumber)); // Logs

        Iterator<PokemonState> iterator = usersActivePokemons.values().iterator();
        updatedEmbedBuilder.setImage(iterator.next().getSpriteUrl());

        EmbedBuilder updatedSecondEmbedBuilder = new EmbedBuilder();
        updatedSecondEmbedBuilder.setUrl("https://www.pokemon.com");
        updatedSecondEmbedBuilder.setImage(iterator.next().getSpriteUrl());

        this.message.editMessageEmbeds(updatedEmbedBuilder.build(), updatedSecondEmbedBuilder.build()).complete();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void lastUpdate(int turnNumber) {
        EmbedBuilder updatedEmbedBuilder = new EmbedBuilder();
        updatedEmbedBuilder.setUrl("https://www.pokemon.com");
        updatedEmbedBuilder.setColor(Color.RED);

        List<MessageEmbed.Field> updatedFields = new ArrayList<>();
        for (Map.Entry<User, List<PokemonState>> userPokemons : this.usersPokemons.entrySet()) {
            MessageEmbed.Field field = getTeamField(userPokemons.getKey(), userPokemons.getValue());
            updatedFields.add(field);
        }

        updatedFields.add(new MessageEmbed.Field("", "", true));

        for (MessageEmbed.Field updatedField : updatedFields) {
            updatedEmbedBuilder.addField(updatedField);
        }

        for (Map.Entry<User, PokemonState> userActivePokemon : usersActivePokemons.entrySet()) {
            updatedEmbedBuilder.addField(getPokemonActiveAndCurrentHpField(userActivePokemon.getValue()));
        }

        updatedEmbedBuilder.addField(new MessageEmbed.Field("", "", true));

        updatedEmbedBuilder.addField(getLogsField(turnNumber)); // Logs

        Iterator<PokemonState> iterator = usersActivePokemons.values().iterator();
        updatedEmbedBuilder.setImage(iterator.next().getSpriteUrl());

        EmbedBuilder updatedSecondEmbedBuilder = new EmbedBuilder();
        updatedSecondEmbedBuilder.setUrl("https://www.pokemon.com");
        updatedSecondEmbedBuilder.setImage(iterator.next().getSpriteUrl());

        this.message.editMessageEmbeds(updatedEmbedBuilder.build(), updatedSecondEmbedBuilder.build()).complete();
    }

    public void updateActivePokemon(User user, PokemonState newActivePokemon) {
        this.usersActivePokemons.replace(user, newActivePokemon);
    }

    /**
     *  Devuelve un Field con los nombres de los pokemons de un usuario y los emojis correspondientes a los tipos.
     * */
    private MessageEmbed.Field getTeamField(User user, List<PokemonState> pokemons) {
        StringBuilder values = new StringBuilder();
        for (PokemonState pokemon : pokemons) {
            StringBuilder pokemonTypes = new StringBuilder();
            for (Types type : pokemon.getTypes()) {
                pokemonTypes.append(EmojiConverter.convertType(type)).append(" ");
            }

            String value = pokemonTypes + TextFormatter.formatPokemonName(pokemon.getName()) + (pokemon.getCurrentHp() <= 0 ? " :skull_crossbones:" : "");
            values.append(value).append("\n");
        }
        return new MessageEmbed.Field("", user.getAsMention() + "\n" + "**Team:** \n" + values, true);
    }

    /**
     *  Devuelve un Field con el nombre del pokemon activo de un usuario y la vida actual.
     * */
    private MessageEmbed.Field getPokemonActiveAndCurrentHpField(PokemonState activePokemon) {
        return new MessageEmbed.Field("", TextFormatter.convertToBold(activePokemon.getName()) + " :heart_decoration: " + activePokemon.getCurrentHp() + "/" + activePokemon.getBaseHp(), true);
    }

    private MessageEmbed.Field getLogsField(int turnNumber) {
        StringBuilder values = new StringBuilder();
        for (String log : this.loggerV2.getLogsByTurn(turnNumber)) {
            values.append(log).append("\n");
        }

        return new MessageEmbed.Field("", values.toString(), true);
    }

}
