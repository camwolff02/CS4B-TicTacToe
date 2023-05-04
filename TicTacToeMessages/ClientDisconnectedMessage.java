package TicTacToeMessages;

import Router.src.main.java.router.*;

public class ClientDisconnectedMessage extends Message { 

    public ClientDisconnectedMessage() {}

    public String toString() {
        return "Client has disconnected";
    }
}