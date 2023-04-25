import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;
import java.util.HashSet;

import Serialize.Message;
/* inside server router, put private static ClientHandler properties here or in separate class
 * make main private
 * server has one public function "broadcast" (message in)
 *  pushes message to all clients
 */

public class ServerRouter {
    // put static ClientHandler properties here or in separate class

    private HashMap<String, HashSet<ClientHandlerRouter>> channelSubscribers;

    public static void main(String[] args) throws IOException {        
        // created the server socket with a port
        ServerSocket serverSocket = new ServerSocket(1234);
        // created the server object
        ServerRouter server = new ServerRouter(serverSocket);
        // called the run method that starts the server
        System.out.println("INFO: Server started");
        server.runServer();
    }

    // this is for listening for incoming connections or clients 
    // to creat a socket object for conmunication 
    private ServerSocket serverSocket;

    //a construtor method
    public ServerRouter(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        channelSubscribers = new HashMap<>();
    }

    public boolean channelExists(String channel) {
        return channelSubscribers.containsKey(channel);
    }


    public void broadcastMessage(ClientHandlerRouter callingHandler, String channel, Message message) {        
        boolean noClientsInChannel = true;
        boolean notSubscribedToAnyChannels = true;
        
        try {
            if (!channelSubscribers.get(channel).contains(callingHandler)) {
                callingHandler.sendMessageToClient(message);  //"ERROR: not a member of channel"
                return;
            }
            for (var subscriber : channelSubscribers.get(channel)) {  // for each member in that channel
                notSubscribedToAnyChannels = false;
                
                if (subscriber != callingHandler) {
                    noClientsInChannel = false;
                    subscriber.sendMessageToClient(message);
                }
            }
        }
        catch (NullPointerException e) {
            callingHandler.sendMessageToClient(message); //"ERROR: tried to send message to nonexistent channel"
            return;
        }
        
        if (notSubscribedToAnyChannels) {
            System.out.println("ERROR: " + callingHandler + " tried to send message without being subscribed");
            callingHandler.sendMessageToClient(message); //"ERROR: not a member of any channel"
        }
        
        if (noClientsInChannel)
            System.out.println("WARNING: " + callingHandler + " sent message with no other members in channel");
    }

    public void subscribeToChannel(ClientHandlerRouter callingHandler, String channel) {
        // if the channel doesn't exist, create the channel
        if (!channelSubscribers.containsKey(channel)) 
            channelSubscribers.put(channel, new HashSet<>());

        // add the client to the channel
        channelSubscribers.get(channel).add(callingHandler);
        
        System.out.println("INFO: " + callingHandler + " has entered \"" + channel + "\"");
        //broadcastMessage(callingHandler, channel, message);
        //callingHandler.sendMessageToClient(message);
    }

    public void unsubscribeFromChannel(ClientHandlerRouter callingHandler, String channel) {
        System.out.println("INFO: " + callingHandler + " has left \"" + channel + "\"");
        
        // broadcastMessage(callingHandler, channel, message);
        // callingHandler.sendMessageToClient(message);

        try {
            channelSubscribers.get(channel).remove(callingHandler);
        }
        catch (NullPointerException e) {
            // TODO send error that channel could not be left
            // callingHandler.sendMessageToClient(message);
        }

    }    
    

    // a run method that will start the server and keeping the server running
    private void runServer() {
        try{
            // the server will be constantly running untill the server socket is closed
            while (!serverSocket.isClosed()){
                // a blocking method for halting the program untill a client has conected
                Socket socket = serverSocket.accept();
                System.out.println("INFO: A new client has connected!");

                // a class that will be responsible for the communication with the client and have a runnable interface
                ClientHandlerRouter clienHandler = new ClientHandlerRouter(this, socket);
                // Encapsulate thread in ClientHandler, then do ClientHandler.start() 
                Thread thread = new Thread(clienHandler);
                thread.start();
            }
        } catch (IOException e){

        }
    }
}