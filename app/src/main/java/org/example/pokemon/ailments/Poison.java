package org.example.pokemon.ailments;

public record Poison (
        int minTurns,
        int maxTurns,
        int currentTurn
) implements Ailment {
}
