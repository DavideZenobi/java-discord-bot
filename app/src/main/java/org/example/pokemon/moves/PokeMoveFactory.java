package org.example.pokemon.moves;

import com.google.gson.JsonObject;
import org.example.pokemon.enums.Converter;
import org.example.pokemon.moves.*;
import org.example.pokemon.enums.Categories;

public class PokeMoveFactory {

    public static MoveV2 createPokeMove(JsonObject jsonObject) {
        System.out.println("Move ID: " + jsonObject.get("id").getAsInt());
        Categories moveCategory = Converter.fromCategoryStringToCategoryEnum(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString());
        // meta(object) -> category(object) -> name(string)
        return switch (moveCategory) {
            case AILMENT -> AilmentMove.from(jsonObject);
            case DAMAGE_AILMENT -> DamageAilmentMove.from(jsonObject);
            case DAMAGE_HEAL -> DamageHealMove.from(jsonObject);
            case DAMAGE_LOWER -> DamageLowerMove.from(jsonObject);
            case DAMAGE -> DamageMove.from(jsonObject);
            case DAMAGE_RAISE -> DamageRaiseMove.from(jsonObject);
            case HEAL -> HealMove.from(jsonObject);
            case NET_GOOD_STATS -> NetGoodStatsMove.from(jsonObject);
            case SWAGGER -> SwaggerMove.from(jsonObject);
        };
    }

}
