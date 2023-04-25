package Serialize.Messages;

import Serialize.ApplicationMessage;

public class GameOverMessage extends ApplicationMessage {
    private GameState gameState;

    public GameOverMessage(GameState gameState) {
        this.gameState = gameState;
    }

    public String toString() {
        switch (gameState) {
            case WIN:
                return "Current Gamestate: WIN";
            case LOSE:
                return "Current Gamestate: LOSE";
            case TIE:
                return "Current Gamestate: TIE";
        }

        return "No current gamestate";
    }
}