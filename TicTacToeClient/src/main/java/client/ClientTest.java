package client;

import java.io.IOException;
// import java.util.ArrayList;
// import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.Scanner;

// import router.Packet;
// import router.Message;

// import TicTacToeMessages.*;

public class ClientTest {
    public static void main(String [] args) {
        Scanner scanner = new Scanner(System.in);

        // start client listener
        TicTacToeClient c = new TicTacToeClient();
        c.start();

        // wait for connection
        while (!c.isConnected()) {
            System.out.println("Waiting for connection...");
            try {
                TimeUnit.SECONDS.sleep(1);        
            } catch (InterruptedException ex) {}
        }

        System.out.println("Client Connected to server");

        while (true) {
            // handle incoming messages
            while (c.hasUnreadMessages()) {
                System.out.println(c.getLatestMessage());
            }

            // send outgoing messages
            c.sendPacket(createPacket(scanner));

            System.out.println("Press enter to receive messages and send next message");
            scanner.nextLine();
        }
    }

    private static Packet createPacket(Scanner scanner) {
        Message message = null;
        String type = null;
        String channel = null;

        System.out.print("Please enter a channel name to interact with: ");
        channel = scanner.nextLine();

        System.out.println("Please enter 1, 2, or 3:\n" 
        + "1. <subscribe> to a channel\n" 
        + "2. <unsubscribe> from a channel\n"
        + "3. <send> a message");

        while (type == null) {
            System.out.print("selection: ");

            switch (scanner.nextLine()) {
                case "1":
                    type = "subscribe";
                    message = createMessage(channel, type);
                    break;

                case "2":
                    type = "unsubscribe";
                    message = createMessage(channel, type);
                    break;

                case "3":
                    while (message == null) {
                        System.out.print("Enter type: ");
                        type = scanner.nextLine();      
                        message = createMessage(channel, type);
                    }
                    break;

                default:
                    System.out.println("ERROR: please enter 1, 2, or 3");
            }
        }

        return new Packet(channel, type, message);
    }

    private static Message createMessage(String channel, String type) {
        // switch (type) {
        //     case "subscribe":
        //         return new SubscribeRequest(channel);

        //     case "unsubscribe":
        //         return new UnsubscribeRequest(channel);

        //     case "create_login":
        //         return new CreateLoginRequest("username", "password", "picture.png");
            
        //     case "add_profile_pic":
        //         return new AddProfilePicRequest("picture.png"); 

        //     case "login":
        //         return new LoginRequest("user", "abcde");
                
        //     case "create_game":
        //         return new CreateGameRequest("Homi's Lobby"); 

        //     case "join_game":
        //         return new JoinGameRequest("Homi's Lobby");

        //     case "client_info":
        //         return new ClientInfoMessage("username", "picture.png");

        //     case "make_move":
        //         int[] moves = {1, 2};
        //         return new MakeMoveRequest("Homi's Lobby", "Player2", moves); 

        //     case "list_games":
        //         return new ListGamesRequest(); 

        //     case "list_of_games":
        //         ArrayList<String> games = new ArrayList<>(Arrays.asList("Homi's Lobby", "Player2's Lobby"));
        //         return new ListOfGamesResponse(games); 

        //     case "action_success":
        //         return new ActionSuccessResponse(true);

        //     case "start_game":
        //         return new StartGameRequest(true, "Homi's Lobby");

        //     case "client_disconnected":
        //         return new ClientDisconnectedMessage();

        //     case "game_over":
        //         return new GameOverMessage(GameState.TIE);

        //     case "play_again": 
        //         return new PlayAgainRequest(true);

        //     case "exit":
        //         return new ExitRequest();
        //     default:
        //         System.out.println("ERROR: please enter an existing message type");
        //         return null;
        // }

        return new CreateLoginRequest("username", "password", "picture.png");
    }
}