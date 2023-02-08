package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Stage;

public class playerselection {

    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Image gigachad = new Image("file:src/images/gigachad.png");
    @FXML
    private Image soyjack  = new Image("file:src/images/soyjack.png");
    @FXML
    private Image mittens  = new Image("file:src/images/mittens.jpg");
    @FXML
    private Image pika     = new Image("file:src/images/pika.jpg");
    @FXML
    private Image jack     = new Image("file:src/images/jackhorner.jpg");
    ArrayList<Image> images ;
    
    @FXML
    private TextField player1Text;
    @FXML
    private TextField player2Text;

    Data data1 = Data.getInstance();
    
    private Image player1Avatar;
    private Image player2Avatar;
    private String player1Name;
    private String player2Name;
    

    public void goback(ActionEvent event) throws IOException
    {
        root  = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void startGame(ActionEvent event) throws IOException
    {
        setPlayerName();
        initAvatar();
        if(checkAvatar()== false)
        {
            PopupWindow.display("Unable to Start", "Players must have different Avatars to start game.");
        }
        else if(checkName() == false)
        {
            PopupWindow.display("Unable to Start", "Players must have different names to start.");
        }
        else if(checkNameSize() == false)
        {
            PopupWindow.display("Unable to Start", "Players must be less than 15 characters.");
        }
        else
        {
            root  = FXMLLoader.load(getClass().getResource("board.fxml"));
            stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
      
    }

    public boolean checkAvatar()throws IOException
    {
        if(player1Avatar ==player2Avatar)
        {
            //PopupWindow.display("Unable to Start", "Players must have different Avatars to start game.");
            return false;
        }
        return true;
    }

    public boolean checkNameSize()throws IOException
    {
        if(player1Name.length() > 15 || player2Name.length() > 15)
        {
            //PopupWindow.display("Unable to Start", "Players must have a name to start the game.");
            return false;
        }
        return true;
    }

    public boolean checkName()throws IOException
    {
        if(player1Name.equals(player2Name))
        {
            //PopupWindow.display("Unable to Start", "Players must have a name to start the game.");
            return false;
        }
        return true;
    }

    public void checkNull()throws IOException
    {

        if(player1Text.getText() == "")
        {
            player1Text.setText("Player 1");
        }

        if(player2Text.getText() == "")
        {
            player2Text.setText("Player 2");
        }

    }

    public void initAvatar()throws IOException
    {
        images = new ArrayList<>(Arrays.asList(gigachad, soyjack, mittens, pika, jack));
        player1Avatar= images.get(0);
        player2Avatar = images.get(2);
    }

    public void changeAvatar(ActionEvent event)throws IOException
    {

    }

    public void setPlayerName() throws IOException
    {
        checkNull();
        player1Name = player1Text.getText();
        player2Name =  player2Text.getText();

        data1.setPlayerNames(player1Name, player2Name);
    }

}