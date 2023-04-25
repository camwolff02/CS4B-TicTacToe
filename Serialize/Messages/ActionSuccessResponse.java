package Serialize.Messages;

import Serialize.Message;

public class ActionSuccessResponse extends Message { 
    boolean actionSucceeded;
    String infoMessage;

    public ActionSuccessResponse(boolean actionSucceeded, String infoMessage) {
        this.actionSucceeded = actionSucceeded;
        this.infoMessage = infoMessage;
    }

    public ActionSuccessResponse(boolean actionSucceeded) {
        this.actionSucceeded = actionSucceeded;
        this.infoMessage = "Action completed";
    }

    public boolean getActionSucceeded() { return actionSucceeded; }
    public String getInfoMessage() { return infoMessage; }


    public String toString() {
        if (infoMessage == "Action completed")
            return "Action Success State: " + actionSucceeded;
        else
            return "INFO: " + infoMessage;
    }
}
