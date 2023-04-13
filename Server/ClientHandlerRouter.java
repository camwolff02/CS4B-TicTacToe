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

    private static int currentId = 0;
    private int id;

    private ServerRouter router;
    
    private Socket socket;                  // used for establish a connection between the client and server
    private BufferedReader bufferedReader;  // used for reading message that is sent from the client
    private BufferedWriter bufferedWriter;  // used for sending message to other client from a client
    private String clientInfo;              // used for identify for whitch clinet

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;


    // a construter method
    public ClientHandlerRouter(ServerRouter router, Socket socket) {
        try {
            this.id = currentId;
            currentId++;
        
            this.router = router;
            this.socket = socket;
            // this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //this.clientInfo = bufferedReader.readLine();            
        } catch(IOException e) {
            closeEverthing();
        }        
    }

    // public Message getClientMessage() throws ClassNotFoundException, IOException{
    //     Message message = (Message) objectInputStream.readObject();
    //     return message;
    // }

    // public void sendMessage(Message message){
    //     // objectOutputStream.writeObject(message);
    //     System.out.println(message);
    // }

    // a method that listens for the message using a separate thread
    @Override
    public void run() {
        // make sure there is still a connection to the client and read the message
            try {
                Message incomingMessage = (Message)objectInputStream.readObject();
                
                String channel = incomingMessage.getChannel();
                String type = incomingMessage.getType();

                System.out.println("HANDLER: Message received: <channel: " + channel + ", type: " + type + ">" );

                if (type.equals("subscribe")) {
                    System.out.println("HANDLER: attempting to subscribe");
                    router.subscribeToChannel(this, channel);
                }
                else if (type.equals("unsubscribe")){
                    System.out.println("HANDLER: attempting to unsubscribe");
                    router.unsubscribeFromChannel(this, channel);
                }
                else {
                    System.out.println("HANDLER: attempting to send message");
                    router.broadcastMessage(this, channel, incomingMessage);
                }
            } catch(IOException e) {
                e.printStackTrace();
                closeEverthing();
            } catch(ClassNotFoundException e){
                e.printStackTrace();
                closeEverthing();
            }
        
    }

    public String toString() {
        // return clientInfo;
        return Integer.toString(id);
    }

    public void sendMessageToClient(Message message) {
        try {
            this.objectOutputStream.writeObject(message);
            this.objectOutputStream.flush();

        } catch(IOException e){
            closeEverthing();
        }   
 
    }

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
