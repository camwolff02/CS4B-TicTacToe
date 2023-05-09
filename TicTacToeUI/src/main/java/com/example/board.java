package com.example;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import com.example.messages.*;

class BoardUpdater extends Thread {
    private TicTacToeClient client;
    private board boardController;

    public BoardUpdater(TicTacToeClient client, board boardController) {
        this.client = client;
        this.boardController = boardController;
    }

    @Override
    public void run() {
        while (true) {
            if (client.hasUnreadMessages()) {
                Message message = client.getLatestMessage();
                if (message != null) {
                    Platform.runLater(() -> {
                        boardController.processMessage(message);
                    });
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class board implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private Button button3;
    @FXML
    private Button button4;
    @FXML
    private Button button5;
    @FXML
    private Button button6;
    @FXML
    private Button button7;
    @FXML
    private Button button8;
    @FXML
    private Button button9;

    @FXML
    private Button restartGame;
   
    @FXML
    private ImageView xImageViewPfp;
    @FXML
    private ImageView oImageViewPfp;
    @FXML
    private ImageView gameStateImage;

    @FXML
    private ImageView image1;
    @FXML
    private ImageView image2;
    @FXML
    private ImageView image3;
    @FXML
    private ImageView image4;
    @FXML
    private ImageView image5;
    @FXML
    private ImageView image6;
    @FXML
    private ImageView image7;
    @FXML
    private ImageView image8;
    @FXML
    private ImageView image9;

    @FXML
    private Label scoreText;
    @FXML
    private Label gameStateText;

    @FXML
    private Text playerXName;
    @FXML
    private Text playerOName;

    private ArrayList<Button> buttons;
    private ArrayList<ImageView> images;

    private int xBoardMask;
    private int oBoardMask;
    private int winningBoardMasks[];

    private boolean playerXTurn;
    private boolean imageMode;

    private Image playerXImage;
    private Image playerOImage;

    private int xWinCount;
    private int oWinCount;
    private int tieCount;
    private int moveCount;

    private String channel;
    private String type;
    private Message message;
    private BoardUpdater boardUpdater;
    private TicTacToeClient c;

    playerdata data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = playerdata.getInstance();
        
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        images  = new ArrayList<>(Arrays.asList(image1,image2,image3,image4,image5,image6,image7,image8,image9));

        playerXName.setText(data.getP1Name());
        playerOName.setText(data.getP2Name());

        playerXImage = data.getP1Avatar();
        playerOImage = data.getP2Avatar();
        
        gameStateImage.setImage(playerXImage);
        xImageViewPfp.setImage(playerXImage);
        oImageViewPfp.setImage(playerOImage);

        winningBoardMasks = new int[] {
            0b111_000_000,  // top row win
            0b000_111_000,  // middle row win
            0b000_000_111,  // bottom row win
            0b100_100_100,  // left column win
            0b010_010_010,  // middle column win
            0b001_001_001,  // right column win
            0b001_010_100,  // forward slash win
            0b100_010_001   // backward slash win
        };

        for (var button : buttons) {
            setupButton(button);
            resetButton(button);
        }

        xBoardMask = 0b000_000_000;
        oBoardMask = 0b000_000_000;
    
        playerXTurn = true;
        imageMode = false;

        xWinCount = 0;
        oWinCount = 0;
        tieCount  = 0;
        moveCount = 0; 
        
        // start client listener
        c = new TicTacToeClient();
        c.start();

             // wait for connection
             while (!c.isConnected()) {
                System.out.println("[INFO] Waiting for connection...");
                try {
                    TimeUnit.SECONDS.sleep(1);        
                } catch (InterruptedException ex) {}
            }
    
            System.out.println("[SUCCESS] Client Connected to server");

        channel = "game";
        type = "subscribe";

        message = new SubscribeRequest(channel);

        Packet packet = new Packet(channel, type, message);

        c.sendPacket(packet);

        boardUpdater = new BoardUpdater(c, this);
        boardUpdater.start();

    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        gameStateText.setText("  Player Turn: X");
        updateScoreText();
        restartGame.setText("Restart");

        
        hideAllImages(true);
        gameStateImage.setImage(playerXImage);
        if (imageMode) gameStateImage.setVisible(true);
        
        playerXTurn = true;
        moveCount   = 0;
        xBoardMask  = 0;
        oBoardMask  = 0;
    }

    @FXML
    void showSymbolOnHover(MouseEvent event) {
        String buttonID = ((Button)event.getSource()).getId();
        int index = Integer.parseInt(String.valueOf(buttonID.charAt(buttonID.length()-1)))-1;

        if (!buttons.get(index).isFocusTraversable()) return;
        
        if (playerXTurn) 
            buttons.get(index).setText("X");
        else 
            buttons.get(index).setText("O");
    }

    @FXML
    void hideSymbolOnHover(MouseEvent event) {
        String buttonID = ((Button)event.getSource()).getId();
        int index = Integer.parseInt(String.valueOf(buttonID.charAt(buttonID.length()-1)))-1;

        if (buttons.get(index).isFocusTraversable()) 
            buttons.get(index).setText("");
    }

    @FXML
    void changeDisplayMode(ActionEvent event) {
        if (imageMode) {  // turn off image mode
            hideAllImages(false);
            gameStateImage.setVisible(false);

            ((ToggleButton) event.getSource()).setText("Toggle Image Mode");
        }
        else {  // turn on image mode    
            for (var image : images) 
                if (image.getImage() != null)
                    image.setVisible(true);
            
            gameStateImage.setVisible(true);

            ((ToggleButton) event.getSource()).setText("Toggle Classic Mode");
        }

        imageMode = !imageMode;
    }

    public void updateScoreText() {
        scoreText.setText(String.format("%d - %d - %d", xWinCount, tieCount, oWinCount));
    }

    public void resetButton(Button button){
        button.setFocusTraversable(true);
        button.setMouseTransparent(false);
        button.setText("");
    }

    public void backtoMenu(ActionEvent event) throws IOException{
        root  = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setMouseTransparent(true);
            button.setFocusTraversable(false);        
        
            moveCount++;
        
            checkGameOver();
        });
    }

    public void lockAllButtons() {
        for (var button : buttons) {
            button.setMouseTransparent(true);
            button.setFocusTraversable(false);   
        }
    }

    public void hideAllImages(boolean clear) {
        for (var image : images) {
            image.setVisible(false);
            if (clear) image.setImage(null);
        }
    }

    public void setPlayerSymbol(Button button){
        String buttonID = button.getId();
        int index = Integer.parseInt(String.valueOf(buttonID.charAt(buttonID.length()-1)))-1;



        button.setText("X");
        gameStateText.setText("  Player Turn: O");
        gameStateImage.setImage(playerOImage);

        xBoardMask += Math.pow(2, index);  

        images.get(index).setImage(playerXImage);

        if (imageMode) 
        {
            images.get(index).setVisible(true);
        }


        channel = "game";
        type = "make_move";

        String index1 = Integer.toString(index);
        message = new MakeMoveRequest(channel, type, index1);

        Packet packet = new Packet(channel, type, message);

        c.sendPacket(packet);

        
    }

    public void checkGameOver() {        
        for (var boardMask : winningBoardMasks) {
            if ((xBoardMask & boardMask) == boardMask) {
                xWinCount++;
                gameStateText.setText("Winner! Player X");
                gameStateImage.setImage(playerXImage);

                updateScoreText();
                lockAllButtons();

                restartGame.setText("Play Again");

                return;
            }
            if ((oBoardMask & boardMask) == boardMask) {
                oWinCount++;
                gameStateText.setText("Winner! Player O");
                gameStateImage.setImage(playerOImage);

                updateScoreText();  
                lockAllButtons();

                restartGame.setText("Play Again");
            
                return;
            }
        }
        
        if (moveCount >= 9) {
            tieCount++;
            updateScoreText();
            gameStateText.setText("Tie Game");
            gameStateImage.setVisible(false);

            restartGame.setText("Play Again");
        }
    }

    public void processMessage(Message message) {
        MakeMoveRequest moveRequest = (MakeMoveRequest) message;
        String index = moveRequest.getGameMove();

        int num = Integer.parseInt(index);

        gameStateText.setText("User sent message");
  
        System.out.println("Recieved");
    }

}