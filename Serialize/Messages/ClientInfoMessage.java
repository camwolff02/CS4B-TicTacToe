package Serialize.Messages;

import Serialize.ApplicationMessage;

public class ClientInfoMessage extends ApplicationMessage {
    String username;
    String profilePic;

    public ClientInfoMessage(String username, String profilePic) {
        this.username = username;
        this.profilePic = profilePic;
    }

    public String getUsername() { return username; }

    public String getProfilePic() { return profilePic; }

    public String toString() {
        return "Username: " + username + "\nProfile Picture: " + profilePic;
    }


}