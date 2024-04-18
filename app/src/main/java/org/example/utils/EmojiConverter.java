package org.example.utils;

import org.example.pokemon.enums.Stats;
import org.example.pokemon.enums.Types;

public class EmojiConverter {

    public static String convertType(Types type) {
        return switch (type) {
            case NORMAL -> ":feet:";
            case FIRE -> ":fire:";
            case WATER -> ":bubbles:";
            case ELECTRIC -> ":zap:";
            case GRASS -> ":herb:";
            case ICE -> ":snowflake:";
            case FIGHTING -> ":boxing_glove:";
            case POISON -> ":microbe:";
            case GROUND -> ":desert:";
            case FLYING -> ":bird:";
            case PSYCHIC -> ":cyclone:";
            case BUG -> ":bug:";
            case ROCK -> ":rock:";
            case GHOST -> ":ghost:";
            case DRAGON -> ":dragon:";
            case DARK -> ":new_moon:";
            case STEEL -> ":wrench:";
            case FAIRY -> ":fairy:";
        };
    }

    public static String convertStat(Stats stat) {
        return switch (stat) {
            case HP -> null;
            case ATTACK -> ":crossed_swords:";
            case DEFENSE -> ":shield:";
            case SPECIAL_ATTACK -> ":magic_wand:";
            case SPECIAL_DEFENSE -> ":dna:";
            case SPEED -> ":dash:";
            case ACCURACY -> ":dart:";
            case EVASION -> ":cloud:";
        };
    }
}
