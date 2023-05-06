package router;

import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;


public class Router {
    private HashMap<String, HashSet<ClientHandler>> channelSubscribers;
    private ServerSocket serverSocket;

    public static void main(String[] args) throws IOException {        
        // created the server socket with a port
        ServerSocket serverSocket = new ServerSocket(1234);
        // created the server object
        Router server = new Router(serverSocket);
        // called the run method that starts the server
        System.out.println("[INFO] [ROUTER] Server started");
        server.start();
    }

    public Router(ServerSocket serverSocket) {
        this.channelSubscribers = new HashMap<>();
        this.serverSocket = serverSocket;
    }

    public boolean channelExists(String channel) {
        return channelSubscribers.containsKey(channel);
    }


    public void broadcastPacket(ClientHandler callingHandler, String channel, Packet packet) {        
        boolean noClientsInChannel = true;
        boolean notSubscribedToChannel = true;
        
        try {
            if (!channelSubscribers.get(channel).contains(callingHandler)) {
                callingHandler.sendPacketToClient(packet);  //"ERROR: not a member of channel"
                return;
            }
            for (ClientHandler subscriber : channelSubscribers.get(channel)) {  // for each member in that channel
                notSubscribedToChannel = false;
                
                if (subscriber != callingHandler) {
                    noClientsInChannel = false;
                    subscriber.sendPacketToClient(packet);
                }
            }
        }
        catch (NullPointerException e) {
            System.out.println("[ERROR] [HANDLER] tried to send message to nonexistent channel");
            return;
        }
        
        if (notSubscribedToChannel) {
            System.out.println("[ERROR] [ROUTER]" + callingHandler + " tried to send message without being subscribed");
        }
        
        if (noClientsInChannel)
            System.out.println("[WARNING] [ROUTER]" + callingHandler + " sent message with no other members in channel");
    }

    public void subscribeToChannel(ClientHandler callingHandler, String channel) {
        // if the channel doesn't exist, create the channel
        if (!channelSubscribers.containsKey(channel)) 
            channelSubscribers.put(channel, new HashSet<ClientHandler>());

        // add the client to the channel
        channelSubscribers.get(channel).add(callingHandler);
        
        System.out.println("[INFO] [ROUTER] " + callingHandler + " has entered \"" + channel + "\"");
    }

    public void unsubscribeFromChannel(ClientHandler callingHandler, String channel) {
        System.out.println("[INFO] [ROUTER]" + callingHandler + " has left \"" + channel + "\"");
        
        // broadcastMessage(callingHandler, channel, message);
        // callingHandler.sendMessageToClient(message);

        try {
            channelSubscribers.get(channel).remove(callingHandler);
        }
        catch (NullPointerException e) {
            System.out.println("[WARNING] [ROUTER] could not leave chanel");
            // TODO send error that channel could not be left
        }

    }    
    
    public void removeClient(ClientHandler handler) {
        for (Map.Entry<String, HashSet<ClientHandler>> entry : channelSubscribers.entrySet()) {
            if (entry.getValue().contains(handler))
                entry.getValue().remove(handler);
        }
    }

    // a run method that will start the server and keeping the server running
    private void start() {
        try {
            // the server will be constantly running untill the server socket is closed
            while (true){
                // a blocking method for halting the program untill a client has conected
                Socket socket = serverSocket.accept();
                System.out.println("[INFO] [ROUTER] A new client has connected!");

                // a class that will be responsible for the communication with the client and have a runnable interface
                ClientHandler clienHandler = new ClientHandler(this, socket);
                // Encapsulate thread in ClientHandler, then start the thread 
                Thread thread = new Thread(clienHandler);
                thread.start();
            }
        } catch (IOException e){
            System.out.println("[ERROR] [ROUTER] problem connecting to client's socket");
        }
    }
}