package org.example;

import org.example.battle.BattleHandler;
import org.example.discord.DiscordHandler;
import org.jetbrains.annotations.NotNull;

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
        BattleHandler battle = new BattleHandler(new DiscordHandler(event.getHook()), event);
        battle.startSetup();
        battle.run();
    }

    private void handleTestCommand(SlashCommandInteractionEvent event) {
    }

}
