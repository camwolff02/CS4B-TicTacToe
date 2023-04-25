package Serialize.Messages;

import Serialize.ApplicationMessage;

public class StartGameRequest extends ApplicationMessage {
    private boolean status;
    private String lobbyName;
    
    public StartGameRequest(boolean status, String lobbyName) {
        this.status = status;
        this.lobbyName = lobbyName;
    }

    public boolean getStatus(){ return status;}

    public String getLobbyName(){return lobbyName;}
    

    public String toString() {
        return "Lobby name: " + lobbyName
            + "\n has" + status;
    }
}