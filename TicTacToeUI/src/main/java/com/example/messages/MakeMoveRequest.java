package com.example.messages;

import com.example.router.Message;

public class MakeMoveRequest extends Message {
    private String currentPlayer;
    private String lobbyName;
    //private Pair<Integer, Integer> gameMove;
    private int gameMove;  // TODO: change to javafx.util.Pair

    public MakeMoveRequest(String id, String currentPlayer, int gameMove) {
        super(id);
        this.currentPlayer = currentPlayer;
        this.gameMove = gameMove;
    }

    public String getCurrentPlayer(){ return currentPlayer;}
    
    public int getGameMove(){ return gameMove; }

    public String getLobbyName(){return lobbyName;}

    public String toString() {
        return super.toString() + "Lobby name: " + lobbyName
            + "\nGame move: (" + gameMove + ")";
    }
}