package Serialize.Messages;

import Serialize.Message;

public class ClientDisconnectedMessage extends Message { 

    public ClientDisconnectedMessage() {}

    public String toString() {
        return "Client has disconnected";
    }
}