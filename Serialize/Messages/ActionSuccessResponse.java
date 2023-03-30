package Serialize.Messages;

import Serialize.ApplicationMessage;

public class ActionSuccessResponse extends ApplicationMessage { 
    boolean actionSucceeded;

    public ActionSuccessResponse(boolean actionSucceeded) {
        this.actionSucceeded = actionSucceeded;
    }

    public boolean getActionSucceeded() { return actionSucceeded; }

    public String toString() {
        return "Action Success State: " + actionSucceeded;
    }
}
