package messages;

import router.Message;

public class MakeMoveRequest extends Message {
    private String currentPlayer;
    private String lobbyName;
    //private Pair<Integer, Integer> gameMove;
    private int[] gameMove;  // TODO: change to javafx.util.Pair

    public MakeMoveRequest(String lobbyName, String currentPlayer, int[] gameMove) {
        this.currentPlayer = currentPlayer;
        this.lobbyName = lobbyName;
        this.gameMove = gameMove;
    }

    public String getCurrentPlayer(){ return currentPlayer;}
    
    public int[] getGameMove(){ return gameMove; }

    public String getLobbyName(){return lobbyName;}

    public String toString() {
        return "Lobby name: " + lobbyName
            + "\nGame move: (" + gameMove[0] + ", " + gameMove[1] + ")";
    }
}