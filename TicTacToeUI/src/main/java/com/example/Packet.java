package com.example;

import java.io.Serializable;

public class Packet implements Serializable {
    private String channel;
    private String type;
    private Message message;

    public Packet(String channel, String type, Message message) {
        this.channel = channel;
        this.type = type;
        this.message = message;
    }
    
    public String getChannel() { return channel; }
    public String getType() { return type; }
    public Message getMessage() { return message; }   

    public String toString() {
        return "Channel: " + channel 
           + "\nType: " + type 
           + "\nMessage: " + message;
    }
}