package messages;

import router.Message;

public class ExitRequest extends Message {
    public ExitRequest(String id) {
        super(id);
    }

    public String toString() {
        return "Exit";
    }
}