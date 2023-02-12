package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;

import javafx.embed.swing.SwingFXUtils;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class playerselection {

    private Stage stage;
    private Scene scene;
    private Parent root;
    // @FXML
    // private Image gigachad = new Image("file:src/images/gigachad.png");
    // @FXML
    // private Image soyjack  = new Image("file:src/images/soyjack.png");
    // @FXML
    // private Image mittens  = new Image("file:src/images/mittens.jpg");
    // @FXML
    // private Image pika     = new Image("file:src/images/pika.jpg");
    // @FXML
    // private Image jack     = new Image("file:src/images/jackhorner.jpg");
    // @FXML
    // private Image bubble     = new Image("file:src/images/xbox360Bubble.jpg");
    // @FXML
    // private Image monkey     = new Image("file:src/images/xbox360Monkey.jpg");
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

    // using this 
    private FileChooser fileChooser;
    private File filePath;
        
    

    public void goback(ActionEvent event) throws IOException
    {
        root  = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
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
            stage.centerOnScreen();
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
        data.initAvatar();
        images = data.images;
        // images = new ArrayList<>(Arrays.asList(gigachad, soyjack, mittens, pika, jack, bubble, monkey));
        player1Avatar= images.get(0);
        player2Avatar = images.get(2);
    }

    public void changeAvatar(ActionEvent event) throws IOException {
        // strings representing the left and right buttons for both players
        String p1Left = "Button[id=player1Left, styleClass=button]'<--'";
        String p1Right = "Button[id=player1Right, styleClass=button]'-->'";
        String p2Left = "Button[id=player2Left, styleClass=button]'<--'";
        String p2Right = "Button[id=player2Right, styleClass=button]'-->'";
        String button = event.getSource().toString();
        int imgCount = images.size();
    
        // check which button was pressed
        boolean isP1Left = p1Left.equals(button);
        boolean isP1Right = p1Right.equals(button);
        boolean isP2Left = p2Left.equals(button);
        boolean isP2Right = p2Right.equals(button);
    
        ImageView playerImage = null;
        int arrayLocation = 0;
        // determine which player's avatar needs to be changed
        if (isP1Left || isP1Right) {
            playerImage = player1Image;
            arrayLocation = p1arraylocation;
        } else if (isP2Left || isP2Right) {
            playerImage = player2Image;
            arrayLocation = p2arraylocation;
        }
    
        // return if no player's avatar needs to be changed
        if (playerImage == null) {
            return;
        }
    
        int nextIndex = arrayLocation;
        // calculate the next index based on the button that was pressed
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
    
        // update the player's avatar
        if (isP1Left || isP1Right) {
            p1arraylocation = nextIndex;
            player1Avatar = images.get(p1arraylocation);
        } else if (isP2Left || isP2Right) {
            p2arraylocation = nextIndex;
            player2Avatar = images.get(p2arraylocation);
        }
    
        // set the updated avatar images
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

     // this function will let the player to upload their own image from their pc
     public void uploadUserImageButton(ActionEvent event){
        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();

        fileChooser = new FileChooser();
        fileChooser.setTitle("Open image");

        //set to user's directory or go to the default c dirve if don't have the access
        String userDirectoString = System.getProperty("user.home") + "\\Pictures";
        File userDirectory = new File(userDirectoString);

        // check it
        if(!userDirectory.canRead())
            userDirectory = new File("c:/");

        fileChooser.setInitialDirectory(userDirectory);

        this.filePath = fileChooser.showOpenDialog(stage);

        // update the image by loading the new image
        try{
            BufferedImage bufferedImage = ImageIO.read(filePath);
            Image image = SwingFXUtils.toFXImage(bufferedImage, null);

            // check which user wants to upload image for their avater
            String uploadAvater1 = "Button[id=UploadAvatarP1], styleClass=button]'Upload P1 Avatar'";
            String uploadP2Avater = "Button[id=UploadAvatarP2], styleClass=button]'Upload P2 Avatar'";
            String button = event.getSource().toString();

            if(uploadAvater1== button)
                player1Image.setImage(image);
                this.player1Avatar = player1Image.getImage();
            
            if(uploadP2Avater == button)
                player2Image.setImage(image);
                this.player2Avatar = player2Image.getImage();           
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
        
    }

}