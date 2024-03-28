package org.example;

import java.util.ArrayList;
import java.util.List;

import net.dv8tion.jda.api.EmbedBuilder;

public class EmbedImages {
    
    String user1ImageUrl;
    String user2ImageUrl;
    String pokemon1ImageUrl;
    String pokemon2ImageUrl;

    public EmbedImages(String user1ImageUrl, String user2ImageUrl, String pokemon1ImageUrl, String pokemon2ImageUrl) {
        this.user1ImageUrl = user1ImageUrl;
        this.user2ImageUrl = user2ImageUrl;
        this.pokemon1ImageUrl = pokemon1ImageUrl;
        this.pokemon2ImageUrl = pokemon2ImageUrl;
    }

    public List<EmbedBuilder> build() {
        List<EmbedBuilder> embedsList = new ArrayList<>();

        EmbedBuilder embed1 = new EmbedBuilder();
        embed1.setUrl("https://www.pokemon.com").setImage(user1ImageUrl);
        EmbedBuilder embed2 = new EmbedBuilder();
        embed2.setUrl("https://www.pokemon.com").setImage(user2ImageUrl);
        EmbedBuilder embed3 = new EmbedBuilder();
        embed3.setUrl("https://www.pokemon.com").setImage(pokemon1ImageUrl);
        EmbedBuilder embed4 = new EmbedBuilder();
        embed4.setUrl("https://www.pokemon.com").setImage(pokemon2ImageUrl);

        embedsList.add(embed1);
        embedsList.add(embed2);
        embedsList.add(embed3);
        embedsList.add(embed4);

        return embedsList;
    }

}
