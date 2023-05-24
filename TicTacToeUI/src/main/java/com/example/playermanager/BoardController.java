package com.example.playermanager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.example.client.TicTacToeClient;
import com.example.router.Message;
import com.example.playermanager.BoardLogic;
import com.example.messages.*;

public class BoardController implements Runnable {
    private final TicTacToeClient client;
    private String boardID;
    private final List<String> playerIds;
    private final BoardLogic boardLogic;

    public BoardController(String id1, String id2) {
        client = new TicTacToeClient();
        client.connect();
        client.start();

        playerIds = new ArrayList<>();
        playerIds.add(id1);
        playerIds.add(id2);

        boardLogic = new BoardLogic();
        boardID = getBoardID();

        for (String playerId : playerIds) {
            String playerChannel = boardID;
            client.subscribeToChannel(playerChannel);
        }
    }

    public String getBoardID() {
        return client.getID();
    }

    @Override
    public void run() {
        // Game loop
        while (!boardLogic.isGameOver()) {
            try {
                Thread.sleep(100); // Small delay to avoid excessive CPU usage
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Process incoming messages
            while (client.hasUnreadMessages()) {
                Message message = client.getLatestMessage();
                handleMessage(message);
            }
        }
    }

    private void handleMessage(Message message) {
        String playerId = message.getSenderID();

        if (message instanceof MakeMoveRequest) {
            // Handle MakeMoveRequest message
            MakeMoveRequest makeMoveRequest = (MakeMoveRequest) message;
            handleMakeMoveRequest(makeMoveRequest, playerId);
        } else if (message instanceof PlayAgainRequest) {
            // Handle PlayAgainRequest message
            PlayAgainRequest playAgainRequest = (PlayAgainRequest) message;
            handlePlayAgainRequest(playAgainRequest, playerId);
        } else if(message instanceof ExitRequest)
        {
            ExitRequest exitRequest = (ExitRequest) message;
        }
    }

    private void handleMakeMoveRequest(MakeMoveRequest message, String playerId) {
        int index = message.getGameMove();
        int[] gamemove = convertTo2DMove(index);
        int row = gamemove[0];
        int col = gamemove[1];
        boolean success = boardLogic.makeMove(playerId, row, col);

        if (success) {
            // Send updated board state to other player
            // String[][] cells = boardLogic.getCells();
            String otherPlayerId = getOtherPlayerId(playerId);
            int move = message.getGameMove();
            MakeMoveResponse response = new MakeMoveResponse(playerId, move);
            client.sendMessage(otherPlayerId, "move_response", response);

            String winner = boardLogic.getWinner();

            if (winner != null) {
                // Player has won, perform game over actions
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handleGameOver(playerId, "WIN");

            } else if (boardLogic.isGameOver()) {
                // The game is a tie
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                handleGameOver("", "TIE");
            }
        } else {
            // Send error response to current player
            ErrorResponse response = new ErrorResponse(playerId, "Invalid move");
            client.sendMessage(playerId, "error_response", response);
        }
    }

    private void handlePlayAgainRequest(PlayAgainRequest message, String playerId) {
        if (playerId.equals(playerIds.get(0))) {
            boolean playAgain = message.getPlayAgain();

            if (playAgain) {
                // Play again requested
                client.sendMessage(getOtherPlayerId(playerId), "play_again", message);
                System.out.println("Calling reset");
                boardLogic.reset();
                // Reset the board and restartRequested flag
                // restartRequested = false;
            } else {
                // Play again not requested
                PlayAgainRequest response = new PlayAgainRequest(playerId, false);
                client.sendMessage(playerId, "play_again", response);
            }
        }
    }

    private void handleGameOver(String winner, String gameResult) {
        // Perform actions for game over
        for (String playerId : playerIds) {
            // Send GameOverMessage to each player
            GameOverMessage message = new GameOverMessage(playerId, gameResult);
            client.sendMessage(playerId, "game_over", message);
            boardLogic.reset();

            // if (restartRequested) {
            //     // If restart requested, send PlayAgainRequest to each player
            //     PlayAgainRequest playAgainRequest = new PlayAgainRequest(playerId, true);
            //     client.sendMessage(playerId, "play_again", playAgainRequest);
            //     boardLogic.reset();
            // }
        }
        // // Reset the board and restartRequested flag
        // restartRequested = false;
    }

    private String getOtherPlayerId(String playerId) {
        // Get the ID of the other player
        for (String id : playerIds) {
            if (!id.equals(playerId)) {
                return id;
            }
        }
        return null;
    }

    public static int[] convertTo2DMove(int index) {
        // Convert a 1D move index to 2D row and column indices
        int row = index / 3;
        int col = index % 3;
        return new int[] { row, col };
    }
}