package org.example.pokemon.tmp;

import com.google.gson.JsonObject;
import org.example.pokemon.enums.Ailments;
import org.example.pokemon.enums.Categories;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

// Daño + efecto de estado (quemar, veneno, etc)
public record DamageAilmentMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        int accuracy,
        int priority,
        /** Variables de este record */
        int power,
        Ailments ailment,
        int ailmentAccuracy,
        int minTurns,
        int maxTurns,
        int flinchChance
) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        return new DamageAilmentMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Types.valueOf(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Categories.valueOf(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                DamageClasses.valueOf(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                jsonObject.get("power").getAsInt(),
                Ailments.valueOf(jsonObject.get("meta").getAsJsonObject().get("ailment").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("meta").getAsJsonObject().get("ailment_chance").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("min_turns").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("max_turns").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("flinch_chance").getAsInt()
        );
    }

}
