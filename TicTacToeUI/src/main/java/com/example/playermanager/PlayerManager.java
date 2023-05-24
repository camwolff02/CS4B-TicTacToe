package com.example.playermanager;

import java.util.concurrent.TimeUnit;
import com.example.client.TicTacToeClient;
import com.example.messages.*;
import com.example.router.*;
public class PlayerManager{
    
    public static void main(String [] args) {

        // start client listener
        TicTacToeClient c = new TicTacToeClient();
        c.connect();
        c.start();
        
        // wait for connection

        System.out.println("[SUCCESS] PlayerManager Connected to server, waiting for players to Manage");
        
        c.subscribeToChannel("join"); // creates and subscribes to own channel
        
        while (c.isConnected()) {
       
            try
            { 
                TimeUnit.SECONDS.sleep(1);
                while(c.numUnreadMessages() >= 2)
                {
                    //System.out.println(c.getLatestMessage());
                    Message getMessage =c.getLatestMessage();
                    String Player1ID =getMessage.getSenderID();
                    System.out.println("Player: "+ Player1ID+  " Accepted");
                   // c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),Player1ID));  //subs player1
                    getMessage =c.getLatestMessage();
                    String Player2ID = getMessage.getSenderID();
                    System.out.println("Player: "+ Player2ID+  " Accepted");
                    //c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),Player2ID));  //subs player2
                    BoardController boardController = new BoardController(Player1ID, Player2ID);
                    String boardID = boardController.getBoardID();
                    c.sendMessage(Player1ID, "id", new IDMessage(c.getID(),boardID));
                    c.sendMessage(Player2ID, "id", new IDMessage(c.getID(),boardID));
                    new Thread(boardController).start();
                    System.out.println("[SUCCESS] YAY YAY YAY YAY");
                }  
            }catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }      
            
        }

        System.out.println("[ERROR] Either Server shut down, or something is wrong with player manager");
        
       
    }
    
    
}
