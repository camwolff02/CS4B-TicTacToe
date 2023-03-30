package Serialize.Messages;

import Serialize.ApplicationMessage;

public class ClientDisconnectedMessage extends ApplicationMessage { 

    public ClientDisconnectedMessage() {}

    public String toString() {
        return "Client has disconnected";
    }
}