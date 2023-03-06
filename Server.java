import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket();

            ArrayList<Thread> clientHandlerList = new ArrayList<>();  // do we want list of threads, ClientHandlers, or sockets?

            boolean isStopped = false;
            while (!isStopped) {
                Socket socket;
                try {
                    socket = serverSocket.accept();

                    Thread clientHandler = new Thread(new ClientHandler(socket, clientHandlerList));
                    clientHandler.start();
                    clientHandlerList.add(clientHandler);
                    //...implement some way to stop server
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            for (var clientHandler : clientHandlerList) {
                // close all client handler resources
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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