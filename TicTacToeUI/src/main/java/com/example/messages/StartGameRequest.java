package com.example.messages;

import com.example.router.Message;

public class StartGameRequest extends Message {
    private boolean status;
    private String lobbyName;
    
    public StartGameRequest(String id, boolean status) {
        super(id);
        this.status = status;
    }

    public boolean getStatus(){ return status;}

    public String getLobbyName(){return lobbyName;}
    

    public String toString() {
        return super.toString()
            + "\n has" + status;
    }
}