package com.example;

import java.util.concurrent.TimeUnit;

import com.example.client.TicTacToeClient;
import com.example.messages.ExitRequest;
import com.example.messages.GameOverMessage;
import com.example.messages.MakeMoveRequest;
import com.example.messages.MakeMoveResponse;
import com.example.messages.PlayAgainRequest;
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
            try {
                TimeUnit.MILLISECONDS.sleep(200);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if (client.hasUnreadMessages()) {
                Platform.runLater(() -> {

                    Message message = client.getLatestMessage();
                    if (message instanceof MakeMoveResponse) {
                        MakeMoveResponse moveMessage = (MakeMoveResponse) message;
                        boardController.processMove(moveMessage);
                    }
                    else if(message instanceof PlayAgainRequest)
                    {
                        PlayAgainRequest restartMessage = (PlayAgainRequest) message;
                        boardController.processRestart(restartMessage);
                    }
                    else if(message instanceof ExitRequest)
                    {
                        ExitRequest exitMessage = (ExitRequest) message;
                        boardController.processExit(exitMessage);
                    }
                    else if (message instanceof GameOverMessage) {
                        GameOverMessage gameOverMessage = (GameOverMessage) message;
                        boardController.processWin(gameOverMessage);
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