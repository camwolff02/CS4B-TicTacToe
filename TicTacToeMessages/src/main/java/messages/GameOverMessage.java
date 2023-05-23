package messages;

import router.Message;

public class GameOverMessage extends Message {
    private String gameState;

    public GameOverMessage(String id, String gameState) {
        super(id);
        this.gameState = gameState;
    }

}