package com.example.router;

import java.io.Serializable;

public abstract class Message implements Serializable {
    private String senderID;

    public Message(String senderID) {
        this.senderID = senderID;
    };

    public String getSenderID() { return senderID; }

    public String toString() {
        return "Sender: ID #" + senderID + "\n";
    }
}
