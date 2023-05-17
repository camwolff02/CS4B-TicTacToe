package com.example.messages;

import java.util.ArrayList;

import com.example.router.Message;

public class ListOfGamesResponse extends Message {
    ArrayList<String> lobbyNames;
    
    public ListOfGamesResponse(String id, ArrayList<String> lobbyNames) {
        super(id);
        this.lobbyNames = lobbyNames;
    }

    public ArrayList<String> getLobbyNames() {
        return lobbyNames;
    }

    public String toString() {
        String result = "[";

        for (String s : lobbyNames) {
            result += s + ", ";
        }

        return super.toString() + "List of Games: " + result + "]";
    }
}