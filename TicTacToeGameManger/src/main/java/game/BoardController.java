package game;

import java.util.ArrayList;
import java.util.List;
import client.TicTacToeClient;
import router.Message;
import game.BoardLogic;
import messages.MakeMoveRequest;
import messages.MakeMoveResponse;

public class BoardController implements Runnable {
    private final TicTacToeClient client;
    private final List<String> playerIds;
    private final String id;
    private final BoardLogic boardLogic;
    
    public BoardController(String id1, String id2) {
        this.client = new TicTacToeClient();
        this.client.start();
        this.id = client.getID();
        this.playerIds = new ArrayList<>();
        this.playerIds.add(id1);
        this.playerIds.add(id2);
        this.boardLogic = new BoardLogic();
        for (String playerId : playerIds) {
            client.subscribeToChannel(playerId);
        }
    }
    
    public String getBoardID() {
        return client.getID();
    }
    
    @Override
    public void run() {
        // Start the game loop
        while (!boardLogic.isGameOver()) {
            // Wait for player moves or other events
            try {
                Thread.sleep(100); // Add a small delay to avoid excessive CPU usage
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Process incoming messages
            while (client.hasUnreadMessages()) {
                Message message = client.getLatestMessage();
                handleMessage(message);
            }
        }
        
        // Game over, perform any necessary cleanup or game-ending actions
    }
    
    private void handleMessage(Message message) {
        // Handle messages received from the player channels
        String playerId = message.getPlayerId();
		
		if (message instanceof MakeMoveRequest) {
            MakeMoveRequest makeMoveRequest = (MakeMoveRequest) message;
            handleMakeMoveRequest(makeMoveRequest, playerId);
		}
    }
    
    private void handleMakeMoveRequest(MakeMoveRequest message, String playerId) {
        int row = message.getRow();
        int col = message.getCol();
        boolean success = boardLogic.makeMove(playerId, row, col);
        if (success) {
            // Send updated board state to other player
            String[][] cells = boardLogic.getCells();
            String otherPlayerId = getOtherPlayerId(playerId);
            MakeMoveResponse response = new MakeMoveResponse(cells);
            try {
                client.sendMessage(otherPlayerId,"MakeMoveResponse", response);
            } catch (ClientException e) {
                e.printStackTrace();
            }
            
            // Check if the current player has won
            String winner = boardLogic.getWinner();
            if (winner != null) {
                // Player has won, perform actions accordingly
                handleWin(winner);
            }
        } else {
            // Send error message to current player
            ErrorResponse response = new ErrorResponse("Invalid move");
            try {
                client.sendMessage(playerId, "MakeMoveResponse", response);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void handleWin(String winner) {
        // Perform actions for winning player
    }
    
    // Get the other player's ID who is not the one making the move on the board
    private String getOtherPlayerId(String playerId) {
        return playerIds.stream()
            .filter(id -> !id.equals(playerId))
            .findFirst()
            .orElse(null);
    }
}