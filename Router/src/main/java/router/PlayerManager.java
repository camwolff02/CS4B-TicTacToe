package router;

import java.util.LinkedList;
import java.util.Queue;
//import java.net.Socket;

public class PlayerManager implements Runnable {

    private Queue<ClientHandler> waitingPlayers;
    // private BoardController boardController;
    // private boolean running;

    public PlayerManager() {
        this.waitingPlayers = new LinkedList<>();
        // this.boardController = boardController;
        // this.running = true;
    }
    //in whatever thing call PlayerManager.addPlayerToQueue(client)
    public synchronized void addPlayerToQueue(ClientHandler client) {
        waitingPlayers.add(client);
        notifyAll();
        System.out.println("adding johnsons to the queue");
        }
    

    public void removePlayerFromQueue(ClientHandler client) {
        synchronized (waitingPlayers) {
            waitingPlayers.remove(client);
        }
    }

    @Override
    public void run() {
        while (true) {
            synchronized (this) {
                while (waitingPlayers.size() < 2) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            ClientHandler player1 = waitingPlayers.poll();
            ClientHandler player2 = waitingPlayers.poll();

            System.out.println("Paired players: " + player1+ " and " + player2);
            // Create and start a new BoardController with paired players
        }
    }


    // private void checkAndPairPlayers() {
    //     while (waitingPlayers.size() >= 2) {
    //         ClientHandler player1 = waitingPlayers.poll();
    //         ClientHandler player2 = waitingPlayers.poll();
    //         createNewGame(player1, player2);
    //     }
    // }

    // private void createNewGame(ClientHandler player1, ClientHandler player2) {
    //     boardController.startNewGame(player1, player2);
    // }

}
