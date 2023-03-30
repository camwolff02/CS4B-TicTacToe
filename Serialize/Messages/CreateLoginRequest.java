package Serialize.Messages;

import Serialize.ApplicationMessage;

public class CreateLoginRequest extends ApplicationMessage {
    String username;
    String password;
    String profilePic;

    public CreateLoginRequest(String username, String password, String profilePic) {
        this.username = username;
        this.password = password;
        this.profilePic = profilePic;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getProfilePic() { return profilePic; }

    public String toString() {
        return "Username: " + username + "\nPassword: " + password
        + "\nProfile picture: " + profilePic;
    }


}