package org.example.battle;

import net.dv8tion.jda.api.entities.User;
import org.example.pokemon.enums.Stats;
import org.example.utils.EmojiConverter;
import org.example.utils.TextFormatter;

import java.util.*;

public class LoggerV2 {

    private final int MAX_ELEMENTS_TO_RETURN = 5;
    private List<String> logs;
    private Map<Integer, List<String>> logsV2;

    public LoggerV2() {
        this.logs = new ArrayList<>();
        this.logsV2 = new LinkedHashMap<>();
    }

    public void setNewBattle(String pokemonName1, String pokemonName2) {
        String log = "Battle between " + TextFormatter.convertToBold(pokemonName1) + " and " + TextFormatter.convertToBold(pokemonName2) + " is about to start!";
        this.logs.add(log);
    }

    /*public void saveNewTurn(int turnNumber) {
        String log = TextFormatter.convertToItalic("Turn number: " + turnNumber);
        this.logs.add(log);
    }*/

    public void saveUnableToMove(String pokemonName, String reason) {
        String log = TextFormatter.convertToBold(pokemonName.toUpperCase()) + " can't move because it's " + reason.toUpperCase();
        this.logs.add(log);
    }

    public void saveMoveWithoutDamage(String pokemonName, String moveName) {
        String log = TextFormatter.convertToBold(pokemonName) + " used " + moveName.toUpperCase();
        this.logs.add(log);
    }

    public void saveMissedMove(String pokemonName, String moveName) {
        String log = TextFormatter.convertToBold(pokemonName) + " used " + moveName.toUpperCase() + " but it missed";
        this.logs.add(log);
    }

    public void saveConfuseDamage(String pokemonName, int damage) {
        String log = TextFormatter.convertToBold(pokemonName) + " is confused! It hurt itself by " + damage + " hp :direct_hit:";
        this.logs.add(log);
    }

    public void saveDamageMoveApplied(String attackerName, String defenderName, String moveName, int damage) {
        String log = TextFormatter.convertToBold(attackerName) + " used " + moveName.toUpperCase() + " and did " + damage + " damage :direct_hit: to " + TextFormatter.convertToBold(defenderName);
        this.logs.add(log);
    }

    public void saveAilmentApplied(String pokemonName, String ailment) {
        String log = TextFormatter.convertToBold(pokemonName) + " is now under the effect of " + ailment.toUpperCase();
        this.logs.add(log);
    }

    public void saveRepeatedAilmentApplied(String pokemonName, String ailment) {
        String log = TextFormatter.convertToBold(pokemonName) + " is already " + ailment.toUpperCase();
        this.logs.add(log);
    }

    public void saveStatChangeMoveApplied(String pokemonName, String statName, boolean raised) {
        String log = statName.toUpperCase() + " of " + TextFormatter.convertToBold(pokemonName) + " has " + (raised ? "increased" : "decreased");
        this.logs.add(log);
    }

    public void saveAilmentDamageApplied(String pokemonName, String ailment, int damage) {
        String log = TextFormatter.convertToBold(pokemonName) + " is " + ailment + " and has lost " + damage + " hp";
        this.logs.add(log);
    }

    public void savePokemonFlinched(String pokemonName) {
        String log = TextFormatter.convertToBold(pokemonName) + " flinched!";
        this.logs.add(log);
    }

    public void saveHealingApplied(String pokemonName, int healing) {
        String log = TextFormatter.convertToBold(pokemonName) + " has healed " + healing + " hp :sparkling_heart:";
        this.logs.add(log);
    }

    public void saveAilmentRemoved(String pokemonName, String ailmentRemoved) {
        String log = TextFormatter.convertToBold(pokemonName) + " is no longer under the effects of " + ailmentRemoved;
        this.logs.add(log);
    }

    public void saveCriticalHit() {
        String log = "A critical hit!";
        this.logs.add(log);
    }

    public void saveSuperEffectiveMove() {
        String log = "It's super effective!";
        this.logs.add(log);
    }

    public void saveLowEffectiveMove() {
        String log = "It's not very effective...";
        this.logs.add(log);
    }

    public void savePokemonDied(String pokemonName) {
        String log = TextFormatter.convertToBold(pokemonName) + " is out of combat";
        this.logs.add(log);
    }

    public void savePokemonChange(User user, String newPokemonName) {
        String log = user.getName() + " sends " + TextFormatter.convertToBold(newPokemonName) + " to the battlefield";
        this.logs.add(log);
    }

    public void saveWinnerUser(User user) {
        String log = ":crown:" + TextFormatter.convertToBold(user.getName()) + " has won the battle";
        this.logs.add(log);
    }

    public List<String> getLastLogs() {
        if (this.logs.size() > MAX_ELEMENTS_TO_RETURN) {
            return this.logs.subList(this.logs.size() - MAX_ELEMENTS_TO_RETURN, this.logs.size());
        } else {
            return this.logs;
        }
    }

    /*public void showAllLogs() {
        for (int i = 0; i < this.logs.size(); i++) {
            System.out.println(i + ": " + this.logs.get(i));
        }
    }*/

    /******************************************************************************/

    public void setNewBattle(int currentTurn, String pokemonName1, String pokemonName2) {
        String log = "Battle between " + TextFormatter.convertToBold(pokemonName1) + " and " + TextFormatter.convertToBold(pokemonName2) + " is about to start!";
        this.logs.add(log);
    }

    public void saveNewTurn(int turnNumber) {
        this.logsV2.put(turnNumber, new LinkedList<>());
        String log = TextFormatter.convertToItalic("Turn number: " + turnNumber);
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveUnableToMove(int turnNumber, String pokemonName, String reason) {
        String log = TextFormatter.convertToBold(pokemonName.toUpperCase()) + " can't move because it's " + reason.toUpperCase();
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveMoveWithoutDamage(int turnNumber, String pokemonName, String moveName) {
        String log = TextFormatter.convertToBold(pokemonName) + " used " + moveName.toUpperCase();
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveMissedMove(int turnNumber, String pokemonName, String moveName) {
        String log = TextFormatter.convertToBold(pokemonName) + " used " + moveName.toUpperCase() + " but it missed :x:";
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveConfuseDamage(int turnNumber, String pokemonName, int damage) {
        String log = TextFormatter.convertToBold(pokemonName) + " is confused! It hurt itself by " + damage + " hp :direct_hit:";
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveDamageMoveApplied(int turnNumber, String attackerName, String defenderName, String moveName, int damage) {
        String log = TextFormatter.convertToBold(attackerName) + " used " + moveName.toUpperCase() + " and did " + damage + " damage :direct_hit: to " + TextFormatter.convertToBold(defenderName);
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveAilmentApplied(int turnNumber, String pokemonName, String ailment) {
        String log = TextFormatter.convertToBold(pokemonName) + " is now under the effect of " + ailment.toUpperCase();
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveRepeatedAilmentApplied(int turnNumber, String pokemonName, String ailment) {
        String log = TextFormatter.convertToBold(pokemonName) + " is already " + ailment.toUpperCase();
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveStatChangeMoveApplied(int turnNumber, String pokemonName, Stats statName, boolean raised) {
        String log = EmojiConverter.convertStat(statName) + " " + statName + " of " + TextFormatter.convertToBold(pokemonName) + " has " + (raised ? "increased :arrow_double_up:" : "decreased :arrow_double_down:");
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveAilmentDamageApplied(int turnNumber, String pokemonName, String ailment, int damage) {
        String log = TextFormatter.convertToBold(pokemonName) + " is " + ailment + " and has lost " + damage + " hp";
        this.logsV2.get(turnNumber).add(log);
    }

    public void savePokemonFlinched(int turnNumber, String pokemonName) {
        String log = TextFormatter.convertToBold(pokemonName) + " flinched!";
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveHealingApplied(int turnNumber, String pokemonName, int healing) {
        String log = TextFormatter.convertToBold(pokemonName) + " has healed " + healing + " hp :sparkling_heart:";
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveAilmentRemoved(int turnNumber, String pokemonName, String ailmentRemoved) {
        String log = TextFormatter.convertToBold(pokemonName) + " is no longer under the effects of " + ailmentRemoved;
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveCriticalHit(int turnNumber) {
        String log = "A critical hit!";
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveSuperEffectiveMove(int turnNumber) {
        String log = "It's super effective!";
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveLowEffectiveMove(int turnNumber) {
        String log = "It's not very effective...";
        this.logsV2.get(turnNumber).add(log);
    }

    public void savePokemonDied(int turnNumber, String pokemonName) {
        String log = TextFormatter.convertToBold(pokemonName) + " is out of combat";
        this.logsV2.get(turnNumber).add(log);
    }

    public void savePokemonChange(int turnNumber, User user, String newPokemonName) {
        String log = user.getName() + " sends " + TextFormatter.convertToBold(newPokemonName) + " to the battlefield";
        this.logsV2.get(turnNumber).add(log);
    }

    public void saveWinnerUser(int turnNumber, User user) {
        String log = ":crown:" + TextFormatter.convertToBold(user.getName()) + " has won the battle";
        this.logsV2.get(turnNumber).add(log);
    }

    public List<String> getLogsByTurn(int turnNumber) {
        return this.logsV2.get(turnNumber);
    }

    public void showAllLogs() {
        for (Map.Entry<Integer, List<String>> logsByTurn : this.logsV2.entrySet()) {
            for (String log : logsByTurn.getValue()) {
                System.out.println(log);
            }
        }
    }

}
