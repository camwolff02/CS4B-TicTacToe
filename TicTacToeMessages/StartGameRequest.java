package TicTacToeMessages;

import Router.src.main.java.router.*;

public class StartGameRequest extends Message {
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