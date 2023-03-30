package Serialize;

import java.io.Serializable;

public class Message implements Serializable {
    private String channel;
    private String type;
    private ApplicationMessage message;

    public Message(String channel, String type, ApplicationMessage message) {
        this.channel = channel;
        this.type = type;
        this.message = message;
    }
    
    public String getChannel() { return channel; }
    public String getType() { return type; }
    public ApplicationMessage getMessage() { return message; }   

    public String toString() {
        return "Channel: " + channel 
           + "\nType: " + type 
           + "\nMessage: " + message;
    }

    
}
