package org.example;

import com.google.gson.JsonObject;

public class Pokemon {
    
    private int id;
    private String name;
    private int hp;
    private int attack;
    private int defense;
    private int specialAttack;
    private int specialDefense;
    private int speed;
    private String spriteUrl;

    public Pokemon(int id, String name, int hp, int attack, int defense, int specialAttack, int specialDefense, int speed, String spriteUrl) {
        this.id = id;
        this.name = name;
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

        Pokemon pokemon = new Pokemon(id, name, hp, attack, defense, specialAttack, specialDefense, speed, spriteUrl);

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
}
