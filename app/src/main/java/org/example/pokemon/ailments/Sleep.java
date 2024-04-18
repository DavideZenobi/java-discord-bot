package org.example.pokemon.ailments;

/**
 *  1-3 turnos. No se puede mover
 * */
public record Sleep (
        int minTurns,
        int maxTurns,
        int currentTurn
) implements Ailment {
}
