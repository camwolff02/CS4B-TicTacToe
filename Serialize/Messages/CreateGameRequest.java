package Serialize.Messages;

import Serialize.ApplicationMessage;

public class CreateGameRequest extends ApplicationMessage {
    private String lobbyName;

    public CreateGameRequest(String lobbyName) {
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() { return lobbyName; }

    public String toString() {
        return "Lobby name: " + lobbyName;
    }
}