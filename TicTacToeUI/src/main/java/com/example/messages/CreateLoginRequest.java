package com.example.messages;

import com.example.router.Message;

public class CreateLoginRequest extends Message {
    String username;
    String password;
    String profilePic;

    public CreateLoginRequest(String id, String username, String password, String profilePic) {
        super(id);
        this.username = username;
        this.password = password;
        this.profilePic = profilePic;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getProfilePic() { return profilePic; }

    public String toString() {
        return super.toString() + "Username: " + username + "\nPassword: " + password
        + "\nProfile picture: " + profilePic;
    }


}