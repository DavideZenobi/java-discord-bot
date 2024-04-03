package org.example.battle;

import org.example.pokemon.PokemonState;

public class Turn {
    
    private PokemonState pokemon1;
    private PokemonState pokemon2;
    private int currentTurn;

    public Turn(PokemonState pokemon1, PokemonState pokemon2, int currentTurn) {
        this.pokemon1 = pokemon1;
        this.pokemon2 = pokemon2;
        this.currentTurn = currentTurn;
    }

    private void applyInitialEffects(PokemonState pokemon) {
        if (pokemon.getEffects().isEmpty()) {
            return;
        }

        // TODO
        /*switch (pokemon.getPokeMove()) {
            case DamageEffectPokeMove damageEffectPokeMove -> pokeMoveApplier.apply(damageEffectPokeMove, pokemon);
        }*/
    }

    private void applyMove(PokemonState attackingPokemon, PokemonState defendingPokemon) {

    }

    // Ejemplo: quemar, veneno
    private void applyFinalEffects(PokemonState pokemon) {
        if (pokemon.getEffects().isEmpty()) {
            return;
        }
    }

    private void checkPokemonHp(PokemonState pokemon) {
        if (pokemon.getCurrentHp() <= 0) {
            // Pokemon is dead. Inform that match is finished.
        }
    }

    private void reduceEffectTurns(PokemonState pokemon) {
        
    }
}
