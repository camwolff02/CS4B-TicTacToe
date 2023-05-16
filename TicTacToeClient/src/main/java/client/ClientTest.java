package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

import router.IDMessage;
import router.Message;
import messages.*;

public class ClientTest {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);
        boolean keep_running = true;

        // start client listener
        TicTacToeClient c = new TicTacToeClient();
        c.start();

        // wait for connection
        while (!c.isConnected()) {
            System.out.println("[INFO] Waiting for connection...");
            try {
                TimeUnit.SECONDS.sleep(1);        
            } catch (InterruptedException ex) {}
        }

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
        + "4. exit");

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
                        message = createMessage(client, channel, type);
                        client.sendMessage(channel, type, message);
                    }
                    break;

                case "4":
                    return false;

                default:
                    System.out.println("[ERROR] please enter 1, 2, 3, or 4");
            }
        }

        return true;
    }

    private static Message createMessage(TicTacToeClient client, String channel, String type) {
        switch (type) {
            case "id":
                return new IDMessage(client.getID(), "channel");
            case "create_login":
                return new CreateLoginRequest(client.getID(), "username", "password", "picture.png");
            
            case "add_profile_pic":
                return new AddProfilePicRequest(client.getID(), "picture.png"); 

            case "login":
                return new LoginRequest(client.getID(), "user", "abcde");
                
            case "create_game":
                return new CreateGameRequest(client.getID(), "Homi's Lobby"); 

            case "join_game":
                return new JoinGameRequest(client.getID(), "Homi's Lobby");

            case "client_info":
                return new ClientInfoMessage(client.getID(), "username", "picture.png");

            case "make_move":
                int[] moves = {1, 2};
                return new MakeMoveRequest(client.getID(), "Homi's Lobby", "Player2", moves); 

            case "list_games":
                return new ListGamesRequest(client.getID()); 

            case "list_of_games":
                ArrayList<String> games = new ArrayList<>(Arrays.asList("Homi's Lobby", "Player2's Lobby"));
                return new ListOfGamesResponse(client.getID(), games); 

            case "action_success":
                return new ActionSuccessResponse(client.getID(), true);

            case "start_game":
                return new StartGameRequest(client.getID(), true, "Homi's Lobby");

            case "client_disconnected":
                return new ClientDisconnectedMessage(client.getID());

            case "game_over":
                return new GameOverMessage(client.getID(), GameState.TIE);

            case "play_again": 
                return new PlayAgainRequest(client.getID(), true);

            case "exit":
                return new ExitRequest(client.getID());
            default:
                System.out.println("[ERROR] not a proper message type");
                return null;
        }
    }
}