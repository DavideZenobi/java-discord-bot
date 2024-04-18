package org.example.pokemon.ailments;

/**
 *  infinitos turnos. -50% speed. 25% de fallar
 * */
public record Paralysis (
        int minTurns,
        int maxTurns,
        int currentTurn
) implements Ailment {
}
