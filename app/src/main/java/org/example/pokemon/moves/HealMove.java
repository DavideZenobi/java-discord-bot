package org.example.pokemon.moves;

import com.google.gson.JsonObject;
import org.example.pokemon.enums.Categories;
import org.example.pokemon.enums.Converter;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

// Cura al pokemon que realiza el movimiento
public record HealMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        Integer accuracy,
        int priority,
        /** Variables de este record */
        // El healing es % (suele ser 50% de max HP)
        int healing

) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        return new HealMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Converter.fromTypeStringToEnum(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Converter.fromCategoryStringToCategoryEnum(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                Converter.fromDamageClassesStringToEnum(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").isJsonNull() ? 100 : jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("healing").getAsInt()
        );
    }

}
