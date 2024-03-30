package org.example.pokemon;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Pokemon {
    
    private int id;
    private String name;
    private List<String> types;
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private String spriteUrl;

    public Pokemon(int id, String name, List<String> types, int hp, int attack, int defense, int specialAttack, int specialDefense, int speed, String spriteUrl) {
        this.id = id;
        this.name = name;
        this.types = types;
        this.hp = hp;
        this.attack = attack;
        this.defense = defense;
        this.specialAttack = specialAttack;
        this.specialDefense = specialDefense;
        this.speed = speed;
        this.spriteUrl = spriteUrl;
    }

    public static Pokemon create(JsonObject jsonPokemon) {
        int id = jsonPokemon.get("id").getAsInt();
        String name = jsonPokemon.get("name").getAsString();
        int hp = jsonPokemon.get("stats").getAsJsonArray().get(0).getAsJsonObject().get("base_stat").getAsInt();
        int attack = jsonPokemon.get("stats").getAsJsonArray().get(1).getAsJsonObject().get("base_stat").getAsInt();
        int defense = jsonPokemon.get("stats").getAsJsonArray().get(2).getAsJsonObject().get("base_stat").getAsInt();
        int specialAttack = jsonPokemon.get("stats").getAsJsonArray().get(3).getAsJsonObject().get("base_stat").getAsInt();
        int specialDefense = jsonPokemon.get("stats").getAsJsonArray().get(4).getAsJsonObject().get("base_stat").getAsInt();
        int speed = jsonPokemon.get("stats").getAsJsonArray().get(5).getAsJsonObject().get("base_stat").getAsInt();
        String spriteUrl = jsonPokemon.get("sprites").getAsJsonObject().get("front_default").getAsString();
        // Esto se hace porque puede haber pokemons que tengan 1 o 2 tipos
        JsonArray typesArray = jsonPokemon.get("types").getAsJsonArray();
        List<String> types = new ArrayList<>();
        for (JsonElement typeElement : typesArray) {
            String typeName = typeElement.getAsJsonObject().get("type").getAsJsonObject().get("name").getAsString();
            types.add(typeName);
        }

        Pokemon pokemon = new Pokemon(id, name, types, hp, attack, defense, specialAttack, specialDefense, speed, spriteUrl);

        return pokemon;
    }

    @Override
    public String toString() {
        return "id: " + this.id + 
                " name: " + this.name + 
                " hp: " + this.hp + 
                " attack: " + this.attack + 
                " defense: " + this.defense + 
                " specialAttack: " + this.specialAttack + 
                " specialDefense: " + this.specialDefense + 
                " speed: " + this.speed;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getSpecialAttack() {
        return specialAttack;
    }

    public void setSpecialAttack(int specialAttack) {
        this.specialAttack = specialAttack;
    }

    public int getSpecialDefense() {
        return specialDefense;
    }

    public void setSpecialDefense(int specialDefense) {
        this.specialDefense = specialDefense;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public String getSpriteUrl() {
        return spriteUrl;
    }

    public void setSpriteUrl(String spriteUrl) {
        this.spriteUrl = spriteUrl;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    
}
