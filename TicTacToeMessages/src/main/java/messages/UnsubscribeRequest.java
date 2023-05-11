package messages;

import router.Message;

public class UnsubscribeRequest extends Message {
    private String channel;
    
    public UnsubscribeRequest(String id, String channel) {
        super(id);
        this.channel = channel;
    }

    public String getLobbyName() { return channel; }

    public String toString() {
        return super.toString() + "channel: " + channel;
    }
}
