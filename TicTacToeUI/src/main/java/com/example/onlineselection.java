package com.example;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;

import javax.imageio.ImageIO;

import com.example.client.TicTacToeClient;
import com.example.messages.ClientInfoMessage;
import com.example.messages.StartGameRequest;
import com.example.router.Message;

import java.awt.image.BufferedImage;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class onlineselection {

    private Stage stage;
    private Scene scene;
    private Parent root;
    ArrayList<Image> images ;
    private Image player1Avatar;
    private int player1AvatarIndex;
    private String player1Name;
    private int p1arraylocation = 0;
    @FXML
    private TextField player1name;
    @FXML 
    private ImageView player1Image;
    @FXML
    private Label players;
    @FXML
    private Button readyPlayer;
    @FXML
    private Button player1Left;
    @FXML
    private Button player1Right;
    @FXML
    private Button UploadAvatarP1;

    private TicTacToeClient c;
    private String boardID;

    playerdata data = playerdata.getInstance();

    // using this 
    private FileChooser fileChooser;
    private File filePath;
        
    public void setClient(TicTacToeClient client, String boardID) throws IOException
    {
        this.c = client;
        this.boardID = boardID;
    }

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
        lockUI();
        c.sendMessage(boardID, "start_game", new StartGameRequest(c.getID(), true));
        c.sendMessage(boardID, "client_info", new ClientInfoMessage(c.getID(), player1Name, player1AvatarIndex));
    
        Thread messageCheckingThread = new Thread(() -> {
            while (true) {
                if (c.numUnreadMessages() > 1) {
                    Message newMessage = c.getLatestMessage();
    
                    if (newMessage instanceof StartGameRequest) {
                        Platform.runLater(() -> {
                            if(c.hasUnreadMessages())
                            {
                                Message newMessage1 = c.getLatestMessage();
                                if(newMessage1 instanceof ClientInfoMessage)
                                {
                                    ClientInfoMessage clientInfoMessage = (ClientInfoMessage) newMessage1;
                                    data.setPlayerNames(player1Name, clientInfoMessage.getUsername());
                                    data.setPlayerAvatareInts(player1AvatarIndex, clientInfoMessage.getProfilePic());
                                    data.setPlayerIDs(c.getID(), clientInfoMessage.getSenderID());

                                    try {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("onlineboard.fxml"));root = loader.load();
                                    
                                    onlineboard onlineboardController = loader.getController();
                                    onlineboardController.setClient(c, boardID);

                                    stage = (Stage) ((Node) (event.getSource())).getScene().getWindow();
                                    scene = new Scene(root);
                                    stage.setScene(scene);
                                    stage.centerOnScreen();
                                    stage.show();
                                    } catch (IOException e) {
                                        // TODO Auto-generated catch block
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });
                    }
                }
    
                try {
                    Thread.sleep(100); // Adjust the sleep time as needed
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    
        messageCheckingThread.setDaemon(true);
        messageCheckingThread.start();
    }

    public boolean checkAvatar()throws IOException
    {
        // if(player1Avatar ==player2Avatar)
        // {
        //     //PopupWindow.display("Unable to Start", "Players must have different Avatars to start game.");
        //     return false;
        // }
        return true;
    }

    public void initAvatar()
    {
        data.initAvatar();
        images = data.images;
        // images = new ArrayList<>(Arrays.asList(gigachad, soyjack, mittens, pika, jack, bubble, monkey));
        player1Avatar= images.get(0);
    }

    public void changeAvatar(ActionEvent event) throws IOException {
        // strings representing the left and right buttons for both players
        String p1Left = "Button[id=player1Left, styleClass=button]'<--'";
        String p1Right = "Button[id=player1Right, styleClass=button]'-->'";
        String button = event.getSource().toString();
        int imgCount = images.size();
    
        // check which button was pressed
        boolean isP1Left = p1Left.equals(button);
        boolean isP1Right = p1Right.equals(button);
    
        ImageView playerImage = null;
        int arrayLocation = 0;
        // determine which player's avatar needs to be changed
        if (isP1Left || isP1Right) {
            playerImage = player1Image;
            arrayLocation = p1arraylocation;

        }
    
        // return if no player's avatar needs to be changed
        if (playerImage == null) {
            return;
        }
    
        int nextIndex = arrayLocation;
        // calculate the next index based on the button that was pressed
        if (isP1Left) {
            nextIndex = (arrayLocation - 1 + imgCount) % imgCount;
        } else if (isP1Right) {
            nextIndex = (arrayLocation + 1) % imgCount;
        }
    
        // update the player's avatar
        if (isP1Left || isP1Right) {
            p1arraylocation = nextIndex;
            player1Avatar = images.get(p1arraylocation);
        }
    
        // set the updated avatar images
        player1Image.setImage(player1Avatar);
    }    
    
    public boolean checkName()throws IOException
    {
        // if(player1Name.equals(player1Name))
        // {
        //     //PopupWindow.display("Unable to Start", "Players must have a name to start the game.");
        //     return false;
        // }
        return true;
    }

    public void checkNull()throws IOException
    {

        if(player1name.getText() == "")
        {
            player1name.setText("Player " + c.getID());
        }

    }

    public void setPlayerName()throws IOException
    {
        checkNull();
        player1Name = player1name.getText();
        //players.setText(player1Name);
    }

    public void setAvatar()throws IOException
    {
        player1AvatarIndex = p1arraylocation;
    }

    public void lockUI()throws IOException
    {
        player1name.setDisable(true);
        player1Left.setDisable(true);
        player1Right.setDisable(true);
        readyPlayer.setDisable(true);
        //UploadAvatarP1.setDisable(true);
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
            String uploadP1Avater = "Button[id=UploadAvatarP1, styleClass=button]'Upload Avatar'";
            String uploadP2Avater = "Button[id=UploadAvatarP2, styleClass=button]'Upload Avatar'";
            String button = event.getSource().toString();

            if(uploadP1Avater.equals(button)){
                player1Image.setImage(image);
                this.player1Avatar = player1Image.getImage();  
            }      
        } catch(IOException e){
            System.err.println(e.getMessage());
        }
    } 
    

}


