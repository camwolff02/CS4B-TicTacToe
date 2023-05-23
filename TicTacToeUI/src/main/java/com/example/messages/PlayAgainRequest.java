package com.example.messages;

import com.example.router.Message;

public class PlayAgainRequest extends Message { 
    
    boolean playAgain;

    public PlayAgainRequest(String id, boolean playAgain) {
        super(id);
        this.playAgain = playAgain;
    }

    public boolean getPlayAgain() { return playAgain; }

    public String toString() {
        return super.toString() + "Client would like to play again: " + playAgain;
    }
}