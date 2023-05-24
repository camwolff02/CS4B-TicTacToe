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
import javafx.scene.effect.ColorAdjust;
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
import java.util.ResourceBundle;

import com.example.client.TicTacToeClient;
import com.example.messages.ExitRequest;
import com.example.messages.GameOverMessage;
import com.example.messages.MakeMoveRequest;
import com.example.messages.MakeMoveResponse;
import com.example.messages.PlayAgainRequest;
import com.example.router.Message;

public class onlineboard implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private TicTacToeClient c;
    private String boardID;
    private String player1Symbol;
    private String player2Symbol;
    private BoardUpdater boardUpdater;
    
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
    private ImageView p1ImageViewPfp;
    @FXML
    private ImageView p2ImageViewPfp;
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
    private Text player1Name;
    @FXML
    private Text player2Name;

    private ArrayList<Button> buttons;
    private ArrayList<ImageView> images;

    private boolean player1Turn;
    private boolean imageMode;

    private Image player1Image;
    private Image player2Image;

    private int p1WinCount;
    private int p2WinCount;
    private int tieCount;
    private ColorAdjust colorAdjust;

    playerdata data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        data = playerdata.getInstance();
        
        // Create a ColorAdjust effect and set the saturation property to -1 to make it grayscale
        colorAdjust = new ColorAdjust();
        colorAdjust.setSaturation(-1);
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));
        images  = new ArrayList<>(Arrays.asList(image1,image2,image3,image4,image5,image6,image7,image8,image9));

        player1Image = data.getP1Avatar();
        player2Image = data.getP2Avatar();

        for (var button : buttons) {
            setupButton(button);
            resetButton(button);
        }

        if(!data.usersTurn())
        {
            lockAllUnusedButtons();
            player1Turn = false;
            gameStateImage.setImage(player2Image);
            player1Symbol = "O";
            player2Symbol = "X";
            restartGame.setVisible(false);
            // restartGame.setMouseTransparent(true);
            // restartGame.setFocusTraversable(false);
        }
        else
        {
            player1Turn = true;
            gameStateImage.setImage(player1Image);
            player1Symbol = "X";
            player2Symbol = "O";
        }

        player1Name.setText(data.getP1Name() + " " + player1Symbol);
        player2Name.setText(data.getP2Name() + " " + player2Symbol);

        gameStateText.setText("  Player Turn: X");
        p1ImageViewPfp.setImage(player1Image);
        p2ImageViewPfp.setImage(player2Image);
    
        imageMode = false;

        p1WinCount = 0;
        p2WinCount = 0;
        tieCount  = 0;   

    }

    public void setClient(TicTacToeClient client, String boardID) throws IOException
    {
        this.c = client;
        this.boardID = boardID;

        boardUpdater = new BoardUpdater(c, this);
        boardUpdater.start();
    }

    @FXML
    void restartGame(ActionEvent event) {
        // tell other client to restart
        //
        //
        buttons.forEach(this::resetButton);
        updateScoreText();
        restartGame.setText("Restart");
        gameStateText.setText("  Player Turn: X");

        if(!data.usersTurn())
        {
            lockAllUnusedButtons();
            player1Turn = false;
            gameStateImage.setImage(player2Image);
        }
        else
        {
            player1Turn = true;
            gameStateImage.setImage(player1Image);
        }

        
        hideAllImages(true);
        if (imageMode) gameStateImage.setVisible(true);

        if(data.usersTurn())
        {
            c.sendMessage(boardID, "play_again", new PlayAgainRequest(c.getID(), true));
        }
        
    }

    @FXML
    void showSymbolOnHover(MouseEvent event) {
        String buttonID = ((Button)event.getSource()).getId();
        int index = Integer.parseInt(String.valueOf(buttonID.charAt(buttonID.length()-1)))-1;

        if (!buttons.get(index).isFocusTraversable()) return;
            buttons.get(index).setText(player1Symbol);
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
        scoreText.setText(String.format("%d - %d - %d", p1WinCount, tieCount, p2WinCount));
    }

    public void resetButton(Button button){
        button.setFocusTraversable(true);
        button.setMouseTransparent(false);
        button.setText("");
    }

    public void backtoMenu(ActionEvent event) throws IOException{
        // not going to focus on handling this if client clicks back to menu
        // both clients should go back and game is over
        // unsubscribe client from their board
        root  = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        c.sendMessage(boardID, "exit", new ExitRequest(c.getID()));
        c.unsubscribeFromChannel(boardID);
    }

    private void setupButton(Button button) {
        // send message to board of the move we are doing
        // also need area that process messages and disables the button
        // the other client pressed
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setMouseTransparent(true);
            button.setFocusTraversable(false);
            String buttonID = button.getId();
            int index = Integer.parseInt(String.valueOf(buttonID.charAt(buttonID.length()-1)))-1;
            c.sendMessage(boardID,"make_move" , new MakeMoveRequest(c.getID(),index));
        });
    }

    public void lockAllUnusedButtons() {
        for (var button : buttons) {
            if (button.getText().isEmpty()) {
                button.setMouseTransparent(true);
                button.setFocusTraversable(false);
            }
        }
    }

    public void unlockAllUnusedButtons() {
        for (var button : buttons) {
            if (button.getText().isEmpty()) {
                button.setMouseTransparent(false);
                button.setFocusTraversable(true);
            }
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

        if (player1Turn) {
            button.setText(player1Symbol);
            gameStateText.setText("  Player Turn: " + player2Symbol);
            gameStateImage.setImage(player2Image);

            images.get(index).setImage(player1Image);

            if (imageMode) 
            {
                images.get(index).setVisible(true);  
            }


            player1Turn = !player1Turn;
            lockAllUnusedButtons();
        }
        else
        {
            button.setText(player2Symbol);
            gameStateText.setText("  Player Turn: " + player1Symbol);
            gameStateImage.setImage(player1Image);


            images.get(index).setImage(player2Image);

            if (imageMode) 
                images.get(index).setVisible(true);   
        }  
    }

    public void processMove(MakeMoveResponse message) {
        String buttonId = "button" + Integer.toString(message.getGameMove()+1);
        for(Button button: buttons)
        {
            if(button.getId().equals(buttonId))
            {
                setPlayerSymbol(button);
                button.setMouseTransparent(true);
                button.setFocusTraversable(false);        
                
            }
        }

        player1Turn = !player1Turn;

        if(player1Turn)
        {
            unlockAllUnusedButtons();
        }
    }

    public void processWin(GameOverMessage message) {
        if(player1Turn)
        {
            lockAllUnusedButtons();
        }

        if(message.getGameMessage().equals("WIN")){
            if(!player1Turn)
            {
                gameStateText.setText("You win");
                p1WinCount++;
            }
            else{
                gameStateText.setText("You suck");
                p2WinCount++;
            }
        }
        else{
            gameStateText.setText("Cat game meow ");
            tieCount++;
        }

        updateScoreText();
        
    }

    public void processRestart(PlayAgainRequest restartMessage) {

        if(restartMessage.getPlayAgain())
        {
            restartGame(null);
        }

    }

    public void processExit(ExitRequest exitMessage) {
        p2ImageViewPfp.setEffect(colorAdjust);
        p2ImageViewPfp.setOpacity(0.5);
        player2Name.setEffect(colorAdjust);
        player2Name.setText("Disconnected");
        restartGame.setDisable(true);
        lockAllUnusedButtons();
    }
}
