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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class matchmaking implements Initializable{


    private Stage popup;
    private Stage menu;
    
    private Scene scene;
    private Parent root;
    private TicTacToeClient c;

    @FXML
    private Label actionLabel;

    @FXML
    private Button btn1;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
      updateMessage("Connecting to server");
      btn1.setVisible(false);
    }


    public void startClient() throws IOException
    {
        // Start client listener
        c = new TicTacToeClient();
        c.connect();
        c.start();
        updateMessage("Connected to server");
        btn1.setVisible(true);

    }

    private void updateMessage(String string) {
      Platform.runLater(() -> {
        actionLabel.setText(string);
    });
   }
    public void exitButton(ActionEvent event)throws IOException
    {
        Platform.exit();
    }

    @FXML
    public void joinQueue(ActionEvent event) {
      c.subscribeToChannel("join");
      c.sendMessage("join", "join_game", new JoinGameRequest(c.getID()));

      updateMessage("Searching for player...");
  
      popup = (Stage) ((Node) event.getSource()).getScene().getWindow();
  
      JoinQueueTask task = new JoinQueueTask(c, menu, popup);
      Thread thread = new Thread(task);
      thread.setDaemon(true);
      thread.start();
  }


    public void setMenuStage(Stage menuStage) {
      menu = menuStage;
      
    }
    
     
     

}