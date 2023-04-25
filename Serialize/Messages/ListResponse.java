package Serialize.Messages;

import java.util.ArrayList;

import Serialize.Message;

public class ListResponse extends Message {
    ArrayList<String> list;
    
    public ListResponse(ArrayList<String> list) {
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

        return "List of Games: " + result + "]";
    }
}
