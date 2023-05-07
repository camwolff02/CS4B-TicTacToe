package UnitTests;

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
        CreateLoginRequest message1 = new CreateLoginRequest("username", "password", "picture.png");
        Packet packet1 = new Packet("game", "create_login", message1);
        testSerialization(packet1);

        // TEST ADD PROFILE PIC REQUEST
        AddProfilePicRequest message2 = new AddProfilePicRequest("picture.png");
        Packet packet2 = new Packet("game", "add_profile_pic", message2);
        testSerialization(packet2);

        // TEST LOGIN REQUEST
        LoginRequest message3 = new LoginRequest("user", "abcde");
        Packet packet3 = new Packet("game", "login", message3);
        testSerialization(packet3);

        // TEST CREATE GAME REQUEST
        CreateGameRequest message4 = new CreateGameRequest("Homi's Lobby");
        Packet packet4 = new Packet("game", "create_game", message4);
        testSerialization(packet4);

        
        // TEST JOIN GAME REQUEST
        JoinGameRequest message5 = new JoinGameRequest("Homi's Lobby");
        Packet packet5 = new Packet("game", "join_game", message5);
        testSerialization(packet5);
        
        // TEST CREATE CLIENT INFO MESSAGE
        ClientInfopacket message6 = new ClientInfopacket("username", "picture.png");
        Packet packet6 = new Packet("game", "client_info", message6);
        testSerialization(packet6);
        
        // TEST MAKE MOVE REQUEST
        int[] moves = {1, 2};
        MakeMoveRequest message7 = new MakeMoveRequest("Homi's Lobby", "Player2", moves);
        Packet packet7 = new Packet("game", "make_move", message7);
        testSerialization(packet7);

        // TEST CREATE LIST GAME REQUEST
        ListGamesRequest message8 = new ListGamesRequest();
        Packet packet8 = new Packet("game", "list_games", message8);
        testSerialization(packet8);

        // TEST LIST OF GAMES RESPONSE
        ArrayList<String> games = new ArrayList<>(Arrays.asList("Homi's Lobby", "Player2's Lobby"));
        ListOfGamesResponse message9 = new ListOfGamesResponse(games);
        Packet packet9 = new Packet("game", "list_of_games", message9);
        testSerialization(packet9);

        //TEST ACTION SUCCESS packet
        ActionSuccessResponse message10 = new ActionSuccessResponse(true);
        Packet packet10 = new Packet("game", "action_success", message10);
        testSerialization(packet10);

        // TEST CREATE START GAME REQUEST
        StartGameRequest message11 = new StartGameRequest(true, "Homi's Lobby");
        Packet packet11 = new Packet("game", "start_game", message11);
        testSerialization(packet11);

        // TEST CLIENT DISCONNECTED MESSAGE
        ClientDisconnectedpacket message12 = new ClientDisconnectedpacket();
        Packet packet12 = new Packet("game", "client_disconnected", message12);
        testSerialization(packet12);

        // TEST GAME OVER MESSAGE
        GameOverpacket message13 = new GameOverpacket(GameState.TIE);
        Packet packet13 = new Packet("game", "game_over", message13);
        testSerialization(packet13);

        // TEST CLIENT DISCONNECTED MESSAGE
        PlayAgainRequest message14 = new PlayAgainRequest(true);
        Packet packet14 = new Packet("game", "play_again", message14);
        testSerialization(packet14);
        
        // TEST EXIT MESSAGE
        ExitRequest message15 = new ExitRequest();
        Packet packet15 = new Packet("game", "exit", message15);
        testSerialization(packet15);
        
    }

    public static void testSerialization(Packet message) {
        testSerialization(message, 
            "Testing " + message.getType() + " message\n"+ "---------------------------------------------------------" 
        );
    }

    public static void testSerialization(Packet message, String info) { 
        String filename = "MessageData.txt";

        System.out.println(info);
        
        try{
            // CLIENT: Serialize message and send to router
            FileOutputStream file1 = new FileOutputStream(filename);
            ObjectOutputStream outClient = new ObjectOutputStream (file1);
            
            outClient.writeObject(message);
            System.out.println("Object has been serialized by client");
            outClient.close();



            // ROUTER: De-serialize message, look at channel, serialize and send to correct client
            FileInputStream file2 = new FileInputStream(filename);
            ObjectInputStream inRouter = new ObjectInputStream(file2);
            
            Packet messageRouter = (Packet)inRouter.readObject(); 
            inRouter.close();

            String channel = messageRouter.getChannel();
            String type = messageRouter.getType();
              
            System.out.println("Object has been deserialized by router");
            System.out.println(
                "Channel: " + channel
                + "\nType: " + type
            ); 

            if (channel.equals("game")) {
                System.out.println("Sending message to correct channel: " + channel);
            }

            FileOutputStream file3 = new FileOutputStream(channel + ".txt");
            ObjectOutputStream outRouter = new ObjectOutputStream (file3);
            
            outRouter.writeObject(message);
            System.out.println("Object has been serialized by router");
            outRouter.close();



            // CLIENT: De-serialize message and cast to correct message
            FileInputStream file4 = new FileInputStream(filename);
            ObjectInputStream inClient = new ObjectInputStream(file4);
            
            Packet messageClient = (Packet)inClient.readObject(); 
            inClient.close();

            System.out.println("Object has been deserialized by client");


            System.out.println("Message Contents: ");
            switch (messageClient.getType()) {
                case "create_login":
                    System.out.println((CreateLoginRequest)messageClient.getMessage());
                    break;
                
                case "add_profile_pic":
                    System.out.println((AddProfilePicRequest)messageClient.getMessage());
                    break; 

                case "login":
                    System.out.println((LoginRequest)messageClient.getMessage());
                    break;
                    
                case "create_game":
                    System.out.println((CreateGameRequest)messageClient.getMessage());
                    break; 

                case "join_game":
                    System.out.println((JoinGameRequest)messageClient.getMessage());
                    break;

                case "client_info":
                    System.out.println((ClientInfoMessage)messageClient.getMessage());
                    break;

                case "make_move":
                    System.out.println((MakeMoveRequest)messageClient.getMessage());
                    break; 

                case "list_games":
                    System.out.println((ListGamesRequest)messageClient.getMessage());
                    break; 

                case "list_of_games":
                    System.out.println((ListOfGamesResponse)messageClient.getMessage());
                    break; 

                case "action_success":
                    System.out.println((ActionSuccessResponse)messageClient.getMessage());
                    break;

                case "start_game":
                    System.out.println((StartGameRequest)messageClient.getMessage());
                    break;

                case "client_disconnected":
                    System.out.println((ClientDisconnectedMessage)messageClient.getMessage());
                    break;

                case "game_over":
                    System.out.println((GameOverMessage)messageClient.getMessage());
                    break;

                case "play_again": 
                    System.out.println((PlayAgainRequest)messageClient.getMessage());
                    break;

                case "exit":
                    System.out.println((ExitRequest)messageClient.getMessage());
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
