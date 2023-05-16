package messages;

import router.Message;

public class StartGameRequest extends Message {
    private boolean status;
    private String lobbyName;
    
    public StartGameRequest(String id, boolean status, String lobbyName) {
        super(id);
        this.status = status;
        this.lobbyName = lobbyName;
    }

    public boolean getStatus(){ return status;}

    public String getLobbyName(){return lobbyName;}
    

    public String toString() {
        return super.toString() + "Lobby name: " + lobbyName
            + "\n has" + status;
    }
}