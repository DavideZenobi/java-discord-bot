package org.example.pokemon;

import java.util.List;

public class PokemonState {
    
    private String name;
    private List<String> types;
    private int baseHp; 
    private int currentHp;
    private int baseAttack;
    private int currentAttack;
    private int baseDefense;
    private int currentDefense;
    private int baseSpecialAttack;
    private int currentSpecialAttack;
    private int baseSpecialDefense;
    private int currentSpecialDefense;
    private int baseSpeed;
    private int currentSpeed;
    private List<Effect> effects;

    public PokemonState(Pokemon pokemon) {
        this.name = pokemon.getName();
        this.types = pokemon.getTypes();
        this.baseHp = pokemon.getHp();
        this.baseAttack = pokemon.getAttack();
        this.baseDefense = pokemon.getDefense();
        this.baseSpecialAttack = pokemon.getSpecialAttack();
        this.baseSpecialDefense = pokemon.getSpecialDefense();
        this.baseSpeed = pokemon.getSpeed();
    }
}
