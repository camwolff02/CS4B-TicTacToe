package com.example.messages;

import com.example.*;

public class ClientDisconnectedMessage extends Message { 

    public ClientDisconnectedMessage() {}

    public String toString() {
        return "Client has disconnected";
    }
}