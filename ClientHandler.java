import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

// The sever start method will run this client handler class so there is no main here
// implements Runnable here so the instances will be executed by a separate thread(override the run method) 
public class ClientHandler implements Runnable{

    // this array list is to keep track of all the clients
    public static ArrayList<ClientHandler> clientHandler = new ArrayList<>();
    private Socket socket;                  // used for establish a connection between the client and server
    private BufferedReader bufferedReader;  // used for reading message that is sent from the client
    private BufferedWriter bufferedWriter;  // used for sending message to other client from a client
    private String clientInfo;              // used for identify for whitch clinet

    // a construter method
    public ClientHandler(Socket socket){
        try{
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientInfo = bufferedReader.readLine();

            // add the client to the array list and notify the other client when a new client as joint
            clientHandler.add(this);
            broadcastMessage("Sever: " + clientInfo + " has enterd the room");

        }catch(IOException e){
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }        
    }

    // a method that listens for the message using a separate thread
    @Override
    public void run(){
        String clientMessage;   // used for holding the message received from client

        // make sur there is still a connection to the client and read the message
        while(socket.isConnected()){
            try{
                clientMessage = bufferedReader.readLine();
                broadcastMessage(clientMessage);
            }catch(IOException e){
                closeEverthing(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }

    // a method that will send message
    public void broadcastMessage(String sendMessage){
        for(ClientHandler clientHandler: clientHandler){
            try{
                if(!clientHandler.clientInfo.equals(clientInfo)){
                    clientHandler.bufferedWriter.write(sendMessage);
                    clientHandler.bufferedWriter.newLine();
                    clientHandler.bufferedWriter.flush();
                }
            }catch(IOException e){
                closeEverthing(socket, bufferedReader, bufferedWriter);
            }
        }
    }

    // a method that will notify if a client has left
    public void removeClientHandler(){
        clientHandler.remove(this);
        broadcastMessage("Sever: " + clientInfo + " has left the room");
    }

    // a method that will close the socket after a client has left or there is an error
    public void closeEverthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        removeClientHandler();
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
