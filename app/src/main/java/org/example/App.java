package org.example;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.Executors;

public class App {

    public static void main(String[] args) throws InterruptedException {

        Dotenv dotenv = Dotenv.load();
        
        JDA bot = JDABuilder.createDefault(dotenv.get("BOT_TOKEN"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.MESSAGE_CONTENT)
                .setEventPool(Executors.newFixedThreadPool(100))
            .setActivity(Activity.playing("Type /battle"))
            .addEventListeners(new BotCommands())
            .build().awaitReady();

        /**
         *  Global command
         * */
        bot.updateCommands().addCommands(
                Commands.slash("battle", "Start randomized pokemon battle between 2 users")
                        .setGuildOnly(true)
                        .addOption(OptionType.USER, "user1", "First user", true)
                        .addOption(OptionType.USER, "user2", "Second user", true)
                        .addOption(OptionType.INTEGER, "total", "Maximum: 6 - Default: 1", false)
        ).queue();

        /**
         *  Guild command
         * */
        long testGuildId = 1215437494732980244L;
        Guild guild = bot.getGuildById(testGuildId);
        guild.updateCommands().addCommands(
                Commands.slash("test", "Test Command")
                        .addOption(OptionType.USER, "user1", "First user", true)
                        .addOption(OptionType.USER, "user2", "Second user", true)
                        .addOption(OptionType.INTEGER, "total", "Maximum: 6 - Default: 1", false)
        ).queue();
    }
}
