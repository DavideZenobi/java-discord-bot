package org.example.pokemon.ailments;

/**
 *  Infinitos turnos, 20% en cada turno de desaparecer. No puede hacer movimientos
 * */
public record Freeze (
        int minTurns,
        int maxTurns,
        int currentTurn
) implements Ailment {
}
