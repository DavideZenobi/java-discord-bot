package org.example.pokemon.ailments;

import org.example.pokemon.enums.Ailments;

/**
 *  2-4 turnos. 50% posibilidades de golpearse a si mismo
 * */
public record Confusion (
        int minTurns,
        int maxTurns,
        int currentTurn
) implements Ailment {

}
