public class Server {
    
}

/*

CLASS Server {
    ArrayList<> clientHandlerList  // do we want list of threads, ClientHandlers, or sockets?

    FUNCTION main() {
        clientHandlerList = new ArrayList<>()

        boolean isStopped = false
        WHILE (!isStopped) {
            socket = serverSocket.accept()
            Thread clientHandler = new Thread(new ClientHandler(socket, clientHandlerList)
            clientHandler.start()
            clientHandlerList.append(clientHandler)

            ...implement some way to stop server
        }

        FOR (clientHandler IN clientHandlerList) {
            // close all client handler resources
        }
    }
}

*/