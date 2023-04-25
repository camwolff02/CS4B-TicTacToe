package TicTacToeMessages;

import Router.Message;

public class ServorErrorMessage extends Message { 

    public String toString() {
        return "Something either failed or message does not exist";
    }
}