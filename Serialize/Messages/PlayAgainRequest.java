package Serialize.Messages;

import Serialize.Message;

public class PlayAgainRequest extends Message { 
    
    boolean playAgain;

    public PlayAgainRequest(boolean playAgain) {
        this.playAgain = playAgain;
    }

    public boolean getPlayAgain() { return playAgain; }

    public String toString() {
        return "Client would like to play again: " + playAgain;
    }
}