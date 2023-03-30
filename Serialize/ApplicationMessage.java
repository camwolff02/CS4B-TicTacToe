package Serialize;

import java.io.Serializable;

public abstract class ApplicationMessage implements Serializable {
    public ApplicationMessage() {};
    public abstract String toString();
}
