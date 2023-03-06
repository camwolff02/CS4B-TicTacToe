import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    Socket clientSocket;  
    DataInputStream clientIn;  
    DataOutputStream clientOut;  
    ArrayList<Thread> clientHandlerList;
    
    public void run() {
        // if (!clientSocket.isConnected()) {
        //     // stop this thread and remove this instance from list
        //     // clean up resources
        // }
        // String msg = clientIn.read();

        // if (true/*msg has data*/) {  // how do you tell if a user put data in the socket or not?
        //     for (ClientHandler clinetHandler : clientHandlerList) {
        //         if (clinetHandler.getSocket() != clientSocket) {
        //             clinetHandler.sendMessageToClient(msg);
        //         }
        //     }
        // }
    }

    public ClientHandler(Socket socket, ArrayList<Thread>clientHandlerList) {
        this.clientSocket = socket;
        this.clientHandlerList = clientHandlerList;
        try {
            this.clientIn = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            this.clientOut = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void sendMessageToClient(String msg) {
        try {
            clientOut.write(msg.getBytes());
            clientOut.flush();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public Socket getSocket() {
        return clientSocket;
    }
}

/*

CLASS ClientHandler implements Runnable {
    Socket clientSocket  
    InputStream clientIn  
    OutputStream clientOut  
    ArrayList<ClientHandler> clientHandlerList
    
    public void run() {
        IF (!clientSocket.isConnected()) {
            // stop this thread and remove this instance from list
            // clean up resources
        }

        msg = clientIn.read()

        IF (msg contains sent data) {  // how do you tell if a user put data in the socket or not?
            FOR (clinetHandler IN clientHandlerList) {
                IF (clientHandler.getSocket() != clientSocket) {
                    clientHandler.sendMessageToClient(msg)
                }
            }
        }
    }

    FUNCTION ClientMessageHandler(socket, clientHandlerList) {
        clientSocket = socket
        clientHandlerList = clientHandlerList
        clientIn = socket.getInputStream()
        clientOut = socket.getOutputStream()
    }

    FUNCTION sendMessageToClient(msg) {
        out.write(msg.getBytes())
        out.flush()
    }

    FUNCTION getSocket() {
        return clientSocket
    }
}

*/
