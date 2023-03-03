public class Client {

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