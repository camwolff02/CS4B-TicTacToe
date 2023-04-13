package Serialize.Messages;

import Serialize.ApplicationMessage;

public class SubscribeRequest extends ApplicationMessage {
    private String channel;
    
    public SubscribeRequest(String channel) {
        this.channel = channel;
    }

    public String getLobbyName() { return channel; }

    public String toString() {
        return "channel: " + channel;
    }
}
