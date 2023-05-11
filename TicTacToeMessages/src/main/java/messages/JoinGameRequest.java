package messages;

import router.Message;

public class JoinGameRequest extends Message {
    private String lobbyName;

    public JoinGameRequest(String id, String lobbyName) {
        super(id);
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() { return lobbyName; }

    public String toString() {
        return super.toString() + "Lobby name: " + lobbyName;
    }
}