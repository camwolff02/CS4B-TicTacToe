package Serialize.Messages;

import Serialize.ApplicationMessage;

public class UnsubscribeRequest extends ApplicationMessage {
    private String channel;
    
    public UnsubscribeRequest(String channel) {
        this.channel = channel;
    }

    public String getLobbyName() { return channel; }

    public String toString() {
        return "channel: " + channel;
    }
}
