package com.example;

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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.ResourceBundle;

public class singleboard implements Initializable {

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
    private Random random;

    playerdata data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = playerdata.getInstance();
        random = new Random();
        
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

        if (playerXTurn) {
            button.setText("X");
            gameStateText.setText("  Player Turn: O");
            gameStateImage.setImage(playerOImage);

            xBoardMask += Math.pow(2, index);  

            images.get(index).setImage(playerXImage);

            if (imageMode) 
                images.get(index).setVisible(true);  
        }
        else { 
            button.setText("O");
            gameStateText.setText("  Player Turn: X");
            gameStateImage.setImage(playerXImage);

            oBoardMask += Math.pow(2, index);

            images.get(index).setImage(playerOImage);

            if (imageMode) 
                images.get(index).setVisible(true);
        }

        playerXTurn = !playerXTurn;
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

        if(!playerXTurn)
        {
            int computerMove = makeComputerMove();
            Button computerButton = buttons.get(computerMove);
            setPlayerSymbol(computerButton);
            computerButton.setMouseTransparent(true);
            computerButton.setFocusTraversable(false);
            checkGameOver();
            playerXTurn = true;
        }
    }


    private int makeComputerMove() {
        // Generate a random move until finding an available button
        while (true) {
            int randomIndex = random.nextInt(9);
            Button randomButton = buttons.get(randomIndex);
            if (randomButton.getText().isEmpty()) {
                return randomIndex;
            }
        }
    }
}
