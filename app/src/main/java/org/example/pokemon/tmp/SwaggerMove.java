package org.example.pokemon.tmp;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.pokemon.enums.Categories;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

import java.util.ArrayList;
import java.util.List;

// Aumento o reduccion de una stat + ailment
public record SwaggerMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        int accuracy,
        int priority,
        /** Variables de este record */
        List<String> statsNames,
        int statChange,
        int minTurns,
        int maxTurns
) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        List<String> statsNames = new ArrayList<>();
        JsonArray jsonArray = jsonObject.get("stat_changes").getAsJsonArray();
        for (JsonElement element : jsonArray) {
            String statName = element.getAsJsonObject().get("stat").getAsJsonObject().get("name").getAsString();
            statsNames.add(statName);
        }

        return new SwaggerMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Types.valueOf(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Categories.valueOf(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                DamageClasses.valueOf(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                statsNames,
                jsonObject.get("stat_changes").getAsJsonArray().get(0).getAsJsonObject().get("change").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("min_turns").getAsInt(),
                jsonObject.get("meta").getAsJsonObject().get("max_turns").getAsInt()
        );
    }

}
