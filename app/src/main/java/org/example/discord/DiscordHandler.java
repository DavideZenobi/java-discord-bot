package org.example.discord;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.InteractionHook;

public class DiscordHandler {

    private final InteractionHook hook;

    public DiscordHandler(InteractionHook hook) {
        this.hook = hook;
    }

    public void sendMessageEmbeds(MessageEmbed messageEmbed) {
        hook.sendMessageEmbeds(messageEmbed).queue();
    }

}
