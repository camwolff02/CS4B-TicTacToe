import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRouter {
    public static void main(String[] args) throws IOException {        
        // created the server socket with a port
        ServerSocket serverSocket = new ServerSocket(1234);
        // created the server object
        ServerRouter server = new ServerRouter(serverSocket);
        // called the run method that starts the server
        System.out.println("Server started");
        server.startServer();
    }

    // this is for listening for incoming connections or clients 
    // to creat a socket object for conmunication 
    private ServerSocket serverSocket;

    //a construtor method
    public ServerRouter(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    // a run method that will start the server and keeping the server running
    public void startServer() {
        try{
            // the server will be constantly running untill the server socket is closed
            while (!serverSocket.isClosed()){
                // a blocking method for halting the program untill a client has conected
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");

                // a class that will be responsible for the communication with the client and have a runnable interface
                ClientHandlerRouter clienHandler = new ClientHandlerRouter(socket);
                Thread thread = new Thread(clienHandler);
                thread.start();
            }
        } catch (IOException e){

        }
    }

    // a method that will close the server socket if an error has occurs
    public void closeServerSocket() {
        // make sure the server socket is not pointed to a null
        try{
            if(serverSocket != null) {
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }     
}