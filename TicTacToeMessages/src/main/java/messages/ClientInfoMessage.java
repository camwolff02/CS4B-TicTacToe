package messages;

import router.Message;

public class ClientInfoMessage extends Message {
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