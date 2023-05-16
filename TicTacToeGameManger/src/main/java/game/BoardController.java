package game;

import java.util.ArrayList;
import java.util.List;

import client.TicTacToeClient;
import router.Message;

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
        return client.getBoardID();
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
            List<Message> messages = client.getLatestMessage();
            for (Message message : messages) {
                handleMessage(message);
            }
        }
        
        // Game over, perform any necessary cleanup or game-ending actions
    }
    
    private void handleMessage(Message message) {
        // Handle messages received from the player channels
        String playerId = message.getSenderID();
		
		if (message instanceof MakeMoveRequest) {
			handleMakeMoveRequest((MakeMoveRequest) message, playerId);
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
        } else {
            // Send error message to current player
            ErrorResponse response = new ErrorResponse("Invalid move");
            try {
                client.sendMessage(playerId, "MakeMoveResponse",response);
            } catch (ClientException e) {
                e.printStackTrace();
            }
        }
    }
	
	// get the other player's ID who is not the one make move on the board
	private String getOtherPlayerId(String playerId) {
        return playerIds.stream()
            .filter(id -> !id.equals(playerId))
            .findFirst()
            .orElse(null);
    }
}
