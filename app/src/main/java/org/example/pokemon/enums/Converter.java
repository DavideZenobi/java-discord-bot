package org.example.pokemon.enums;

import org.example.pokemon.enums.Categories;

public class Converter {

    public static Categories fromCategoryStringToCategoryEnum(String categoryString) {
        return switch (categoryString) {
            case "ailment" -> Categories.AILMENT;
            case "damage+ailment" -> Categories.DAMAGE_AILMENT;
            case "damage+heal" -> Categories.DAMAGE_HEAL;
            case "damage+lower" -> Categories.DAMAGE_LOWER;
            case "damage" -> Categories.DAMAGE;
            case "damage+raise" -> Categories.DAMAGE_RAISE;
            case "heal" -> Categories.HEAL;
            case "net-good-stats" -> Categories.NET_GOOD_STATS;
            case "swagger" -> Categories.SWAGGER;
            default -> throw new IllegalArgumentException("Category not recognised: " + categoryString);
        };
    }

    public static Targets fromTargetStringToEnum(String targetString) {
        return switch (targetString) {
            case "user" -> Targets.USER;
            case "all-opponents" -> Targets.ALL_OPPONENTS;
            case "all-other-pokemon" -> Targets.ALL_OTHER_POKEMON;
            case "selected-pokemon" -> Targets.SELECTED_POKEMON;
            case "user-and-allies" -> Targets.USER_AND_ALLIES;
            case "all-pokemon" -> Targets.ALL_POKEMON;
            case "ally" -> Targets.ALLY;
            default -> throw new IllegalStateException("Unexpected value: " + targetString);
        };
    }

    public static Ailments fromAilmentStringToEnum(String ailmentString) {
        return switch (ailmentString) {
            case "unknown" -> Ailments.UNKNOWN;
            case "none" -> Ailments.NONE;
            case "paralysis" -> Ailments.PARALYSIS;
            case "sleep" -> Ailments.SLEEP;
            case "freeze" -> Ailments.FREEZE;
            case "burn" -> Ailments.BURN;
            case "poison" -> Ailments.POISON;
            case "confusion" -> Ailments.CONFUSION;
            case "infatuation" -> Ailments.INFATUATION;
            case "trap" -> Ailments.TRAP;
            case "nightmare" -> Ailments.NIGHTMARE;
            case "torment" -> Ailments.TORMENT;
            case "disable" -> Ailments.DISABLE;
            case "yawn" -> Ailments.YAWN;
            case "heal-block" -> Ailments.HEAL_BLOCK;
            case "no-type-immunity" -> Ailments.NO_TYPE_IMMUNITY;
            case "leech-seed" -> Ailments.LEECH_SEED;
            case "embargo" -> Ailments.EMBARGO;
            case "perish-song" -> Ailments.PERISH_SONG;
            case "ingrain" -> Ailments.INGRAIN;
            case "silence" -> Ailments.SILENCE;
            case "tar-shot" -> Ailments.TAR_SHOT;
            default -> throw new IllegalStateException("Unexpected value: " + ailmentString);
        };
    }

    public static Types fromTypeStringToEnum(String typeString) {
        return switch (typeString) {
            case "normal" -> Types.NORMAL;
            case "fire" -> Types.FIRE;
            case "water" -> Types.WATER;
            case "electric" -> Types.ELECTRIC;
            case "grass" -> Types.GRASS;
            case "ice" -> Types.ICE;
            case "fighting" -> Types.FIGHTING;
            case "poison" -> Types.POISON;
            case "ground" -> Types.GROUND;
            case "flying" -> Types.FLYING;
            case "psychic" -> Types.PSYCHIC;
            case "bug" -> Types.BUG;
            case "rock" -> Types.ROCK;
            case "ghost" -> Types.GHOST;
            case "dragon" -> Types.DRAGON;
            case "dark" -> Types.DARK;
            case "steel" -> Types.STEEL;
            case "fairy" -> Types.FAIRY;
            default -> throw new IllegalStateException("Unexpected value: " + typeString);
        };
    }

    public static DamageClasses fromDamageClassesStringToEnum(String damageClassesString) {
        return switch (damageClassesString) {
            case "physical" -> DamageClasses.PHYSICAL;
            case "special" -> DamageClasses.SPECIAL;
            case "status" -> DamageClasses.STATUS;
            default -> throw new IllegalStateException("Unexpected value: " + damageClassesString);
        };
    }

    public static Stats fromStatsStringToEnum(String statsString) {
        return switch (statsString) {
            case "hp" -> Stats.HP;
            case "attack" -> Stats.ATTACK;
            case "defense" -> Stats.DEFENSE;
            case "special-attack" -> Stats.SPECIAL_ATTACK;
            case "special-defense" -> Stats.SPECIAL_DEFENSE;
            case "speed" -> Stats.SPEED;
            case "accuracy" -> Stats.ACCURACY;
            case "evasion" -> Stats.EVASION;
            default -> throw new IllegalStateException("Unexpected value: " + statsString);
        };
    }
}
