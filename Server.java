import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
    private ServerSocket serverSocket;

    //a construter method
    public Server(ServerSocket serverSocket){
        this.serverSocket = serverSocket;
    }

    // a run method that will start the server
    public void startServer(){
        try{

            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                System.out.println("A new client has connected!");
                ClientHandler clienHandler = new ClientHandler(socket);

                Thread thread = new Thread(clienHandler);
                thread.start();
            }
        } catch (IOException e){

        }
    }

    // a method that will close the server socket
    public void closeServerSocket(){
        // make sure the server socket is not pointed to a null
        try{
            if(serverSocket != null){
                serverSocket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // here is the main method
    public static void main(String[] args) throws IOException{
        // created the server sovket with a port
        ServerSocket serverSocket = new ServerSocket(1234);
        // created the server object
        Server server = new Server(serverSocket);
        // called the run method that starts the server
        server.startServer();
    }
     
}