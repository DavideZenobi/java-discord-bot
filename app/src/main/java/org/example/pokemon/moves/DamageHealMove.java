package org.example.pokemon.moves;

import com.google.gson.JsonObject;
import org.example.pokemon.enums.Categories;
import org.example.pokemon.enums.Converter;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

// Daño + curación del pokemon que lanza el ataque.
public record DamageHealMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        Integer accuracy,
        int priority,
        /** Variables de este record */
        int power,
        // El drain/healing es % del daño infligido
        int drain,
        int flinchChance,
        int minHits,
        int maxHits
) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        return new DamageHealMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Converter.fromTypeStringToEnum(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Converter.fromCategoryStringToCategoryEnum(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                Converter.fromDamageClassesStringToEnum(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").isJsonNull() ? null : jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                jsonObject.get("power").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("drain").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("flinch_chance").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("min_hits").isJsonNull() ? 1 : jsonObject.get("meta").getAsJsonObject().get("min_hits").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("max_hits").isJsonNull() ? 1 : jsonObject.get("meta").getAsJsonObject().get("max_hits").getAsInt()
        );
    }

}
