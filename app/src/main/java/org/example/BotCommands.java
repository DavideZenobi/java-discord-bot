package org.example;

import org.example.battle.Battle;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommands extends ListenerAdapter {
    
    enum EventName {
        battle,
        test,
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)  {
        var eventName = EventName.valueOf(event.getName());

        switch (eventName) {
            case battle -> handleBattleCommand(event);
            case test -> handleTestCommand(event);
        }
    }

    private void handleBattleCommand(SlashCommandInteractionEvent event) {
        event.deferReply().queue();
        Battle battle = new Battle(event);
        battle.start();
        //event.getHook().sendMessageEmbeds(embed1.build(), embed2.build(), embed3.build(), embed4.build()).queue();
    }

    private void handleTestCommand(SlashCommandInteractionEvent event) {
    }

}
