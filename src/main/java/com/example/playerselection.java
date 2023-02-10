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
    private int p1arraylocation = 0;
    private int p2arraylocation = 2;
    @FXML
    private TextField player1name;
    @FXML
    private TextField player2name;
    @FXML 
    private ImageView player1Image;
    @FXML 
    private ImageView player2Image;

    playerdata data = playerdata.getInstance();
    

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
        setAvatar();
        
        
        
        if(checkAvatar()== false)
        {
            PopupWindow.display("Unable to Start", "Players must have different Avatars to start game.");
        }
        else if(checkName() == false)
        {
            PopupWindow.display("Unable to Start", "Players must have different names to start.");
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

    public void initAvatar()
    {
        images = new ArrayList<>(Arrays.asList(gigachad, soyjack, mittens, pika, jack));
        player1Avatar= images.get(0);
        player2Avatar = images.get(2);
    }

    public void changeAvatar(ActionEvent event) throws IOException {
        String p1Left = "Button[id=player1Left, styleClass=button]'<--'";
        String p1Right = "Button[id=player1Right, styleClass=button]'-->'";
        String p2Left = "Button[id=player2Left, styleClass=button]'<--'";
        String p2Right = "Button[id=player2Right, styleClass=button]'-->'";
        String button = event.getSource().toString();
        int imgCount = images.size();
        
        boolean isP1Left = p1Left.equals(button);
        boolean isP1Right = p1Right.equals(button);
        boolean isP2Left = p2Left.equals(button);
        boolean isP2Right = p2Right.equals(button);
    
        ImageView playerImage = null;
        int arrayLocation = 0;
        if (isP1Left || isP1Right) {
            playerImage = player1Image;
            arrayLocation = p1arraylocation;
        } else if (isP2Left || isP2Right) {
            playerImage = player2Image;
            arrayLocation = p2arraylocation;
        }
    
        if (playerImage == null) {
            return;
        }
        
        int nextIndex = arrayLocation;
        if (isP1Left || isP2Left) {
            nextIndex = (arrayLocation - 1 + imgCount) % imgCount;
            if (nextIndex == p2arraylocation || nextIndex == p1arraylocation) {
                nextIndex = (arrayLocation - 2 + imgCount) % imgCount;
            }
        } else if (isP1Right || isP2Right) {
            nextIndex = (arrayLocation + 1) % imgCount;
            if (nextIndex == p2arraylocation || nextIndex == p1arraylocation) {
                nextIndex = (arrayLocation + 2) % imgCount;
            }
        }
    
        if (isP1Left || isP1Right) {
            p1arraylocation = nextIndex;
            player1Avatar = images.get(p1arraylocation);
        } else if (isP2Left || isP2Right) {
            p2arraylocation = nextIndex;
            player2Avatar = images.get(p2arraylocation);
        }

        player1Image.setImage(player1Avatar);
        player2Image.setImage(player2Avatar);
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

        if(player1name.getText() == "")
        {
            player1name.setText("Player 1");
        }

        if(player2name.getText() == "")
        {
            player2name.setText("Player 2");
        }

    }

    public void setPlayerName()throws IOException
    {
        checkNull();
        player1Name = player1name.getText();
        player2Name =  player2name.getText();

        data.setPlayerNames(player1Name, player2Name);
    }

    public void setAvatar()throws IOException
    {
        data.setPlayerAvatares(player1Avatar, player2Avatar);
    }

}