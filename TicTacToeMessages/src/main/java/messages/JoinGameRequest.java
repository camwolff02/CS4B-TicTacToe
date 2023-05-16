package messages;

import router.Message;

public class JoinGameRequest extends Message {
    public JoinGameRequest(String id) {
        super(id);
    }

    public String toString() {
        return super.toString() + "join game request";
    }
}