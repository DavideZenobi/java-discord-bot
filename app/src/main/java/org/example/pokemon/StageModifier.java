package org.example.pokemon;

/**
 * Todos los valores que se devuelven aquí son porcentajes
 * */
public class StageModifier {

    public static int basicStatsStages(int modifier) {
        return switch (modifier) {
            case 0 -> 100;
            case 1 -> 150;
            case 2 -> 200;
            case 3 -> 250;
            case 4 -> 300;
            case 5 -> 350;
            case 6 -> 400;
            case -1 -> 66;
            case -2 -> 50;
            case -3 -> 40;
            case -4 -> 33;
            case -5 -> 28;
            case -6 -> 25;
            default -> throw new IllegalStateException("Unexpected value: " + modifier);
        };
    }

    public static int accuracyStages(int modifier) {
        return switch (modifier) {
            case 0 -> 100;
            case 1 -> 133;
            case 2 -> 166;
            case 3 -> 200;
            case 4 -> 250;
            case 5 -> 266;
            case 6 -> 300;
            case -1 -> 75;
            case -2 -> 60;
            case -3 -> 50;
            case -4 -> 43;
            case -5 -> 36;
            case -6 -> 33;
            default -> throw new IllegalStateException("Unexpected value: " + modifier);
        };
    }

    public static int evasionStages(int modifier) {
        return switch (modifier) {
            case 0 -> 0;
            case 1 -> 14;
            case 2 -> 28;
            case 3 -> 42;
            case 4 -> 56;
            case 5 -> 70;
            case 6 -> 84;
            case -1 -> -14;
            case -2 -> -28;
            case -3 -> -42;
            case -4 -> -56;
            case -5 -> -70;
            case -6 -> -84;
            default -> throw new IllegalStateException("Unexpected value: " + modifier);
        };
    }

    public static float critStages(int modifier) {
        return switch (modifier) {
            case 0 -> 4.17f;
            case 1 -> 12.5f;
            case 2 -> 50f;
            case 3 -> 100f;
            default -> throw new IllegalStateException("Unexpected value: " + modifier);
        };
    }
}
