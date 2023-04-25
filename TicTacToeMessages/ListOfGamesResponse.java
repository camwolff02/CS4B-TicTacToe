package TicTacToeMessages;

import Router.Message;

import java.util.ArrayList;

public class ListOfGamesResponse extends Message {
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