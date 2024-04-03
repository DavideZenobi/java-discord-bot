package org.example.pokemon;

import com.google.gson.JsonObject;

public class Move {
    
    private int id;
    private String name;
    private String type; // (grass, water, fire, etc)
    private String category; //damage, damage+effect, damage+aumentar tu stat, damage+reducir stat defensor
    private String damageClass; // (physical, special, status, -)
    private int power;
    private int accuracy;
    private String effectDescription;
    private int duration; // lo normal es 1, pero pueden ser mas turnos

    public Move(JsonObject jsonMove) {
        this.id = jsonMove.get("id").getAsInt();
        this.name = jsonMove.get("name").getAsString();
        this.type = jsonMove.get("type").getAsJsonObject().get("name").getAsString();
        this.damageClass = jsonMove.get("damage_class").getAsJsonObject().get("name").getAsString();
        this.power = jsonMove.get("power").getAsInt();
        this.accuracy = jsonMove.get("accuracy").getAsInt();
        this.effectDescription = jsonMove.get("effect_entries").getAsJsonArray().get(0).getAsJsonObject().get("effect").getAsString();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public String getDamageClass() {
        return damageClass;
    }

    public int getPower() {
        return power;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public String getEffectDescription() {
        return effectDescription;
    }
}