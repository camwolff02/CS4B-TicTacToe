package messages;

import router.Message;

public class ListGamesRequest extends Message {

    public ListGamesRequest(String id) {
        super(id);
    }

    public String toString() {
        return super.toString() + "List Games";
    }


}