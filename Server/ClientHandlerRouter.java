import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import Serialize.Message;


// The sever start method will run this client handler class so there is no main here
// implements Runnable here so the instances will be executed by a separate thread(override the run method) 
public class ClientHandlerRouter implements Runnable {    
    // this class should be unaware of other connections, abstract functionality
    // to router

    private ServerRouter router;
    
    private Socket socket;                  // used for establish a connection between the client and server
    private BufferedReader bufferedReader;  // used for reading message that is sent from the client
    private BufferedWriter bufferedWriter;  // used for sending message to other client from a client
    private String clientInfo;              // used for identify for whitch clinet

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    // a construter method
    public ClientHandlerRouter(ServerRouter router, Socket socket) {
        this.router = router;
        
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            this.clientInfo = bufferedReader.readLine();            
        } catch(IOException e) {
            closeEverthing();
        }        
    }

    public Message getClientMessage() throws ClassNotFoundException, IOException{
        Message message = (Message) objectInputStream.readObject();
        return message;
    }

    public void sendMessage(Message message){
        // objectOutputStream.writeObject(message);
        System.out.println(message);
    }

    // a method that listens for the message using a separate thread
    @Override
    public void run() {
        // make sure there is still a connection to the client and read the message
            try {
                Message incomingMessage = (Message)objectInputStream.readObject();
                
                router.broadcastMessage(this, incomingMessage.getChannel(), incomingMessage);

                System.out.println("Message received: " + incomingMessage.getChannel());
                // Message clientMessage = getClientMessage();

                // if (clientMessage.getType() == "SubscribeMessageRequest") {
                //      router.subscribeToChannel(this, clientMessage.getChannel(), clientMessage);
                // }
                // else if(clientMessage.getType() == "UnsubscribeMessageRequest"){
                //     router.unsubscribeFromChannel(this, clientMessage.getChannel(), clientMessage);
                // }

                // String clientMessage = bufferedReader.readLine();
                // if (clientMessage.startsWith("Subscribe::")) {
                //     String channel = clientMessage.split("::", 2)[1]; 
                //     router.subscribeToChannel(this, channel);
                // }
                // else if (clientMessage.startsWith("Unsubscribe::")) {
                //     String channel = clientMessage.split("::", 2)[1]; 
                //     router.unsubscribeFromChannel(this, channel);
                // }
                // else {
                //     try {
                //         var arr = clientMessage.split("::", 2); 
                //         String channel = arr[0];
                //         String message = arr[1];
                //         router.broadcastMessage(this, channel, clientInfo + ": " + message);
                //     } catch (ArrayIndexOutOfBoundsException e) {
                //         sendMessageToClient("ERROR: must send message in format \"Channel::message\"");
                //     }      
                // }
            } catch(IOException e) {
                closeEverthing();
            } catch(ClassNotFoundException e){
                closeEverthing();
            }
        
    }

    public String toString() {
        return clientInfo;
    }

    public void sendMessageToClient(Message message) {
        try {
            this.objectOutputStream.writeObject(message);
            this.objectOutputStream.flush();

        } catch(IOException e){
            closeEverthing();
        }   
 
    }

    // a method that will send message to all the client that is connected except the client that 
    // send the message

    // a method that will close the socket after a client has left or there is an error
    private void closeEverthing(){
        //removeClientHandlerRouter();
        try{
            if(this.bufferedReader != null){
                bufferedReader.close();
            }

            if(this.bufferedWriter != null){
                bufferedWriter.close();
            }
            
            if(this.socket != null){
                socket.close();
            }

            if(objectInputStream != null)
            {
                objectInputStream.close();
            }

            if(objectOutputStream != null)
            {
                objectOutputStream.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
