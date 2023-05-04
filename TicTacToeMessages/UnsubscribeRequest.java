package TicTacToeMessages;

import Router.src.main.java.router.*;

public class UnsubscribeRequest extends Message {
    private String channel;
    
    public UnsubscribeRequest(String channel) {
        this.channel = channel;
    }

    public String getLobbyName() { return channel; }

    public String toString() {
        return "channel: " + channel;
    }
}
