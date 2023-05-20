package com.example;

import com.example.client.TicTacToeClient;
import com.example.messages.GameOverMessage;
import com.example.messages.MakeMoveRequest;
import com.example.messages.MakeMoveResponse;
import com.example.router.Message;

import javafx.application.Platform;

class BoardUpdater extends Thread {
    private TicTacToeClient client;
    private onlineboard boardController;

    public BoardUpdater(TicTacToeClient client, onlineboard boardController) {
        this.client = client;
        this.boardController = boardController;
    }

    @Override
    public void run() {
        while (true) {
            if (client.hasUnreadMessages()) {
                Platform.runLater(() -> {
                if(client.numUnreadMessages() > 1)
                {
                    Message message = client.getLatestMessage();
                    if (message instanceof MakeMoveResponse) {
                        MakeMoveResponse moveMessage = (MakeMoveResponse) message;
                        boardController.processMove(moveMessage);

                    }

                    message = client.getLatestMessage();
                    if (message instanceof GameOverMessage) {
                        GameOverMessage gameOverMessage = (GameOverMessage) message;
                        boardController.processWin(gameOverMessage);
                    }

                }
                else if(client.numUnreadMessages() == 1)
                {
                    Message message = client.getLatestMessage();
                    if (message instanceof MakeMoveResponse) {
                        MakeMoveResponse moveMessage = (MakeMoveResponse) message;
                        boardController.processMove(moveMessage);
                    }
                    
                }
            });

            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}