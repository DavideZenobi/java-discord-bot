package org.example.pokemon;

import org.example.pokemon.enums.Types;

public class TypeEffectiveness {

    public static float getMultiplier(Types attackerType, Types defenderType) {
        return switch (attackerType) {
            case NORMAL -> switch (defenderType) {
                case NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, DRAGON, DARK, FAIRY -> 1f;
                case ROCK, STEEL -> 0.5f;
                case GHOST -> 0f;
            };

            case FIRE -> switch (defenderType) {
                case NORMAL, ELECTRIC, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, GHOST, DARK, FAIRY -> 1f;
                case FIRE, WATER, ROCK, DRAGON -> 0.5f;
                case GRASS, ICE, BUG, STEEL -> 2f;
            };

            case WATER -> switch (defenderType) {
                case NORMAL, ELECTRIC, ICE, FIGHTING, POISON, FLYING, PSYCHIC, BUG, GHOST, DARK, STEEL, FAIRY -> 1f;
                case FIRE, GROUND, ROCK -> 2f;
                case WATER, GRASS, DRAGON -> 0.5f;
            };

            case ELECTRIC -> switch (defenderType) {
                case NORMAL, FIRE, ICE, FIGHTING, POISON, PSYCHIC, BUG, ROCK, GHOST, DARK, STEEL, FAIRY -> 1f;
                case WATER, FLYING -> 2f;
                case ELECTRIC, GRASS, DRAGON -> 0.5f;
                case GROUND -> 0f;
            };

            case GRASS -> switch (defenderType) {
                case NORMAL, ELECTRIC, ICE, FIGHTING, PSYCHIC, GHOST, DARK, FAIRY -> 1f;
                case FIRE, GRASS, POISON, FLYING, BUG, DRAGON, STEEL -> 0.5f;
                case WATER, GROUND, ROCK -> 2f;
            };

            case ICE -> switch (defenderType) {
                case NORMAL, ELECTRIC, FIGHTING, POISON, PSYCHIC, BUG, ROCK, GHOST, DARK, FAIRY -> 1f;
                case FIRE, WATER, ICE, STEEL -> 0.5f;
                case GRASS, GROUND, FLYING, DRAGON -> 2f;
            };

            case FIGHTING -> switch (defenderType) {
                case NORMAL, ICE, ROCK, DARK, STEEL -> 2f;
                case FIRE, WATER, ELECTRIC, GRASS, FIGHTING, GROUND, DRAGON -> 1f;
                case POISON, FLYING, PSYCHIC, BUG, FAIRY -> 0.5f;
                case GHOST -> 0f;
            };

            case POISON -> switch (defenderType) {
                case NORMAL, FIRE, WATER, ELECTRIC, ICE, FIGHTING, FLYING, PSYCHIC, BUG, DRAGON, DARK -> 1f;
                case GRASS, FAIRY -> 2f;
                case POISON, GROUND, ROCK, GHOST -> 0.5f;
                case STEEL -> 0f;
            };

            case GROUND -> switch (defenderType) {
                case NORMAL, WATER, ICE, FIGHTING, GROUND, PSYCHIC, GHOST, DRAGON, DARK, FAIRY -> 1f;
                case FIRE, ELECTRIC, POISON, ROCK, STEEL -> 2f;
                case GRASS, BUG -> 0.5f;
                case FLYING -> 0f;
            };

            case FLYING -> switch (defenderType) {
                case NORMAL, FIRE, WATER, ICE, POISON, GROUND, FLYING, PSYCHIC, GHOST, DRAGON, DARK, FAIRY -> 1f;
                case ELECTRIC, ROCK, STEEL -> 0.5f;
                case GRASS, FIGHTING, BUG -> 2f;
            };

            case PSYCHIC -> switch (defenderType) {
                case NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, GROUND, FLYING, BUG, ROCK, GHOST, DRAGON, FAIRY -> 1f;
                case FIGHTING, POISON -> 2f;
                case PSYCHIC, STEEL -> 0.5f;
                case DARK -> 0f;
            };

            case BUG -> switch (defenderType) {
                case NORMAL, WATER, ELECTRIC, ICE, GROUND, BUG, ROCK, DRAGON -> 1f;
                case FIRE, FIGHTING, POISON, FLYING, GHOST, STEEL, FAIRY -> 0.5f;
                case GRASS, PSYCHIC, DARK -> 2f;
            };

            case ROCK -> switch (defenderType) {
                case NORMAL, WATER, ELECTRIC, GRASS, POISON, PSYCHIC, ROCK, GHOST, DRAGON, DARK, FAIRY -> 1f;
                case FIRE, ICE, FLYING, BUG -> 2f;
                case FIGHTING, GROUND, STEEL -> 0.5f;
            };

            case GHOST -> switch (defenderType) {
                case NORMAL -> 0f;
                case FIRE, WATER, ELECTRIC, GRASS, ICE, FIGHTING, POISON, GROUND, FLYING, BUG, ROCK, DRAGON, STEEL, FAIRY -> 1f;
                case PSYCHIC, GHOST -> 2f;
                case DARK -> 0.5f;
            };

            case DRAGON -> switch (defenderType) {
                case NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, DARK -> 1f;
                case DRAGON -> 2f;
                case STEEL -> 0.5f;
                case FAIRY -> 0f;
            };

            case DARK -> switch (defenderType) {
                case NORMAL, FIRE, WATER, ELECTRIC, GRASS, ICE, POISON, GROUND, FLYING, BUG, ROCK, DRAGON, STEEL -> 1f;
                case FIGHTING, DARK, FAIRY -> 0.5f;
                case PSYCHIC, GHOST -> 2f;
            };

            case STEEL -> switch (defenderType) {
                case NORMAL, GRASS, FIGHTING, POISON, GROUND, FLYING, PSYCHIC, BUG, GHOST, DARK, FAIRY -> 1f;
                case FIRE, WATER, ELECTRIC, DRAGON, STEEL -> 0.5f;
                case ICE, ROCK -> 2f;
            };

            case FAIRY -> switch (defenderType) {
                case NORMAL, WATER, ELECTRIC, GRASS, ICE, GROUND, FLYING, PSYCHIC, BUG, ROCK, GHOST, FAIRY -> 1f;
                case FIRE, POISON, STEEL -> 0.5f;
                case FIGHTING, DRAGON, DARK -> 2f;
            };
        };
    }
}
