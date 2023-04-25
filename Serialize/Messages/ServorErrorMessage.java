package Serialize.Messages;

import Serialize.ApplicationMessage;

public class ServorErrorMessage extends ApplicationMessage { 

    public String toString() {
        return "Something either failed or message does not exist";
    }
}