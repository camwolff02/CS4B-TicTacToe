package com.example.messages;

import com.example.router.Message;

public class ExitRequest extends Message {
    public ExitRequest(String id) {
        super(id);
    }

    public String toString() {
        return "Exit";
    }
}