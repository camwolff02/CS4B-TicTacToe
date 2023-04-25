package Serialize.Messages;

import Serialize.Message;

public class SubscribeRequest extends Message {
    private String channel;
    
    public SubscribeRequest(String channel) {
        this.channel = channel;
    }

    public String getLobbyName() { return channel; }

    public String toString() {
        return "channel: " + channel;
    }
}
