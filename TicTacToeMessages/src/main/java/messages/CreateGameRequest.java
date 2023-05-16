package messages;

import router.Message;


public class CreateGameRequest extends Message {
    private String lobbyName;

    public CreateGameRequest(String id, String lobbyName) {
        super(id);
        this.lobbyName = lobbyName;
    }

    public String getLobbyName() { return lobbyName; }

    public String toString() {
        return super.toString() + "Lobby name: " + lobbyName;
    }
}