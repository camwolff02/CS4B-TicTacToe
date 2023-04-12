import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.Socket;


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


    // a construter method
    public ClientHandlerRouter(ServerRouter router, Socket socket) {
        this.router = router;
        
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientInfo = bufferedReader.readLine();            
        } catch(IOException e) {
            closeEverthing();
        }        
    }

    // a method that listens for the message using a separate thread
    @Override
    public void run() {
        // make sur there is still a connection to the client and read the message
        while(socket.isConnected()) {
            try {
                // Message clientMessage = bufferedReader.readLine();

                // if (clientMessage.getType() == "SubscribeMessageRequest") {
                //     router.subscribeToChannel(this, clientMessage.getChannel());
                // }

                String clientMessage = bufferedReader.readLine();

                if (clientMessage.startsWith("Subscribe::")) {
                    String channel = clientMessage.split("::", 2)[1]; 
                    router.subscribeToChannel(this, channel);
                }
                else if (clientMessage.startsWith("Unsubscribe::")) {
                    String channel = clientMessage.split("::", 2)[1]; 
                    router.unsubscribeFromChannel(this, channel);
                }
                else {
                    try {
                        var arr = clientMessage.split("::", 2); 
                        String channel = arr[0];
                        String message = arr[1];
                        router.broadcastMessage(this, channel, clientInfo + ": " + message);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        sendMessageToClient("ERROR: must send message in format \"Channel::message\"");
                    }      
                }
            } catch(IOException e) {
                closeEverthing();
                break;      // when the client disconnects it will break out of the loop
            }
        }
    }

    public String toString() {
        return clientInfo;
    }

    public void sendMessageToClient(String message) {
        try {
            this.bufferedWriter.write(message);
            this.bufferedWriter.newLine();
            //manually flushing the buffere because it might not be big enough
            this.bufferedWriter.flush();  
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
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
