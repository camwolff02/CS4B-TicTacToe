package com.example.client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import com.example.messages.*;
import com.example.router.IDMessage;
import com.example.router.Message;


public class ClientTest {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        boolean keep_running = true;

        // start client listener
        TicTacToeClient c = new TicTacToeClient();
        c.connect();
        c.start();

        System.out.println("[SUCCESS] Client Connected to server");

        while (keep_running) {
            // handle incoming messages
            while (c.hasUnreadMessages()) {
                System.out.println(c.getLatestMessage());
            }

            // send outgoing messages
            keep_running = sendMessage(c, scanner);

            System.out.println("[INFO] Press enter to receive messages and send next message");
            scanner.nextLine();
        }

        scanner.close();

        System.out.println("[SUCCESS] program exited, press 'ctrl+c' to kill client");
    }

    private static boolean sendMessage(TicTacToeClient client, Scanner scanner) {
        Message message = null;
        String type = null;
        String channel = null;

        System.out.print("[INFO] Please enter a channel name to interact with: ");
        channel = scanner.nextLine();

        System.out.println("[INFO] Please enter 1, 2, or 3:\n" 
        + "1. <subscribe> to a channel\n" 
        + "2. <unsubscribe> from a channel\n"
        + "3. <send> a message\n"
        + "4. wait for messages\n"
        + "5. exit\n");

        while (type == null) {
            System.out.print("[INPUT] selection: ");

            switch (scanner.nextLine()) {
                case "1":
                    type = "subscribe";
                    client.subscribeToChannel(channel);
                    break;

                case "2":
                    type = "unsubscribe";
                    client.unsubscribeFromChannel(channel);
                    break;

                case "3":
                    while (message == null) {
                        System.out.print("[INPUT] type: ");
                        type = scanner.nextLine();      
                        message = createMessage(client.getID(), channel, type);
                    }
                    client.sendMessage(channel, type, message);
                    break;

                case "4":
                    return true;

                case "5":
                    return false;

                default:
                    System.out.println("[ERROR] please enter 1, 2, 3, or 4");
            }
        }

        return true;
    }

    private static Message createMessage(String id, String channel, String type) {
        switch (type) {
            case "id":
                return new IDMessage(id, "channel");
            case "create_login":
                return new CreateLoginRequest(id, "username", "password", "picture.png");
            
            case "add_profile_pic":
                return new AddProfilePicRequest(id, "picture.png"); 

            case "login":
                return new LoginRequest(id, "user", "abcde");
                
            case "create_game":
                return new CreateGameRequest(id, "Homi's Lobby"); 

            case "join_game":
                return new JoinGameRequest(id);

            case "client_info":
                return new ClientInfoMessage(id, "username", 0);

            case "make_move":
                int[] moves = {1, 2};
                return new MakeMoveRequest(id, "Homi's Lobby", "Player2", moves); 

            case "list_games":
                return new ListGamesRequest(id); 

            case "list_of_games":
                ArrayList<String> games = new ArrayList<>(Arrays.asList("Homi's Lobby", "Player2's Lobby"));
                return new ListOfGamesResponse(id, games); 

            case "action_success":
                return new ActionSuccessResponse(id, true);

            case "start_game":
                return new StartGameRequest(id, true);

            case "client_disconnected":
                return new ClientDisconnectedMessage(id);

            case "game_over":
                return new GameOverMessage(id, GameState.TIE);

            case "play_again": 
                return new PlayAgainRequest(id, true);

            case "exit":
                return new ExitRequest(id);
            default:
                System.out.println("[ERROR] not a proper message type");
                return null;
        }
    }
}