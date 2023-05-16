package game;

//import java.net.Socket;
import java.util.concurrent.TimeUnit;

import client.TicTacToeClient;
import router.Message;
import router.IDMessage;

public class PlayerManager{
    
    public static void main(String [] args) {

        // start client listener
        TicTacToeClient c = new TicTacToeClient();
        c.connect();
        c.start();
        
        // wait for connection
        while (!c.isConnected()) {
            System.out.println("[INFO] Waiting for connection...");
            try {
                TimeUnit.SECONDS.sleep(1);        
            } catch (InterruptedException ex) {}
        }

        System.out.println("[SUCCESS] PlayerManager Connected to server, waiting for players to Manage");
        
        //c.subscribeToChannel("join"); // creates and subscribes to own channel
        
        while (c.isConnected()) {
             
                
                while(c.numUnreadMessages()>=2)
                {
                    Message getMessage =c.getLatestMessage();
                    String Player1ID =getMessage.getSenderID();
                    System.out.println("Player: "+ Player1ID+  " Accepted");
                   // c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),Player1ID));  //subs player1
                    getMessage =c.getLatestMessage();
                    String Player2ID = getMessage.getSenderID();
                    System.out.println("Player: "+ Player2ID+  " Accepted");
                    //c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),Player2ID));  //subs player2
                  //  BoardController boardController = new BoardController(Player1ID, Player2ID);
                   // String boardID = boardController.getBoardID();
                    c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),"boardID1"));
                    c.sendMessage(Player2ID, "message", new IDMessage(c.getID(),"boardID1"));
                    //new Thread(boardController).start();
                    System.out.println("[SUCCESS] YAY YAY YAY YAY");
                }       
            
        }

        System.out.println("[ERROR] Either Server shut down, or something is wrong with player manager");
        
       
    }
    
    
}
