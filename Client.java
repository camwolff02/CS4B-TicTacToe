import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;                  // used for establish a connection between the client and server
    private BufferedReader bufferedReader;  // used for reading message that is sent from the client
    private BufferedWriter bufferedWriter;  // used for sending message to other client from a client
    private String userName;                // used for identify for whitch clinet

    // a construter method
    public Client(Socket socket, String userName){
        try{
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.userName = userName;
        }catch(IOException e){
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }
    }

    // a method that will send the message to the clientHandler
    public void sendMessage(){
        try{
            bufferedWriter.write(userName);     // send the user name first
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);

            // make sure the socket is connected
            while(socket.isConnected()){
                String messagetoSend = scanner.nextLine();

                bufferedWriter.write(userName + ": " + messagetoSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }

            scanner.close();
        }catch(IOException e){
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }
    }

    // a method that will listen for messege that has been broadcasted
    public void listenForMessage(){
        // have to used a new thred so the program will not be halted
        new Thread(new Runnable() {
            @Override
            public void run(){
                String incomingMesseage;

                while(socket.isConnected()){
                    try{
                        incomingMesseage = bufferedReader.readLine();
                        System.out.println(incomingMesseage);
                    }catch(IOException e){
                        closeEverthing(socket, bufferedReader, bufferedWriter);
                    }
                }

            }
        }).start();
    }

    // a method that will close everthing down
    public void closeEverthing(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                bufferedReader.close();
            }

            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            
            if(socket != null){
                socket.close();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    // a main method
    public static void main(String [] args) throws IOException{
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter your name to start chatting: ");
        
        String userName = scanner.nextLine();
        Socket socket = new Socket("LocalHost", 1234);

        Client client = new Client(socket, userName);
        client.listenForMessage();
        client.sendMessage();

        scanner.close();
    }
}
