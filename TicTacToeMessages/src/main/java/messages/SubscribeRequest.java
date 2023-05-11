package messages;

import router.Message;

public class SubscribeRequest extends Message {
    private String channel;
    
    public SubscribeRequest(String id, String channel) {
        super(id);
        this.channel = channel;
    }

    public String getLobbyName() { return channel; }

    public String toString() {
        return super.toString() + "channel: " + channel;
    }
}
