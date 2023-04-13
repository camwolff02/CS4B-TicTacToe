import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.ConnectException;
import java.net.Socket;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import Serialize.ApplicationMessage;
import Serialize.Message;
import Serialize.Messages.*;

// TODO maybe include sender in message packet?

public class ClientRouter {
    Scanner scanner;
    private Socket socket;  // used for establish a connection between the client and server

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    private boolean waiting;

    public static void main(String [] args) throws IOException{
        Socket socket = null;
        
        while (socket == null) {
            try {
                socket = new Socket("LocalHost", 1234);
            } 
            catch (ConnectException e) {
                System.out.println("waiting for server...");
                try {
                    TimeUnit.SECONDS.sleep(1);        
                } catch (InterruptedException ex) {}
            }
        }

        System.out.println("Connected to server!");
        ClientRouter client = new ClientRouter(socket);
        
        client.startMessageListener();

        // TODO create proper way to break out of loop and end client
        while (true)
            client.sendMessage(client.createMessage());
    }


    public ClientRouter(Socket socket){
        
        try {
            this.waiting = false;
            this.scanner = new Scanner(System.in);

            this.socket = socket;
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            
        } catch (IOException e) {
            closeEverthing();
        }
    }


    // Listens for messege that has been broadcasted
    public void startMessageListener(){
        // have to used a new thred so the program will not be halted
        new Thread(new Runnable() {
            @Override
            public void run(){
                while(socket.isConnected()){
                    try{
                        Message incomingMessage = (Message) objectInputStream.readObject();

                        System.out.println(incomingMessage);

                    } catch (IOException e){
                        closeEverthing();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    
                }

            }
        }).start();
    }

    private Message createMessage() {
        ApplicationMessage appMessage = null;
       
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
                    appMessage = createApplicationMessage(channel, type);
                    break;

                case "2":
                    type = "unsubscribe";
                    appMessage = createApplicationMessage(channel, type);
                    break;

                case "3":
                    while (appMessage == null) {
                        System.out.print("Enter type: ");
                        type = scanner.nextLine();      
                        appMessage = createApplicationMessage(channel, type);
                    }
                    break;

                default:
                    System.out.println("ERROR: please enter 1, 2, or 3");
            }
        }

        return new Message(channel, type, appMessage);
    }

    private ApplicationMessage createApplicationMessage(String channel, String type) {
        switch (type) {
            case "subscribe":
                return new SubscribeRequest(channel);

            case "unsubscribe":
                return new UnsubscribeRequest(channel);

            case "create_login":
                return new CreateLoginRequest("username", "password", "picture.png");
            
            case "add_profile_pic":
                return new AddProfilePicRequest("picture.png"); 

            case "login":
                return new LoginRequest("user", "abcde");
                
            case "create_game":
                return new CreateGameRequest("Homi's Lobby"); 

            case "join_game":
                return new JoinGameRequest("Homi's Lobby");

            case "client_info":
                return new ClientInfoMessage("username", "picture.png");

            case "make_move":
                int[] moves = {1, 2};
                return new MakeMoveRequest("Homi's Lobby", "Player2", moves); 

            case "list_games":
                return new ListGamesRequest(); 

            case "list_of_games":
                ArrayList<String> games = new ArrayList<>(Arrays.asList("Homi's Lobby", "Player2's Lobby"));
                return new ListOfGamesResponse(games); 

            case "action_success":
                return new ActionSuccessResponse(true);

            case "start_game":
                return new StartGameRequest(true, "Homi's Lobby");

            case "client_disconnected":
                return new ClientDisconnectedMessage();

            case "game_over":
                return new GameOverMessage(GameState.TIE);

            case "play_again": 
                return new PlayAgainRequest(true);

            case "exit":
                return new ExitRequest();
            default:
                System.out.println("ERROR: please enter an existing message type");
                return null;
        }
    }

    // Sends the message to the clientHandler
    public void sendMessage(Message messageToSend) {
        try {
            objectOutputStream.writeObject(messageToSend);
            objectOutputStream.flush();
            
            System.out.println("Message sent!\n"
                + "Press enter when ready to execute next action");
            scanner.nextLine();
            
        } catch (IOException e) {
            closeEverthing();
        }
    }

    // Close everthing down
    public void closeEverthing() {
        try {
            if (objectInputStream != null)
                objectInputStream.close();

            if (objectOutputStream != null)
                objectOutputStream.close();
            
            if (socket != null) 
                socket.close();

            if (scanner != null) 
                scanner.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}