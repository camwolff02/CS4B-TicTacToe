package com.example.messages;

import com.example.router.Message;

public class ListRequest extends Message {
    public ListRequest(String id) {
        super(id);
    }

    public String toString() {
        return super.toString() + "Request to send list";
    }
}