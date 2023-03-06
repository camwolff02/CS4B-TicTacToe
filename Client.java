import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String userName;

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

    // a method that will send the message
    public void sendMessage(){
        try{
            bufferedWriter.write(userName);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while(socket.isConnected()){
                String messagetoSend = scanner.nextLine();

                bufferedWriter.write(userName + ": " + messagetoSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        }catch(IOException e){
            closeEverthing(socket, bufferedReader, bufferedWriter);
        }
    }

    // a method that will listen for incoming messege and used a runnable
    public void listenForMessage(){
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
    }
}
