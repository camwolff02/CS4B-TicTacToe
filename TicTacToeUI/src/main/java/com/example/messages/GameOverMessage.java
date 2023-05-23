package com.example.messages;

import com.example.router.Message;

public class GameOverMessage extends Message {
    private GameState gameState;

    public GameOverMessage(String id, GameState gameState) {
        super(id);
        this.gameState = gameState;
    }

    public String toString() {
        String s = super.toString();
        switch (gameState) {
            case WIN:
                return s + "Current Gamestate: WIN";
            case LOSE:
                return s + "Current Gamestate: LOSE";
            case TIE:
                return s + "Current Gamestate: TIE";
        }

        return s + "No current gamestate";
    }
}