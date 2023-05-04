package TicTacToeMessages;

import Router.src.main.java.router.*;


public class AddProfilePicRequest extends Message {
    String profilePic;

    public AddProfilePicRequest(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getUsername() { return profilePic; }

    public String toString() {
        return "Profile picture name: " + profilePic;
    }
}
