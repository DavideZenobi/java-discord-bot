package org.example;

import org.example.battle.Battle;
import org.jetbrains.annotations.NotNull;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class BotCommands extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event)  {

        switch (event.getName()) {
            case "battle":
                event.deferReply().queue();
                Battle battle = new Battle(event);
                battle.test();
                //event.getHook().sendMessageEmbeds(embed1.build(), embed2.build(), embed3.build(), embed4.build()).queue();
                break;

            case "test":

                break;
            default:
                break;
        }
        
        if (event.getName().equals("battle")) {
            
        }
    }

}
