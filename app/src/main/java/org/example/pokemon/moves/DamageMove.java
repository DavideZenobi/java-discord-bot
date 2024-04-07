package org.example.pokemon.moves;

import com.google.gson.JsonObject;
import org.example.pokemon.enums.Categories;
import org.example.pokemon.enums.Converter;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

public record DamageMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        Integer accuracy,
        int priority,
        /** Variables de este record */
        int power,
        int flinchChance

) implements MoveV2, Damage {

    public static MoveV2 from(JsonObject jsonObject) {
        return new DamageMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Converter.fromTypeStringToEnum(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Converter.fromCategoryStringToCategoryEnum(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                Converter.fromDamageClassesStringToEnum(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").isJsonNull() ? 100 : jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                jsonObject.get("power").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("flinch_chance").getAsInt()
        );
    }
}
