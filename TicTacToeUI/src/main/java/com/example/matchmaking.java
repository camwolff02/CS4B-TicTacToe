package com.example;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.client.TicTacToeClient;
import com.example.messages.JoinGameRequest;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class matchmaking implements Initializable{


    private Stage stage;
    
    private Scene scene;
    private Parent root;
    private TicTacToeClient c;

    @FXML
    private Label actionLabel;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // Start client listener
        c = new TicTacToeClient();
        c.connect();
        c.start();

        updateMessage("Connected to server");



    }

    private void updateMessage(String string) {
      actionLabel.setText(string);
   }
    public void exitButton(ActionEvent event)throws IOException
    {
        Platform.exit();
    }

    @FXML
    public void joinQueue(ActionEvent event) {
      c.subscribeToChannel("join");
      c.sendMessage("join", "join_game", new JoinGameRequest(c.getID()));
  
      stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
  
      JoinQueueTask task = new JoinQueueTask(c, stage);
      Thread thread = new Thread(task);
      thread.setDaemon(true);
      thread.start();
  }
    
     
     

}