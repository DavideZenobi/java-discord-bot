package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

public class App extends ListenerAdapter {

    public static void main(String[] args) throws InterruptedException {

        Dotenv dotenv = Dotenv.load();
        
        JDA bot = JDABuilder.createDefault(dotenv.get("BOT_TOKEN"))
            .setActivity(Activity.playing("WoW Test"))
            .addEventListeners(new BotCommands())
            .build().awaitReady();

        Guild guild = bot.getGuildById(1215437494732980244L);

        if (guild != null) {
            guild.upsertCommand("battle", "Start battle between 2 users")
                .addOption(OptionType.USER, "user1", "First user", true)
                .addOption(OptionType.USER, "user2", "Second user", true)
                .queue();
            
            guild.upsertCommand("test", "Test slash command").queue();
        }
    }
}
