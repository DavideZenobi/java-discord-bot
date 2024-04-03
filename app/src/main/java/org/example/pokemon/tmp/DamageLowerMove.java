package org.example.pokemon.tmp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.pokemon.enums.Categories;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

import java.util.ArrayList;
import java.util.List;

// Error de la api al nombrarlo de esta manera.
// Sería daño + reducción/aumento de una o más stats del pokemon defensor.

/**
 *  Cabe la posibilidad de que altere más de 1 stat, por eso es una lista.
 *  La cantidad del cambio siempre es la misma, por eso se coge el dato del primer elemento de la array.
 *  En caso de cambiar esto, la lista debería cambiar a Map, guardando nombre y cantidad del cambio.
 */
public record DamageLowerMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        int accuracy,
        int priority,
        /** Variables de este record */
        int power,
        List<String> statsNames,
        int statChange,
        int statChance,
        int flinchChance
) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        List<String> statsNames = new ArrayList<>();
        JsonArray jsonArray = jsonObject.get("stat_changes").getAsJsonArray();
        for (JsonElement element : jsonArray) {
            String statName = element.getAsJsonObject().get("stat").getAsJsonObject().get("name").getAsString();
            statsNames.add(statName);
        }

        return new DamageLowerMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Types.valueOf(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Categories.valueOf(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                DamageClasses.valueOf(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                jsonObject.get("power").getAsInt(),
                statsNames,
                jsonObject.get("stat_changes").getAsJsonArray().get(0).getAsJsonObject().get("change").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("stat_chance").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("flinch_chance").getAsInt()
        );
    }

}
