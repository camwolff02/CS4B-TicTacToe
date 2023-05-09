package router;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.net.Socket;

import messages.*;  // TODO REMOVE

public class ClientHandler implements Runnable {    
    private static int currId = 0;
    private int id;

    private Router router;
    private Socket socket;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public ClientHandler(Router router, Socket socket) {
        id = currId++;
        
        try {
            this.router = router;
            this.socket = socket;
        
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            // tell the client what their ID is
            objectOutputStream.writeInt(id);
        } catch(IOException e) {
            System.out.println("[ERROR] [HANDLER] problem creating object stream");
            closeEverthing();
        }        
    }

    // a method that listens for the message using a separate thread
    @Override
    public void run() {
        boolean connected = true;
        while (connected) {
            // make sure there is still a connection to the client and read the message
            try {
                Object o = objectInputStream.readObject();
                System.out.println("[INFO] [HANDLER] Packet received");
            
                Packet incomingPacket = (Packet)o;
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
                connected = false;
            } catch (ClassNotFoundException e){
                System.out.println("[ERROR] [HANDLER] problem casting object input stream");
                e.printStackTrace();
                closeEverthing();
                connected = false;
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
        try {
            router.removeClient(this);

            if (this.socket != null)
                socket.close();

            if (objectInputStream != null)
                objectInputStream.close();

            if (objectOutputStream != null)
                objectOutputStream.close();

        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}



/*
 
in PlayerManager:
    playerManager has id 3
    PlayerManager sees 2 joinGame messages
        - players that sent messages have ids 1 and 2

    PlayerManager spins up new BoardController, passing 2 client ids
        BoardController controller = new BoardController(id1, id2)
        controllerID = controller.getID();
        
        send message to each player with BoardControllerID
        
        controller.start()

in BoardController:
    in constructor BoardController(id1, id2):
        - create a new Client object
        - subscribe to channel id1, id2

    have a function, like getID() so PlayerManager can get your ID

in UI:
    send joinGame message
    wait for board id message
    subscribe to channel boardID
    display the Select Name screen

 */