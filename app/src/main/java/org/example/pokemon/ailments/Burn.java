package org.example.pokemon.ailments;

import org.example.pokemon.enums.Ailments;

public record Burn (
        int minTurns,
        int maxTurns,
        int currentTurn
) implements Ailment {

}