package TicTacToeMessages;

import Router.Message;


public class CreateGameRequest extends Message {
    private String lobbyName;

    public CreateGameRequest(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() { return lobbyName; }

    public String toString() {
        return "Lobby name: " + lobbyName;
    }
}