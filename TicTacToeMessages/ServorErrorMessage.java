package TicTacToeMessages;

import Router.src.main.java.router.*;

public class ServorErrorMessage extends Message { 

    public String toString() {
        return "Something either failed or message does not exist";
    }
}