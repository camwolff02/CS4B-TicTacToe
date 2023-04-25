package Serialize.Messages;

import java.util.ArrayList;

import Serialize.ApplicationMessage;

public class ListOfGamesResponse extends ApplicationMessage {
    ArrayList<String> lobbyNames;
    
    public ListOfGamesResponse(ArrayList<String> lobbyNames) {
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

        return "List of Games: " + result + "]";
    }
}