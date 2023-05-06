package messages;

import router.Message;

public class ClientDisconnectedMessage extends Message { 

    public ClientDisconnectedMessage() {}

    public String toString() {
        return "Client has disconnected";
    }
}