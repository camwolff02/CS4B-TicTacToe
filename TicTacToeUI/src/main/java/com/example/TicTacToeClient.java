package com.example;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;
import java.util.LinkedList;
import java.util.Queue;

import com.example.messages.*;

public class TicTacToeClient extends Thread{
    private Socket socket; 
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private boolean isConnected;
    private Queue<Message> unreadMessages;

    public TicTacToeClient() {
        this.socket = null;
        this.isConnected = false;
        unreadMessages = new LinkedList<Message>();
    }

    @Override
    public void run() {
        // connect client to server
        while (socket == null) {
            try {
                socket = new Socket("LocalHost", 1234);
            } 
            catch (UnknownHostException e) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                    socket = null;        
                } catch (InterruptedException ex) {}
            }
            catch (IOException e) { /* expected when server not started */}
        }

        isConnected = true;

        // starts object stream to communicate with server
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        } 
        catch (IOException e) {
            System.out.println("[ERROR] [CLIENT] creating object stream");
            closeEverthing();
        }

        startMessageListener();
    }

    // UI Client public interface
    public boolean isConnected() { return this.isConnected; }
    public boolean hasUnreadMessages() { return !unreadMessages.isEmpty(); }
    public int numUnreadMessages() { return unreadMessages.size(); }
    public Message getLatestMessage() { return unreadMessages.remove(); }
    
    // Sends the packet to the router
    public void sendPacket(Packet packet) {
        try {
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();            
        } catch (IOException e) {
            System.out.println("[ERROR] [CLIENT] sending packet");
            closeEverthing();
        }
    }

    // Listens for messege that has been broadcasted
    private void startMessageListener() {
        // have to used a new thred so the program will not be halted
        new Thread(new Runnable() {
            @Override
            public void run(){
                while (socket.isConnected()){
                    try{
                        Packet incomingPacket = (Packet)objectInputStream.readObject();
                        Message incomingMessage = unwrapPacket(incomingPacket);
                        unreadMessages.add(incomingMessage);

                    } 
                    catch (IOException e) { /* expected when no messages sent */}
                    catch (ClassNotFoundException e) {
                        System.out.println("[ERROR] [CLIENT] casting packet from object input stream");
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private Message unwrapPacket(Packet packet) {
        switch (packet.getType()) {
            case "create_login":
                return (CreateLoginRequest)packet.getMessage();
            case "add_profile_pic":
                return (AddProfilePicRequest)packet.getMessage(); 
            case "login":
                return (LoginRequest)packet.getMessage();
            case "create_game":
                return (CreateGameRequest)packet.getMessage(); 
            case "join_game":
                return (JoinGameRequest)packet.getMessage();
            case "client_info":
                return (ClientInfoMessage)packet.getMessage();
            case "make_move":
                return (MakeMoveRequest)packet.getMessage(); 
            case "list_games":
                return (ListGamesRequest)packet.getMessage(); 
            case "list_of_games":
                return (ListOfGamesResponse)packet.getMessage(); 
            case "action_success":
                return (ActionSuccessResponse)packet.getMessage();
            case "start_game":
                return (StartGameRequest)packet.getMessage();
            case "client_disconnected":
                return (ClientDisconnectedMessage)packet.getMessage();
            case "game_over":
                return (GameOverMessage)packet.getMessage();
            case "play_again": 
                return (PlayAgainRequest)packet.getMessage();
            case "exit":
                return (ExitRequest)packet.getMessage();
            default:
                return (CreateLoginRequest)packet.getMessage();
        }
    }

    // Close everthing down
    private void closeEverthing() {
        try {
            if (objectInputStream != null)
                objectInputStream.close();

            if (objectOutputStream != null)
                objectOutputStream.close();
            
            if (socket != null) 
                socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}