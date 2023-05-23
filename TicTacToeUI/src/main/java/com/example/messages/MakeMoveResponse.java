package com.example.messages;

import com.example.router.Message;

public class MakeMoveResponse extends Message {
    private int index;

    public MakeMoveResponse(String id, int index) {
      super(id);  
      this.index = index;
    }

    public int getGameMove() {
        return index;
    }
}