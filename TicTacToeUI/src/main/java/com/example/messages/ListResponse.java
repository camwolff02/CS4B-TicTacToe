package com.example.messages;

import java.util.ArrayList;

import com.example.router.Message;

public class ListResponse extends Message {
    ArrayList<String> list;
    
    public ListResponse(String id, ArrayList<String> list) {
        super(id);
        this.list = list;
    }

    public ArrayList<String> getLobbyNames() {
        return list;
    }

    public String toString() {
        String result = "[";

        for (String s : list) {
            result += s + ", ";
        }

        return super.toString() + "List of Games: " + result + "]";
    }
}
