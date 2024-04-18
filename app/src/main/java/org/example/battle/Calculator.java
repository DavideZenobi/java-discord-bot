package org.example.battle;

import org.example.pokemon.TypeEffectiveness;
import org.example.pokemon.enums.DamageClasses;
import org.example.pokemon.enums.Types;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Calculator {

    public record DamageFormulaData(
            int level,
            int movePower,
            Types moveType,
            DamageClasses damageClass,
            int attackerPower,
            List<Types> attackerType,
            int defenderDefense,
            List<Types> defenderType,
            boolean STAB,
            boolean crit,
            boolean burned
    ) { }

    public record HealFormulaData() { }

    public static int calculateHealingBasedOnMaxHp(int healingPercentage, int maxHp) {
        return healingPercentage * maxHp / 100;
    }

    public static int calculateHealingBasedOnDamage(int healingPercentage, int damageDone) {
        int healing = healingPercentage * damageDone / 100;
        if (healing == 0) {
            return 1;
        } else {
            return healing;
        }
    }

    public static int calculateDamage(DamageFormulaData formula) {
        float targetMultiplier = 1f;
        float PBMultiplier = 1f;
        float weatherMultiplier = 1f;
        float glaiveRushMultiplier = 1f;
        float critMultiplier = 1f;
        float stabMultiplier = 1f;
        float typeEffectivenessMultiplier = 1f;
        float burnMultiplier = 1f;
        float otherMultiplier = 1f;

        if (formula.crit) {
            critMultiplier = 1.5f;
        }

        Random random = new Random();
        float randomMultiplier = (float) (random.nextInt(16) + 85) / 100;

        if (formula.STAB()) {
            stabMultiplier = 1.5f;
        }

        List<Float> multipliers = new ArrayList<>();
        for (Types defenderType : formula.defenderType) {
            float multiplier = TypeEffectiveness.getMultiplier(formula.moveType, defenderType);
            multipliers.add(multiplier);
        }
        float result = 1f;
        for (float multiplier: multipliers) {
            result *= multiplier;
        }
        typeEffectivenessMultiplier = result;

        if (formula.burned() && formula.damageClass().equals(DamageClasses.PHYSICAL)) {
            burnMultiplier = 0.5f;
        }

        int damage = (int) (((((2 * formula.level() / 5 + 2) * formula.movePower() * formula.attackerPower() / formula.defenderDefense()) / 50) + 2) *
                targetMultiplier * PBMultiplier * weatherMultiplier * glaiveRushMultiplier * critMultiplier *
                randomMultiplier * stabMultiplier * typeEffectivenessMultiplier * burnMultiplier * otherMultiplier);

        if (damage == 0) {
            return 1;
        } else {
            return damage;
        }
    }

    public static int calculatePercentageDamage(float percentage, int maxHp) {
        return (int) percentage * maxHp / 100;
    }

    public static float calculateProbabilityToHit(int moveAccuracy, float attackerAccuracy, float defenderEvasion) {
        return moveAccuracy * (attackerAccuracy / 100) * (1 - (defenderEvasion / 100));
    }

    public static float calculateProbabilityToHitWithoutEvasion(int moveAccuracy, float attackerAccuracy) {
        return moveAccuracy * (attackerAccuracy / 100);
    }

    public static boolean applyProbability(int probability) {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        return randomNumber < probability;
    }

    public static boolean applyDecimalProbability(float probability) {
        Random random = new Random();
        int randomNumber = random.nextInt(10000);
        return randomNumber < probability * 100;
    }
}


/*
 * 
 */