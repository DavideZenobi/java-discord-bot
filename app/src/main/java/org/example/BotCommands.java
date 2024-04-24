package org.example;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.example.battle.BattleControllerV2;
import org.example.battle.BattleHandler;
import org.example.battle.LoggerV2;
import org.example.discord.DiscordHandler;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class BotCommands extends ListenerAdapter {
    
    enum EventName {
        battle,
        battlev2,
        test,
    }

    private final List<String> guildsBattlesStarted = new ArrayList<>();

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        String messageReceived = event.getMessage().getContentRaw();
        if (messageReceived.contains("ping")) {
            event.getChannel().sendMessage("pong").queue();
        }
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)  {
        var eventName = EventName.valueOf(event.getName());

        switch (eventName) {
            //case battle -> handleBattleCommand(event);
            case battle -> handleBattleV2Command(event);
            case test -> handleTestCommand(event);
        }
    }

    /*private void handleBattleCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        LoggerV2 loggerV2 = new LoggerV2();
        BattleHandler battle = new BattleHandler(new DiscordHandler(event.getHook(), loggerV2), event);
        battle.startSetup();
        battle.run();
    }*/

    private void handleBattleV2Command(SlashCommandInteractionEvent event) {
        if (this.guildsBattlesStarted.contains(event.getGuild().getId())) {
            event.reply("There is already a battle started in this guild").queue();
        } else {
            this.guildsBattlesStarted.add(event.getGuild().getId());
            event.deferReply().complete();
            OptionMapping totalOption = event.getOption("pokemonsxteam");
            int pokemonsPerTeam;
            if (totalOption == null) {
                pokemonsPerTeam = 1; // Default
            } else if (totalOption.getAsInt() > 0 && totalOption.getAsInt() <= 6) {
                pokemonsPerTeam = totalOption.getAsInt();
            } else if (totalOption.getAsInt() <= 0){
                pokemonsPerTeam = 1; // Min 1 pokemon
            } else {
                pokemonsPerTeam = 6; // Max 6 pokemons
            }
            LoggerV2 loggerV2 = new LoggerV2();
            BattleControllerV2 battleControllerV2 = new BattleControllerV2(event, new DiscordHandler(event.getHook(), loggerV2), loggerV2, pokemonsPerTeam);
            battleControllerV2.setup();
            battleControllerV2.run();
            this.guildsBattlesStarted.remove(event.getGuild().getId());
        }
    }

    private void handleTestCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        User user1 = event.getOption("user1").getAsUser();
        User user2 = event.getOption("user2").getAsUser();
        EmbedBuilder embedImage1 = new EmbedBuilder();
        embedImage1.setImage(user1.getAvatarUrl());
        embedImage1.addField(
                "Name",
                "Value \n" +
                        "value 2 \n" +
                        "value 3\n",
                true);
        embedImage1.addField("Name 2", "Value 2", true);
        embedImage1.setUrl("https://www.marca.com");
        EmbedBuilder embedImage2 = new EmbedBuilder();
        embedImage2.setImage(user2.getAvatarUrl());
        embedImage2.setUrl("https://www.marca.com");
        event.getHook().sendMessageEmbeds(embedImage1.build(), embedImage2.build()).queue();
    }

}
