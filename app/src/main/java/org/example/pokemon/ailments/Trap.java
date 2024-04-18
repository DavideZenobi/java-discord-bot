package org.example.pokemon.ailments;

public record Trap(
        int minTurns,
        int maxTurns,
        int currentTurn
) implements Ailment {

}
