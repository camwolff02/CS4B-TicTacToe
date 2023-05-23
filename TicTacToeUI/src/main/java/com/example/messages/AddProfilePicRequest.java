package com.example.messages;

import com.example.router.Message;

public class AddProfilePicRequest extends Message {
    String profilePic;

    public AddProfilePicRequest(String id, String profilePic) {
        super(id);
        this.profilePic = profilePic;
    }

    public String getUsername() { return profilePic; }

    public String toString() {
        return super.toString() + "Profile picture name: " + profilePic;
    }
}
