package game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
//import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import client.TicTacToeClient;
import messages.SubscribeRequest;
import router.Message;
import router.Packet;
import router.IDMessage;

public class PlayerManager{
    
    
    //private static Queue<TicTacToeClient> waitingPlayers;
    //later implementation
    // private Map<String, BoardController> channelToBoardControllerMap;
    // private Map<String, TicTacToeClient> idToClientMap;
    //private PlayerManager playerManager;
    
    // private BoardController boardController;
    // private boolean running;
    public static void main(String [] args) {

        // start client listener
        TicTacToeClient c = new TicTacToeClient();
        c.start();
        c.getID();
        // wait for connection
        while (!c.isConnected()) {
            System.out.println("[INFO] Waiting for connection...");
            try {
                TimeUnit.SECONDS.sleep(1);        
            } catch (InterruptedException ex) {}
        }

        System.out.println("[SUCCESS] PlayerManager Connected to server, waiting for players to Manage");
        
        c.sendMessage("join", "subscribe", new SubscribeRequest("join")); // creates and subscribes to own channel

        while (c.isConnected()) {
            
            
                while(c.numUnreadMessages()>=2)
                {
                    Message getMessage =c.getLatestMessage();
                    String Player1ID =getMessage.getSenderID();
                   // c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),Player1ID));  //subs player1
                    getMessage =c.getLatestMessage();
                    String Player2ID = getMessage.getSenderID();
                    //c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),Player2ID));  //subs player2
                    //BoardController boardController = new BoardController(Player1ID, Player2ID);
                    //String boardID = boardController.getBoardID();
                    c.sendMessage(Player1ID, "message", new IDMessage(c.getID(),"boardID1"));
                    c.sendMessage(Player2ID, "message", new IDMessage(c.getID(),"boardID1"));
                    //new Thread(boardController).start();
                    System.out.println("[SUCCESS] YAY YAY YAY YAY");
                }       
            
        }

        System.out.println("[ERROR] Either Server shut down, or something is wrong with player manager");
        
      
        
        //Message mess = new IDMessage(c.getID(),getMessage.getSenderID());
        //String player1id = getMessage.getID;
        

        //bind player data and stuff to some temp variable
        //send back to player to subsribe them to the chanell
    }


   


    // private static Message queueupMessage(String channel, String type)
    // {
    //     return new SubscribeRequest(channel);
    // }


    // public PlayerManager() {
    //     this.waitingPlayers = new LinkedList<>();
    //     waitingPlayers.add(c);
    //     notifyAll();

    // }

    // public void removePlayerFromQueue() {
    //     synchronized (waitingPlayers) {
    //         waitingPlayers.remove(client);
    //     }
    // }

    // public synchronized void addPlayer() {
    //     waitingPlayers.add(client);
    //     notifyAll();
    // }
    // private void checkAndPairPlayers() {
        
    //         TicTacToeClient player1 = waitingPlayers.poll();
    //         TicTacToeClient player2 = waitingPlayers.poll();

    //         System.out.println("[SUCCESS] Players Paired game will begin");
    //         BoardController boardController = new BoardController();
    // }
    // private void createNewGame(Player player1, Player player2) {
    //     boardController.startNewGame(player1, player2);
    // }
    // @Override
    // public void run() {
    //     while (true) {
    //         synchronized (this) {
    //             while (waitingPlayers.size() < 2) {
    //                 try {
    //                     wait();
    //                 } catch (InterruptedException e) {
    //                     e.printStackTrace();
    //                 }
    //             }
    //         }
    //         TicTacToeClient player1 = waitingPlayers.poll();
    //         TicTacToeClient player2 = waitingPlayers.poll();
    //     }
            
    // }

            

    //         System.out.println("Paired players: " + player1+ " and " + player2);
    //         // Create and start a new BoardController with paired players
    //     }
    // }


    // private void checkAndPairPlayers() {
    //     while (waitingPlayers.size() >= 2) {
    //         ClientHandler player1 = waitingPlayers.poll();
    //         ClientHandler player2 = waitingPlayers.poll();
    //         createNewGame(player1, player2);
    //     }
    // }

    // private void createNewGame() {

    //     String channelName = UUID.randomUUID().toString();
    //     boardController.startNewGame();
    // }

//     public TicTacToeClient getClientById(String clientId) {
//         return idToClientMap.get(clientId);
//     }
// }
    
    
}
