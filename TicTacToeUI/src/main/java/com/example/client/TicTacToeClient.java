package com.example.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import com.example.router.*;
import com.example.messages.*;

import java.util.LinkedList;
import java.util.Queue;

public class TicTacToeClient extends Thread {
    private int id;
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

    public void connect() {
        System.out.println("[INFO] [CLIENT] client starting");
        
        // connect client to server
        while (socket == null) {
            try {
                socket = new Socket("LocalHost", 1234);
            } 
            catch (UnknownHostException e) {
                try {
                    System.out.println("[INFO] [CLIENT] connecting...");
                    TimeUnit.SECONDS.sleep(1);
                    socket = null;        
                } catch (InterruptedException ex) {}
            }
            catch (IOException e) { 
                try {
                    System.out.println("[INFO] [CLIENT] connecting...");
                    TimeUnit.SECONDS.sleep(1);
                    socket = null;        
                } catch (InterruptedException ex) {} 
            }
        }
        System.out.println("[SUCCESS] [CLIENT] connected!");

        // starts object stream to communicate with server
        try {
            this.objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            this.objectInputStream = new ObjectInputStream(socket.getInputStream());
        
            System.out.println("[INFO] [CLIENT] communication with router connected");


            boolean hasId = false;
            while (!hasId) {
                try {
                    // wait to recieve our ID
                    this.id = objectInputStream.readInt();
                    System.out.println("[INFO] [CLIENT] ID received, subscribing to personal channel");
        
                    // subscribe to the channel with our own ID
                    subscribeToChannel(Integer.toString(id));
                    hasId = true;
                } catch (IOException e) { /* expected when no messages sent */ }
            }
        
        } 
        catch (IOException e) {
            System.out.println("[ERROR] [CLIENT] creating object stream");
            closeEverthing();
        }

        isConnected = true;
        System.out.println("[INFO] [CLIENT] client ready to run");
    }

    // UI Client public interface
    public String getID() { return Integer.toString(id); }
    public boolean isConnected() { return this.isConnected; }
    public boolean hasUnreadMessages() { return !unreadMessages.isEmpty(); }
    public int numUnreadMessages() { return unreadMessages.size(); }
    public Message getLatestMessage() { return unreadMessages.remove(); }
    
    // Join channel
    public void subscribeToChannel(String channel) {
        Message subMessage = new SubscribeRequest(Integer.toString(id), channel);
        sendMessage("router", "subscribe", subMessage);
    }

    // Leave channel
    public void unsubscribeFromChannel(String channel) {
        Message unsubMessage = new UnsubscribeRequest(Integer.toString(id), channel);
        sendMessage("router", "unsubscribe", unsubMessage);
    }

    // Sends the packet to the router
    public void sendMessage(String channel, String type, Message message) {
        Packet packet = new Packet(channel, type, message);

        try {
            objectOutputStream.writeObject(packet);
            objectOutputStream.flush();            
        } catch (IOException e) {
            System.out.println("[ERROR] [CLIENT] sending packet");
            closeEverthing();
        }
    }

    // Listens for messege that has been broadcasted
    @Override
    public void start() {
        // have to used a new thred so the program will not be halted
        new Thread(new Runnable() {
            @Override
            public void run(){
                System.out.println("[INFO] [CLIENT] starting message listener");

                while (socket.isConnected()){
                    try{
                        Packet incomingPacket = (Packet)objectInputStream.readObject();
                        Message incomingMessage = unwrapPacket(incomingPacket);
                        unreadMessages.add(incomingMessage);

                    } 
                    catch (IOException e) { /* expected when no messages sent */ }
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
            case "id":
                return (IDMessage)packet.getMessage();
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