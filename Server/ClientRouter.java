import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Serialize.ApplicationMessage;
import Serialize.Message;
import Serialize.Messages.*;

public class ClientRouter {
    public static void main(String [] args) throws IOException{
        Scanner scanner = new Scanner(System.in);

        System.out.print("Please enter your name to start chatting: ");
        String userName = scanner.nextLine();


        Socket socket = new Socket("LocalHost", 1234);

        ClientRouter client = new ClientRouter(socket, userName);
        client.sendMessage(createMessage());
        // client.listenForMessage();
        scanner.close();
    }

    private Socket socket;                  // used for establish a connection between the client and server
    private BufferedReader bufferedReader;  // used for reading message that is sent from the client
    private BufferedWriter bufferedWriter;  // used for sending message to other client from a client

    private ObjectOutputStream objectOutputStream;

    private ObjectInputStream objectInputStream;

    
    private String userName;                // used for identify for whitch clinet

    // a construter method
    public ClientRouter(Socket socket, String userName){
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
            
            this.userName = userName;
        } catch (IOException e) {
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }
    }

    static private Message createMessage() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Channel: ");
        String channel = scanner.nextLine();

        System.out.print("Enter type: ");
        String type = scanner.nextLine();

        scanner.close();
        ApplicationMessage appMessage = null;
        Message message;

        switch (type) {
            case "create_login":
            appMessage = new CreateLoginRequest("username", "password", "picture.png");
                break;
            
            case "add_profile_pic":
            appMessage = new AddProfilePicRequest("picture.png");
                break; 

            case "login":
            appMessage = new LoginRequest("user", "abcde");
                break;
                
            case "create_game":
            appMessage = new CreateGameRequest("Homi's Lobby");
                break; 

            case "join_game":
            appMessage = new JoinGameRequest("Homi's Lobby");
                break;

            case "client_info":
            appMessage = new ClientInfoMessage("username", "picture.png");
                break;

            case "make_move":
            int[] moves = {1, 2};
            appMessage = new MakeMoveRequest("Homi's Lobby", "Player2", moves);
                break; 

            case "list_games":
            appMessage = new ListGamesRequest();
                break; 

            case "list_of_games":
            ArrayList<String> games = new ArrayList<>(Arrays.asList("Homi's Lobby", "Player2's Lobby"));
            appMessage = new ListOfGamesResponse(games);
                break; 

            case "action_success":
            appMessage = new ActionSuccessResponse(true);

                break;

            case "start_game":
            appMessage = new StartGameRequest(true, "Homi's Lobby");
                break;

            case "client_disconnected":
            appMessage = new ClientDisconnectedMessage();

                break;

            case "game_over":
            appMessage = new GameOverMessage(GameState.TIE);
                break;

            case "play_again": 
            appMessage = new PlayAgainRequest(true);
                break;

            case "exit":
            appMessage = new ExitRequest();
        }

        message = new Message(channel, type, appMessage);

        return message;

    }

    // a method that will send the message to the clientHandler
    public void sendMessage(Message messageToSend) {
        try {
            bufferedWriter.write(userName);     // send the user name first
            bufferedWriter.newLine();
            bufferedWriter.flush();
    
            objectOutputStream.writeObject(messageToSend);
            objectOutputStream.flush();
    
            System.out.println("Object has been serialized by client");
            
        } catch(IOException e) {
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }
    }
    

    // a method that will listen for messege that has been broadcasted
    public void listenForMessage(){

        
        // have to used a new thred so the program will not be halted
        new Thread(new Runnable() {
            @Override
            public void run(){

                while(socket.isConnected()){
                    try{
                        Message incomingMessage = (Message) objectInputStream.readObject();

                        System.out.println(incomingMessage);

                    }catch(IOException e){
                        closeEverthing(socket, bufferedReader, bufferedWriter);
                    } catch (ClassNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    
                }

            }
        }).start();
    }

    // a method that will close everthing down
    public void closeEverthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }

            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
