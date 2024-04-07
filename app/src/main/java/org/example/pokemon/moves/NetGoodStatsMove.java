package org.example.pokemon.moves;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.example.pokemon.enums.*;

import java.util.ArrayList;
import java.util.List;

// Aumento o reducción de stats del pokemon lanzador o enemigo.
public record NetGoodStatsMove(
        int id,
        String name,
        Types type,
        Categories category,
        DamageClasses damageClass,
        Integer accuracy,
        int priority,
        /** Variables de este record */
        List<Stats> statsNames,
        int statChange,
         Targets target
) implements MoveV2 {

    public static MoveV2 from(JsonObject jsonObject) {
        List<Stats> statsNames = new ArrayList<>();
        JsonArray jsonArray = jsonObject.get("stat_changes").getAsJsonArray();
        for (JsonElement element : jsonArray) {
            Stats stat = Converter.fromStatsStringToEnum(element.getAsJsonObject().get("stat").getAsJsonObject().get("name").getAsString());
            statsNames.add(stat);
        }

        return new NetGoodStatsMove(
                jsonObject.get("id").getAsInt(),
                jsonObject.get("name").getAsString(),
                Converter.fromTypeStringToEnum(jsonObject.get("type").getAsJsonObject().get("name").getAsString()),
                Converter.fromCategoryStringToCategoryEnum(jsonObject.get("meta").getAsJsonObject().get("category").getAsJsonObject().get("name").getAsString()),
                Converter.fromDamageClassesStringToEnum(jsonObject.get("damage_class").getAsJsonObject().get("name").getAsString()),
                jsonObject.get("accuracy").isJsonNull() ? 100 : jsonObject.get("accuracy").getAsInt(),
                jsonObject.get("priority").getAsInt(),
                statsNames,
                jsonObject.get("stat_changes").getAsJsonArray().get(0).getAsJsonObject().get("change").getAsInt(),
                Converter.fromTargetStringToEnum(jsonObject.get("target").getAsJsonObject().get("name").getAsString())
        );
    }

}
