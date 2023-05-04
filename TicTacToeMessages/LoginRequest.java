package TicTacToeMessages;

import Router.src.main.java.router.*;

public class LoginRequest extends Message {
    String username;
    String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String toString() {
        return "Username: " + username + "\nPassword: " + password;
    }


}