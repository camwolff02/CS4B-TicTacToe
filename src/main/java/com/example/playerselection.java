package com.example;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Image player1Avatar;
    private Image player2Avatar;
    private String player1Name;
    private String player2Name;
    @FXML
    private TextField player1name;
    @FXML
    private TextField player2name;
    @FXML 
    private ImageView player1Image;
    @FXML 
    private ImageView player2Image;
    

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
        //setPlayerName(event);
        
 
        initAvatar();
        if(checkAvatar()== false)
        {
            PopupWindow.display("Unable to Start", "Players must have different Avatars to start game.");
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

    public void initAvatar()throws IOException
    {
        images = new ArrayList<>(Arrays.asList(gigachad, soyjack, mittens, pika, jack));
        player1Avatar= images.get(0);
        player2Avatar = images.get(2);
    }

    public void changeAvatar(ActionEvent event)throws IOException
    {
        String p1buttonleft = "Button[id=player1Left, styleClass=button]'<--'";
        String p1buttonright = "Button[id=player1Right, styleClass=button]'-->'";
        String p2buttonleft = "Button[id=player2Left, styleClass=button]'<--'";
        String p2buttonright = "Button[id=player2Right, styleClass=button]'-->'";
        String button = event.getSource().toString();
        int p1arraylocation = 0;
        int p2arraylocation = 2;

        // button1left.substring(button1left.length()-3,button1left.length());
        System.out.print(button);

        if(button == p1buttonleft ){
            player1Avatar = images.get(p1arraylocation-1);
            player1Image.setImage(player1Avatar);
            System.out.print("1st loop");
        }
        else if(button == p1buttonright){
            player1Avatar = images.get(p1arraylocation+1);
            player1Image.setImage(player1Avatar);
            System.out.print("secondloop");
        }
        else if(button == p2buttonleft){
            player2Avatar = images.get(p2arraylocation-1);
            player2Image.setImage(player2Avatar);
            System.out.print("sthree");
        }
        else if(button == p2buttonright){
            player2Avatar = images.get(p2arraylocation+1);
            player2Image.setImage(player2Avatar);
            System.out.print("4 loop");
        }
        System.out.println("Sugma");

    }
    public void setPlayerName(ActionEvent event)throws IOException
    {
        player1Name = player1name.getText();
        player2Name = player2name.getText();
        if(player1Name ==null ||player1Name =="")
        {
            player1Name = "Player 1";
            System.out.println(player1Name);
        }
        if(player2Name == null||player2Name =="")
        {
            player2Name = "Player 2";
            System.out.println(player2Name);
        }  
        
        System.out.println(player1Name);
        System.out.println(player2Name);
    }

}