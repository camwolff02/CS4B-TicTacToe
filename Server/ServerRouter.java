import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.HashMap;
import java.util.HashSet;

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
        System.out.println("Server started");
        server.startServer();
    }

    // this is for listening for incoming connections or clients 
    // to creat a socket object for conmunication 
    private ServerSocket serverSocket;

    //a construtor method
    public ServerRouter(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        channelSubscribers = new HashMap<>();
    }

    public void broadcastMessageAndIncludeSelf(String channel, String message) {
        broadcastMessage(null, channel, message);
    }

    public void broadcastMessage(ClientHandlerRouter callingHandler, String channel, String message) {
        if (!channelSubscribers.get(channel).contains(callingHandler)) {
            callingHandler.sendMessageToClient("ERROR: not a member of channel");
            return;
        }
        
        boolean noClientsInChannel = true;
        boolean notSubscribedToAnyChannels = true;

        try {
            for (var subscriber : channelSubscribers.get(channel)) {  // for each member in that channel
                notSubscribedToAnyChannels = false;
                
                if (subscriber != callingHandler) {
                    noClientsInChannel = false;
                    subscriber.sendMessageToClient(message);
                }
            }
        }
        catch (NullPointerException e) {
            callingHandler.sendMessageToClient("ERROR: tried to send message to nonexistent channel");
            return;
        }
        
        if (notSubscribedToAnyChannels) {
            System.out.println("ERROR: " + callingHandler + " tried to send message without being subscribed");
            callingHandler.sendMessageToClient("ERROR: not a member of any channel");
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
        broadcastMessage(callingHandler, channel, "Sever: " + callingHandler + " has entered \"" + channel + "\" successfully");
        callingHandler.sendMessageToClient("Sever: channel \"" + channel + "\" entered successfully");
    }

    public void unsubscribeFromChannel(ClientHandlerRouter callingHandler, String channel) {
        System.out.println("INFO: " + callingHandler + " has left \"" + channel + "\"");
        
        broadcastMessage(callingHandler, channel, "Sever: " + callingHandler + " has left \"" + channel + "\" successfully");
        callingHandler.sendMessageToClient("Sever: channel \"" + channel + "\" left successfully");

        try {
            channelSubscribers.get(channel).remove(callingHandler);
        }
        catch (NullPointerException e) {
            callingHandler.sendMessageToClient("ERROR: could not leave channel");
        }

    }    

    // a run method that will start the server and keeping the server running
    private void startServer() {
        try{
            // the server will be constantly running untill the server socket is closed
            while (!serverSocket.isClosed()){
                // a blocking method for halting the program untill a client has conected
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");

                // a class that will be responsible for the communication with the client and have a runnable interface
                ClientHandlerRouter clienHandler = new ClientHandlerRouter(this, socket);
                // Encapsulate thread in ClientHandler, then do ClientHandler.start() 
                Thread thread = new Thread(clienHandler);
                thread.start();
            }
        } catch (IOException e){

        }
    }

    // a method that will close the server socket if an error has occurs
    private void closeServerSocket() {
        // make sure the server socket is not pointed to a null
        try{
            if(serverSocket != null) {
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }     
}