package org.example.pokemon.moves;

import com.google.gson.JsonObject;
import org.example.pokemon.enums.*;

// Daño + efecto de estado (quemar, veneno, etc)
public record DamageAilmentMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        Integer accuracy,
        int priority,
        /** Variables de este record */
        int power,
        Ailments ailment,
        int ailmentAccuracy,
        Integer minTurns,
        Integer maxTurns,
        int flinchChance
) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        return new DamageAilmentMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Converter.fromTypeStringToEnum(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Converter.fromCategoryStringToCategoryEnum(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                Converter.fromDamageClassesStringToEnum(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").isJsonNull() ? null : jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                jsonObject.get("power").getAsInt(),
                Converter.fromAilmentStringToEnum(jsonObject.get("meta").getAsJsonObject().get("ailment").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("meta").getAsJsonObject().get("ailment_chance").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("min_turns").isJsonNull() ? null : jsonObject.get("meta").getAsJsonObject().get("min_turns").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("max_turns").isJsonNull() ? null : jsonObject.get("meta").getAsJsonObject().get("max_turns").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("flinch_chance").getAsInt()
        );
    }

}
