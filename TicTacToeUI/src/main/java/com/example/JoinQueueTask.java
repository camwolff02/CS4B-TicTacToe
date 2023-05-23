package com.example;

import java.io.IOException;

import com.example.client.TicTacToeClient;
import com.example.router.IDMessage;
import com.example.router.Message;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class JoinQueueTask extends Task<Void> {
    private TicTacToeClient c;
    private Stage stage;
    private Stage popup;

    public JoinQueueTask(TicTacToeClient c, Stage stage, Stage popup) {
        this.c = c;
        this.stage = stage;
        this.popup = popup;
    }

    @Override
    protected Void call() throws Exception {
        while (true) {
            if (isCancelled()) {
                break;
            }

            if (c.hasUnreadMessages()) {
                Message newMessage = c.getLatestMessage();

                if (newMessage instanceof IDMessage) {
                    IDMessage idMessage = (IDMessage) newMessage;
                    String boardID = idMessage.getID();

                    Platform.runLater(() -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("onlineSelection.fxml"));
                            Parent root = loader.load();

                            onlineselection onlineSelectionController = loader.getController();
                            onlineSelectionController.setClient(c, boardID);
                            onlineSelectionController.initAvatar();

                            Scene scene = new Scene(root);
                            stage.setScene(scene);
                            stage.centerOnScreen();
                            stage.show();
                            popup.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });

                    c.subscribeToChannel(boardID);
                    c.unsubscribeFromChannel("join");
                    break;
                }
            }

            Thread.sleep(1000); // Adjust the sleep duration as needed
        }

        return null;
    }
}
