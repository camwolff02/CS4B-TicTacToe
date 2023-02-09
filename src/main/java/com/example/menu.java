package com.example;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class menu {

    private Stage stage;
    
    private Scene scene;
    private Parent root;
  
    // ImageView titleImage;

    // Image myImage = new Image(getClass().getResourceAsStream("file:src/images/pika.JPG"));
    // titleImage.setImage(myImage);
    public void onePlayerbutton(ActionEvent event) throws IOException
    {
        PopupWindow.display("Under Development", "Sorry, this feature is currently under development.");
        
    }
    
    public void exitButton(ActionEvent event)throws IOException
    {
        Platform.exit();
    }

    public void twoPlayerButton(ActionEvent event) throws IOException
    {
        //playerdata Playerdata = playerdata.getInstance();
        //root  = FXMLLoader.load(getClass().getResource("playerselection.fxml"));
        //EXAMPLE HOW TO PERSIST DATA THROUGH controllers
        FXMLLoader loader = new FXMLLoader(getClass().getResource("playerselection.fxml"));
        root = loader.load();
        playerselection playerselection = loader.getController();
        playerselection.initAvatar();
        //-----------------------------------------------------
        stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        // Playerdata.initAvatar();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void onlineButton(ActionEvent event) throws IOException
    {
        PopupWindow.display("Under Development", "Sorry, this feature is currently under development.");
    }

    // public void goback(ActionEvent event) throws IOException
    // {
        // root  = FXMLLoader.load(getClass().getResource("menu.fxml"));
        // stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        // scene = new Scene(root);
        // stage.setScene(scene);
        // stage.show();
    // }
   

    @FXML
    private Button btn1;

    @FXML
    private Button btn2;

    @FXML
    private Button btn3;

    @FXML
    private Button settingbtn;

}