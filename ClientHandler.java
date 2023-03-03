public class ClientHandler implements Runnable {
    public void run() {

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
