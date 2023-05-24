package com.example.messages;

import com.example.router.Message;

public class MakeMoveRequest extends Message {
    
    private String lobbyName;
    //private Pair<Integer, Integer> gameMove;
    private int gameMove;  // TODO: change to javafx.util.Pair

    public MakeMoveRequest(String id,  int gameMove) {
        super(id);
        
        this.gameMove = gameMove;
    }
    
    public int getGameMove(){ return gameMove; }

    public String getLobbyName(){return lobbyName;}

    public String toString() {
        return super.toString() + "Lobby name: " + lobbyName
            + "\nGame move: (" + gameMove + ")";
    }
}