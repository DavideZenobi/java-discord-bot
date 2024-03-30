package org.example.battle;

import org.example.pokemon.Pokemon;

public class Turn {
    
    private Pokemon pokemon1;
    private Pokemon pokemon2;
    private int currentTurn;

    public Turn(Pokemon pokemon1, Pokemon pokemon2, int currentTurn) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.currentTurn = currentTurn;
    }

    private void whichPokemonAttackFirst() {
        if (this.pokemon1.getSpeed() > this.pokemon2.getSpeed()) {

        } else if (this.pokemon1.getSpeed() < this.pokemon2.getSpeed()) {

        } else {
            
        }
    }
}
