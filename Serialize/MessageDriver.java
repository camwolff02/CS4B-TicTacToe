package Serialize;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

import Serialize.Messages.*;

public class MessageDriver {
    public static void main(String[] args) {
        // TEST CREATE GAME LOGIN REQUEST
        CreateLoginRequest appMessage1 = new CreateLoginRequest("username", "password", "picture.png");
        Message message1 = new Message("game", "create_login", appMessage1);
        testSerialization(message1);

        // TEST ADD PROFILE PIC REQUEST
        AddProfilePicRequest appMessage2 = new AddProfilePicRequest("picture.png");
        Message message2 = new Message("game", "add_profile_pic", appMessage2);
        testSerialization(message2);

        // TEST LOGIN REQUEST
        LoginRequest appMessage3 = new LoginRequest("user", "abcde");
        Message message3 = new Message("game", "login", appMessage3);
        testSerialization(message3);

        // TEST CREATE GAME REQUEST
        CreateGameRequest appMessage4 = new CreateGameRequest("Homi's Lobby");
        Message message4 = new Message("game", "create_game", appMessage4);
        testSerialization(message4);

        
        // TEST JOIN GAME REQUEST
        JoinGameRequest appMessage5 = new JoinGameRequest("Homi's Lobby");
        Message message5 = new Message("game", "join_game", appMessage5);
        testSerialization(message5);
        
        // TEST CREATE CLIENT INFO MESSAGE
        ClientInfoMessage appMessage6 = new ClientInfoMessage("username", "picture.png");
        Message message6 = new Message("game", "client_info", appMessage6);
        testSerialization(message6);
        
        // TEST MAKE MOVE REQUEST
        int[] moves = {1, 2};
        MakeMoveRequest appMessage7 = new MakeMoveRequest("Homi's Lobby", "Player2", moves);
        Message message7 = new Message("game", "make_move", appMessage7);
        testSerialization(message7);

        // TEST CREATE LIST GAME REQUEST
        ListGamesRequest appMessage8 = new ListGamesRequest();
        Message message8 = new Message("game", "list_games", appMessage8);
        testSerialization(message8);

        // TEST LIST OF GAMES RESPONSE
        ArrayList<String> games = new ArrayList<>(Arrays.asList("Homi's Lobby", "Player2's Lobby"));
        ListOfGamesResponse appMessage9 = new ListOfGamesResponse(games);
        Message message9 = new Message("game", "list_of_games", appMessage9);
        testSerialization(message9);

        //TEST ACTION SUCCESS MESSAGE
        ActionSuccessResponse appMessage10 = new ActionSuccessResponse(true);
        Message message10 = new Message("game", "action_success", appMessage10);
        testSerialization(message10);

        // TEST CREATE START GAME REQUEST
        StartGameRequest appMessage11 = new StartGameRequest(true, "Homi's Lobby");
        Message message11 = new Message("game", "start_game", appMessage11);
        testSerialization(message11);

        // TEST CLIENT DISCONNECTED MESSAGE
        ClientDisconnectedMessage appMessage12 = new ClientDisconnectedMessage();
        Message message12 = new Message("game", "client_disconnected", appMessage12);
        testSerialization(message12);

        // TEST GAME OVER MESSAGE
        GameOverMessage appMessage13 = new GameOverMessage(GameState.TIE);
        Message message13 = new Message("game", "game_over", appMessage13);
        testSerialization(message13);

        // TEST CLIENT DISCONNECTED MESSAGE
        PlayAgainRequest appMessage14 = new PlayAgainRequest(true);
        Message message14 = new Message("game", "play_again", appMessage14);
        testSerialization(message14);
        
        // TEST EXIT MESSAGE
        ExitRequest appMessage15 = new ExitRequest();
        Message message15 = new Message("game", "exit", appMessage15);
        testSerialization(message15);
        
    }

    public static void testSerialization(Message message) {
        testSerialization(message, 
            "Testing " + message.getType() + " message:\n"+ "---------------------------------------------------------" 
        );
    }

    public static void testSerialization(Message message, String info) { 
        String filename = "MessageData.txt";

        System.out.println(info);
        
        // Serialize message
        try{
            // Serialize message and send to router
            FileOutputStream file = new FileOutputStream(filename);
            ObjectOutputStream out = new ObjectOutputStream (file);
            
            out.writeObject(message);
            System.out.println("Object has been serialized");
            out.close();
        }
        catch (IOException ex) {
            ex.printStackTrace(); 
        }
    
        // De-serialize message
        try {
            // De-serialize message, look at channel, serialize and send to correct client
            FileInputStream file = new FileInputStream(filename);
            ObjectInputStream in = new ObjectInputStream(file);
            
            Message messageReceived = (Message)in.readObject();               
            System.out.println("Object has been deserialized");
            System.out.println(
                "Channel: " + messageReceived.getChannel()
                + "\nType: " + messageReceived.getType()
            ); 
            in.close();

            // De-serialize message and cast to correct message
            System.out.println("Message Contents: ");
            switch (message.getType()) {
                case "create_login":
                    System.out.println((CreateLoginRequest)messageReceived.getMessage());
                    break;
                
                case "add_profile_pic":
                    System.out.println((AddProfilePicRequest)messageReceived.getMessage());
                    break; 

                case "login":
                    System.out.println((LoginRequest)messageReceived.getMessage());
                    break;
                    
                case "create_game":
                    System.out.println((CreateGameRequest)messageReceived.getMessage());
                    break; 

                case "join_game":
                    System.out.println((JoinGameRequest)messageReceived.getMessage());
                    break;

                case "client_info":
                    System.out.println((ClientInfoMessage)messageReceived.getMessage());
                    break;

                case "make_move":
                    System.out.println((MakeMoveRequest)messageReceived.getMessage());
                    break; 

                case "list_games":
                    System.out.println((ListGamesRequest)messageReceived.getMessage());
                    break; 

                case "list_of_games":
                    System.out.println((ListOfGamesResponse)messageReceived.getMessage());
                    break; 

                case "action_success":
                    System.out.println((ActionSuccessResponse)messageReceived.getMessage());
                    break;

                case "start_game":
                    System.out.println((StartGameRequest)messageReceived.getMessage());
                    break;

                case "client_disconnected":
                    System.out.println((ClientDisconnectedMessage)messageReceived.getMessage());
                    break;

                case "game_over":
                    System.out.println((GameOverMessage)messageReceived.getMessage());
                    break;

                case "play_again": 
                    System.out.println((PlayAgainRequest)messageReceived.getMessage());
                    break;

                case "exit":
                    System.out.println((ExitRequest)messageReceived.getMessage());
            }

            System.out.println();
        } 
        catch (IOException ex) {
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    } 
}
