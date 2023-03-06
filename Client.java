import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    public static int ip = 10;
    public Socket socket;

    public Client() {
        try {
            socket = new Socket("192.168.10." + ip, 50);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String message) {
        try (OutputStream out = socket.getOutputStream()) {
            out.write(message.getBytes());
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);

        boolean isStopped = false;
        while (!isStopped) {
            System.out.println("enter a message: ");
            client.sendMessage(scanner.nextLine());

            //...implement some way to stop client
        }

        client.closeSocket();
        scanner.close();
    }
}

/*
CLASS Client {
    Socket socket  

    FUNCTION Client() {
        socket = new Socket("Domain name", server port #)
    }

    FUNCTION closeSocket() {
        socket.close()
    }

    FUNCTION sendMessage(message) {
        OutputStream out = socket.getOutputStream();

        out.write(message.getBytes());
        out.flush();
        out.close();
    }

    FUNCTION main() {
        Client client = new Client()
        
        boolean isStopped = false
        WHILE (!isStopped) {
            PRINT("enter a message: ")
            INPUT(message)
            client.sendMessage(message)

            ...implement some way to stop client
        }

        client.closeSocket()
    }
}

// need thread on client side listening for messages from server
 
*/