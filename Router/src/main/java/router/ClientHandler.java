package router;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

public class ClientHandler implements Runnable {    
    private static int currId = 0;
    private int id;
    
    private Router router;
    
    private Socket socket;                  // used for establish a connection between the client and server
    private BufferedReader bufferedReader;  // used for reading message that is sent from the client
    private BufferedWriter bufferedWriter;  // used for sending message to other client from a client

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ClientHandler(Router router, Socket socket) {
        id = currId++;
        
        try {
            this.router = router;
            this.socket = socket;
        
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
        } catch(IOException e) {
            System.out.println("[ERROR] [HANDLER] problem creating object stream");
            closeEverthing();
        }        
    }

    // a method that listens for the message using a separate thread
    @Override
    public void run() {
        while (true) {
            // make sure there is still a connection to the client and read the message
            try {
                Packet incomingPacket = (Packet)objectInputStream.readObject();
                
                String channel = incomingPacket.getChannel();
                String type = incomingPacket.getType();

                System.out.println("[INFO] [HANDLER] Message received: <channel: " + channel + ", type: " + type + ">" );

                if (type.equals("subscribe")) {
                    System.out.println("[INFO] [HANDLER] attempting to subscribe");
                    router.subscribeToChannel(this, channel);
                }
                else if (type.equals("unsubscribe")) {
                    System.out.println("[INFO] [HANDLER] attempting to unsubscribe");
                    router.unsubscribeFromChannel(this, channel);
                }
                else {
                    System.out.println("[INFO] [HANDLER] attempting to send message");
                    router.broadcastPacket(this, channel, incomingPacket);
                }
            } catch (IOException e) {
                System.out.println("[ERROR] [HANDLER] problem reading object input stream");
                closeEverthing();
            } catch (ClassNotFoundException e){
                System.out.println("[ERROR] [HANDLER] problem casting object input stream");
                closeEverthing();
            }
        }
        
    }

    public String toString() {
        return "[HANDLER] [" + id + "]";
    }

    public void sendPacketToClient(Packet packet) {
        try {
            this.objectOutputStream.writeObject(packet);
            this.objectOutputStream.flush();
        } catch(IOException e){
            System.out.println("[ERROR] [HANDLER] problem writing to object output stream");
            closeEverthing();
        }   
 
    }

    // a method that will close the socket after a client has left or there is an error
    private void closeEverthing(){
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
