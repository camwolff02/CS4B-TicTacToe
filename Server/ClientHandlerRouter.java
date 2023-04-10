import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

// The sever start method will run this client handler class so there is no main here
// implements Runnable here so the instances will be executed by a separate thread(override the run method) 
public class ClientHandlerRouter implements Runnable{

    // this array list is to keep track of all the clients
    public static Map<String, HashSet<ClientHandlerRouter>> channelSubscribers = new HashMap<>();
    public static ArrayList<ClientHandlerRouter> clientHandlers = new ArrayList<>();
    public static HashSet<String> subscribedChannels = new HashSet<>();


    private Socket socket;                  // used for establish a connection between the client and server
    private BufferedReader bufferedReader;  // used for reading message that is sent from the client
    private BufferedWriter bufferedWriter;  // used for sending message to other client from a client
    private String clientInfo;              // used for identify for whitch clinet


    // a construter method
    public ClientHandlerRouter(Socket socket) {
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientInfo = bufferedReader.readLine();            
        }catch(IOException e){
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }        
    }

    // a method that listens for the message using a separate thread
    @Override
    public void run() {
        String clientMessage;   // used for holding the message received from client

        // make sur there is still a connection to the client and read the message
        while(socket.isConnected()) {
            try{
                clientMessage = bufferedReader.readLine();
                
                if (clientMessage.startsWith("Subscribe::")) {
                    String channel = clientMessage.split("::", 1)[1]; 

                    if (!channelSubscribers.containsKey(channel)) 
                        channelSubscribers.put(channel, new HashSet<>());

                    channelSubscribers.get(channel).add(this);

                    broadcastMessage("Sever: " + clientInfo + " has enterd \"" + channel + "\"");
                }
                else if (clientMessage.startsWith("Unsubscribe::")) {
                    String channel = clientMessage.split("::", 1)[1]; 
                    
                    for (var clientHandler : channelSubscribers.get(channel)) {
                        if (clientHandler == this) {
                            channelSubscribers.get(channel).remove(this);
                            break;
                        }
                    }

                    broadcastMessage("Sever: " + clientInfo + " has left \"" + channel + "\"");
                }
                else {
                    broadcastMessage(clientMessage);
                }
                
            }catch(IOException e){
                closeEverthing(socket, bufferedReader, bufferedWriter);
                break;      // when the client disconnects it will break out of the loop
            }
        }
    }

    // a method that will send message to all the client that is connected except the client that 
    // send the message
    public void broadcastMessage(String sendMessage) {
        for (String channel : subscribedChannels) {  // for each channel we're subscribed to
            for (var clientHandler : channelSubscribers.get(channel)) {  // for each member in that channel
                // send message
                try{
                    if(clientHandler != this) {
                        clientHandler.bufferedWriter.write(sendMessage);
                        clientHandler.bufferedWriter.newLine();
                        //manually flushing the buffere because it might not be big enough
                        clientHandler.bufferedWriter.flush();   
                    }
                }catch(IOException e){
                    closeEverthing(socket, bufferedReader, bufferedWriter);
                }
            }
        }
            
    }

    // a method that will remov a client handler and notify everone a client has left
    public void removeClientHandlerRouter(){
        clientHandlers.remove(this);

        for (String channel : subscribedChannels) {
            for (var clientHandler : channelSubscribers.get(channel)) {
                if (clientHandler == this) {
                    channelSubscribers.get(channel).remove(this);
                    break;
                }
            }
        }
            
        broadcastMessage("Sever: " + clientInfo + " has left the room");
    }

    // a method that will close the socket after a client has left or there is an error
    public void closeEverthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandlerRouter();
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
