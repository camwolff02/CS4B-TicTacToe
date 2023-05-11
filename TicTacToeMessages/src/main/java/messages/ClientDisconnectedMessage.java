package messages;

import router.Message;

public class ClientDisconnectedMessage extends Message { 

    public ClientDisconnectedMessage(String id) {
        super(id);
    }

    public String toString() {
        return super.toString() + "Client has disconnected";
    }
}