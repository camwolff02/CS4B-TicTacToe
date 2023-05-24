package com.example.router;

public class IDMessage extends Message {
    public String id;

    public IDMessage(String senderID, String id) {
        super(senderID);
        this.id = id;
    }

    public String getID() { return id; }

    public String toString() {
        return super.toString() + "ID: #" + id;
    }
}
