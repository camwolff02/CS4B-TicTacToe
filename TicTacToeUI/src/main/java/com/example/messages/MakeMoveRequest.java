package com.example.messages;

import com.example.*;

import javafx.scene.control.Button;

public class MakeMoveRequest extends Message {
    private String currentPlayer;
    private String lobbyName;
    //private Pair<Integer, Integer> gameMove;
    private String index;  // TODO: change to javafx.util.Pair

    public MakeMoveRequest(String lobbyName, String currentPlayer, String index) {
        this.currentPlayer = currentPlayer;
        this.lobbyName = lobbyName;
        this.index = index;
    }

    public String getCurrentPlayer(){ return currentPlayer;}
    
    public String getGameMove(){ return index; }

    public String getLobbyName(){return lobbyName;}

    public String toString() {
        return "Lobby name: " + lobbyName;
    }
}