package com.example.messages;

import com.example.router.Message;

public class ServerErrorMessage extends Message { 

    public ServerErrorMessage(String id) {
        super(id);
    }

    public String toString() {
        return super.toString() + "Something either failed or message does not exist";
    }
}