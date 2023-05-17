package com.example.messages;

import com.example.router.Message;

public class ClientInfoMessage extends Message {
    String username;
    int profilePic;

    public ClientInfoMessage(String id, String username, int profilePic) {
        super(id);
        this.username = username;
        this.profilePic = profilePic;
    }

    public String getUsername() { return username; }

    public int getProfilePic() { return profilePic; }

    public String toString() {
        return super.toString() + "Username: " + username + "\nProfile Picture: " + Integer.toString(profilePic);
    }


}