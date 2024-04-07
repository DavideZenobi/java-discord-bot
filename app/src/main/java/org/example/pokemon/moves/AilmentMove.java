package org.example.pokemon.moves;

import com.google.gson.JsonObject;
import org.example.pokemon.enums.*;

/** Efecto de estado (quemar, veneno, confusión, etc.) */
public record AilmentMove(
        int id,
        String name,
        Ailments ailment,
        Types type,
        Categories category,
        DamageClasses damageClass,
        Integer accuracy,
        int priority,
        /** Variables de este record */
        Integer minTurns,
        Integer maxTurns
) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        return new AilmentMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Converter.fromAilmentStringToEnum(jsonObject.get("meta").getAsJsonObject().get("ailment").getAsJsonObject().get("name").getAsString()),
                Converter.fromTypeStringToEnum(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Converter.fromCategoryStringToCategoryEnum(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                Converter.fromDamageClassesStringToEnum(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").isJsonNull() ? null : jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("min_turns").isJsonNull() ? null : jsonObject.get("meta").getAsJsonObject().get("min_turns").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("max_turns").isJsonNull() ? null : jsonObject.get("meta").getAsJsonObject().get("max_turns").getAsInt()
        );
    }

}
