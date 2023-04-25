package TicTacToeGameManager

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import Serialize.Message;


public class GameControllerManager {
    private ServerSocket serverSocket;
    private ArrayList<ClientHandlerRouter> clients;
    
    public GameControllerManager(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        clients = new ArrayList<>();
    }
    
    public static void main(String[]args) throws IOException {  //run method to create each instance of game controller
        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandlerRouter client = new ClientHandlerRouter(socket, this);
            clients.add(client);

            if (clients.size() == 2) {
                // Start the game
                break;
            }
        }
        // Game logic goes here
    }
    
    public void broadcast(String message, ClientHandler sender) {
        for (ClientHandler client : clients) {
            if (client != sender) {
                client.sendMessage(message);
            }
        }
    }
    
    public void removeClient(ClientHandler client) {
        clients.remove(client);
    }
    
        
        public void sendMessage(String message) {
            // Send message to client
            // ...
        }
    }

