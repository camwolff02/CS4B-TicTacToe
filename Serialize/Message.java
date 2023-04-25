package Serialize;

import java.io.Serializable;

public abstract class Message implements Serializable {
    public Message() {};
    public abstract String toString();
}
