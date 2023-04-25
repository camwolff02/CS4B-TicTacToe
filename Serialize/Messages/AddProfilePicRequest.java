package Serialize.Messages;

import Serialize.ApplicationMessage;

public class AddProfilePicRequest extends ApplicationMessage {
    String profilePic;

    public AddProfilePicRequest(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() { return profilePic; }

    public String toString() {
        return "Profile picture name: " + profilePic;
    }
}
