package com.example.messages;

import com.example.router.Message;

public class ActionSuccessResponse extends Message { 
    boolean actionSucceeded;
    String infoMessage;

    public ActionSuccessResponse(String id, boolean actionSucceeded, String infoMessage) {
        super(id);
        this.actionSucceeded = actionSucceeded;
        this.infoMessage = infoMessage;
    }

    public ActionSuccessResponse(String id, boolean actionSucceeded) {
        super(id);
        this.actionSucceeded = actionSucceeded;
        this.infoMessage = "Action completed";
    }

    public boolean getActionSucceeded() { return actionSucceeded; }
    public String getInfoMessage() { return infoMessage; }


    public String toString() {
        if (infoMessage == "Action completed")
            return super.toString() + "Action Success State: " + actionSucceeded;
        else
            return super.toString() + "INFO: " + infoMessage;
    }
}
