package TicTacToeMessages;

import Router.Message;

public class JoinGameRequest extends Message {
    private String lobbyName;

    public JoinGameRequest(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() { return lobbyName; }

    public String toString() {
        return "Lobby name: " + lobbyName;
    }
}