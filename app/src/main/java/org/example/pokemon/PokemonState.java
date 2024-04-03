package org.example.pokemon;

import org.example.pokemon.tmp.MoveV2;

import java.util.ArrayList;
import java.util.List;

/**
 *  Accuracy y evasion son valores porcentuales (%)
 */
public class PokemonState {
    
    private String name;
    private List<String> types;
    private int baseHp; 
    private int currentHp;
    private int baseAttack;
    private int currentAttack;
    private int attackModifier; // from -6 to 6. start at 0
    private int baseDefense;
    private int currentDefense;
    private int defenseModifier;
    private int baseSpecialAttack;
    private int currentSpecialAttack;
    private int specialAttackModifier;
    private int baseSpecialDefense;
    private int currentSpecialDefense;
    private int specialDefenseModifier;
    private int baseSpeed;
    private int currentSpeed;
    private int speedModifier;
    private int baseAccuracy;
    private int currentAccuracy;
    private int accuracyModifier;
    private int baseEvasion;
    private int currentEvasion;
    private int evasionModifier;
    private int level;
    private List<Ailment> ailments;
    private MoveV2 move;

    public PokemonState(Pokemon pokemon) {
        this.name = pokemon.getName();
        this.types = pokemon.getTypes();
        this.baseHp = pokemon.getHp();
        this.currentHp = pokemon.getHp();
        this.baseAttack = pokemon.getAttack();
        this.currentAttack = pokemon.getAttack();
        this.baseDefense = pokemon.getDefense();
        this.currentDefense = pokemon.getDefense();
        this.baseSpecialAttack = pokemon.getSpecialAttack();
        this.currentSpecialAttack = pokemon.getSpecialAttack();
        this.baseSpecialDefense = pokemon.getSpecialDefense();
        this.currentSpecialDefense = pokemon.getSpecialDefense();
        this.baseSpeed = pokemon.getSpeed();
        this.currentSpeed = pokemon.getSpeed();
        this.attackModifier = 0;
        this.defenseModifier = 0;
        this.specialAttackModifier = 0;
        this.specialDefenseModifier = 0;
        this.speedModifier = 0;
        this.baseAccuracy = 100;
        this.accuracyModifier = 0;
        this.baseEvasion = 100;
        this.evasionModifier = 0;
        this.level = 1;
        this.ailments = new ArrayList<>();
    }

    public int getCurrentHp() {
        return currentHp;
    }

    public void setCurrentHp(int currentHp) {
        this.currentHp = currentHp;
    }

    public int getCurrentAttack() {
        return currentAttack;
    }

    public void setCurrentAttack(int currentAttack) {
        this.currentAttack = currentAttack;
    }

    public int getCurrentDefense() {
        return currentDefense;
    }

    public void setCurrentDefense(int currentDefense) {
        this.currentDefense = currentDefense;
    }

    public int getCurrentSpecialAttack() {
        return currentSpecialAttack;
    }

    public void setCurrentSpecialAttack(int currentSpecialAttack) {
        this.currentSpecialAttack = currentSpecialAttack;
    }

    public int getCurrentSpecialDefense() {
        return currentSpecialDefense;
    }

    public void setCurrentSpecialDefense(int currentSpecialDefense) {
        this.currentSpecialDefense = currentSpecialDefense;
    }

    public int getCurrentSpeed() {
        return currentSpeed;
    }

    public void setCurrentSpeed(int currentSpeed) {
        this.currentSpeed = currentSpeed;
    }

    public List<Ailment> getEffects() {
        return ailments;
    }

    public void setEffects(List<Ailment> ailments) {
        this.ailments = ailments;
    }

    public MoveV2 getMove() {
        return move;
    }

    public void setMove(MoveV2 move) {
        this.move = move;
    }

    public int getAttackModifier() {
        return attackModifier;
    }

    public void setAttackModifier(int attackModifier) {
        this.attackModifier = attackModifier;
    }

    public int getDefenseModifier() {
        return defenseModifier;
    }

    public void setDefenseModifier(int defenseModifier) {
        this.defenseModifier = defenseModifier;
    }

    public int getSpecialAttackModifier() {
        return specialAttackModifier;
    }

    public void setSpecialAttackModifier(int specialAttackModifier) {
        this.specialAttackModifier = specialAttackModifier;
    }

    public int getSpecialDefenseModifier() {
        return specialDefenseModifier;
    }

    public void setSpecialDefenseModifier(int specialDefenseModifier) {
        this.specialDefenseModifier = specialDefenseModifier;
    }

    public int getSpeedModifier() {
        return speedModifier;
    }

    public void setSpeedModifier(int speedModifier) {
        this.speedModifier = speedModifier;
    }

    public int getAccuracyModifier() {
        return accuracyModifier;
    }

    public void setAccuracyModifier(int accuracyModifier) {
        this.accuracyModifier = accuracyModifier;
    }

    public int getEvasionModifier() {
        return evasionModifier;
    }

    public void setEvasionModifier(int evasionModifier) {
        this.evasionModifier = evasionModifier;
    }    
    
}
