package org.example.battle;

import java.util.*;

public class Logger {

    public static Map<UUID, List<String>> logs = new HashMap<>();

    // Called only once at beginning of battle
    /** Battle between Pikachu and Bulbasur is about to start! */
    public static void setNewBattle(UUID uuid, String pokemonName1, String pokemonName2) {
        logs.put(uuid, new ArrayList<>());
        String log = "Battle between " + pokemonName1.toUpperCase() + " and " + pokemonName2.toUpperCase() + " is about to start!";
        logs.get(uuid).add(log);
    }

    /** Turn number: 1 */
    public static void saveNewTurn(UUID uuid, int turnNumber) {
        String log = "Turn number: " + turnNumber;
        logs.get(uuid).add(log);
    }

    /** Pikachu can't move because it's frozen */
    public static void saveUnableToMove(UUID uuid, String pokemonName, String reason) {
        String log = pokemonName.toUpperCase() + " can't move because it's " + reason.toUpperCase();
        logs.get(uuid).add(log);
    }

    /** Pikachu used thunder */
    public static void saveMoveWithoutDamage(UUID uuid, String pokemonName, String moveName) {
        String log = pokemonName.toUpperCase() + " used " + moveName.toUpperCase();
        logs.get(uuid).add(log);
    }

    /** Pikachu used thunder but it missed */
    public static void saveMissedMove(UUID uuid, String pokemonName, String moveName) {
        String log = pokemonName.toUpperCase() + " used " + moveName.toUpperCase() + " but it missed";
        logs.get(uuid).add(log);
    }

    /** Pikachu is confused! It hurt itself by 1 hp */
    public static void saveConfuseDamage(UUID uuid, String pokemonName, int damage) {
        String log = pokemonName.toUpperCase() + " is confused! It hurt itself by " + damage + " hp";
        logs.get(uuid).add(log);
    }

    /** Pikachu used thunder and did 1 damage to Bulbasur */
    public static void saveDamageMoveApplied(UUID uuid, String attackerName, String defenderName, String moveName, int damage) {
        String log = attackerName.toUpperCase() + " used " + moveName.toUpperCase() + " and did " + damage + " damage to " + defenderName.toUpperCase();
        logs.get(uuid).add(log);
    }

    /** Pikachu is now under the effect of freeze */
    public static void saveAilmentApplied(UUID uuid, String pokemonName, String ailment) {
        String log = pokemonName.toUpperCase() + " is now under the effects of " + ailment.toUpperCase();
        logs.get(uuid).add(log);
    }

    /** Pikachu is already frozen */
    public static void saveRepeatedAilmentApplied(UUID uuid, String pokemonName, String ailment) {
        String log = pokemonName.toUpperCase() + " is already " + ailment.toUpperCase();
        logs.get(uuid).add(log);
    }

    /** Attack of Pikachu has increased */
    public static void saveStatChangeMoveApplied(UUID uuid, String pokemonName, String statName, boolean raised) {
        String log = statName.toUpperCase() + " of " + pokemonName.toUpperCase() + " has " + (raised ? "increased" : "decreased");
        logs.get(uuid).add(log);
    }

    /** Pikachu is burned and has lost 1 hp */
    public static void saveAilmentDamageApplied(UUID uuid, String pokemonName, String ailment, int damage) {
        String log = pokemonName.toUpperCase() + " is " + ailment + " and has lost " + damage + " hp";
        logs.get(uuid).add(log);
    }

    /** Pikachu flinched! */
    public static void savePokemonFlinched(UUID uuid, String pokemonName) {
        String log = pokemonName.toUpperCase() + " flinched!";
        logs.get(uuid).add(log);
    }

    /** Pikachu has healed 1 hp */
    public static void saveHealingApplied(UUID uuid, String pokemonName, int healing) {
        String log = pokemonName.toUpperCase() + " has healed " + healing + " hp";
        logs.get(uuid).add(log);
    }

    /** Pikachu is no longer under the effects of freeze */
    public static void saveAilmentRemoved(UUID uuid, String pokemonName, String ailmentRemoved) {
        String log = pokemonName.toUpperCase() + " is no longer under the effects of " + ailmentRemoved;
        logs.get(uuid).add(log);
    }

    /** A critical hit! */
    public static void saveCriticalHit(UUID uuid) {
        String log = "A critical hit!";
        logs.get(uuid).add(log);
    }

    /** Pikachu is out of combat */
    public static void savePokemonDied(UUID uuid, String pokemonName) {
        String log = pokemonName.toUpperCase() + " is out of combat";
        logs.get(uuid).add(log);
    }

    public static void savePokemonCurrentHp(UUID uuid, String pokemonName1, String pokemonName2, int pokemonHp1, int pokemonHp2) {
        String log = pokemonName1 + ": " + pokemonHp1 + " HP || " + pokemonName2 + ": " + pokemonHp2;
        logs.get(uuid).add(log);
    }

    public static void showAllLogs(UUID uuid) {
        List<String> allLogs = logs.get(uuid);
        System.out.println("Battle ID: " + uuid);
        for (int i = 0; i < allLogs.size(); i++) {
            System.out.println(i + ": " + allLogs.get(i));
        }
    }
    
}
