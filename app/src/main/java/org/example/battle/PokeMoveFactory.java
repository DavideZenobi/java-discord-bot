package org.example.battle;

import com.google.gson.JsonObject;
import org.example.pokemon.tmp.*;
import org.example.pokemon.enums.Categories;

public class PokeMoveFactory {

    public static MoveV2 createPokeMove(JsonObject jsonObject) {
        Categories moveCategory = Categories.valueOf(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString());
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
