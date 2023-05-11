package messages;

import router.Message;

public class LoginRequest extends Message {
    String username;
    String password;

    public LoginRequest(String id, String username, String password) {
        super(id);
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String toString() {
        return super.toString() + "Username: " + username + "\nPassword: " + password;
    }


}