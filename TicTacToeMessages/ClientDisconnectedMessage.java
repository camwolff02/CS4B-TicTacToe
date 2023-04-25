package TicTacToeMessages;

import Router.Message;

public class ClientDisconnectedMessage extends Message { 

    public ClientDisconnectedMessage() {}

    public String toString() {
        return "Client has disconnected";
    }
}