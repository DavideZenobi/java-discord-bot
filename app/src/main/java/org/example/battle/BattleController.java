package org.example.battle;

import org.example.pokemon.Move;
import org.example.pokemon.Pokemon;

public class BattleController {
    
    private final int MAX_TURNS = 60;

    private int currentTurn = 1;
    private String gameStatus; // active or finished

    public BattleController() {
        this.gameStatus = "active";
    }

    private void startBattle() {
        while (this.gameStatus == "active") {
            



            addTurn();
        }
    }

    private void addTurn() {
        this.currentTurn++;
        if (currentTurn > MAX_TURNS) {
            this.gameStatus = "finished";
        }
    }
}
