package Serialize.Messages;

import Serialize.ApplicationMessage;

public class JoinGameRequest extends ApplicationMessage {
    private String lobbyName;

    public JoinGameRequest(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() { return lobbyName; }

    public String toString() {
        return "Lobby name: " + lobbyName;
    }
}