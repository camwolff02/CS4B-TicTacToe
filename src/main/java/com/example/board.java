package com.example;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

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
    private Label winnerText;

    @FXML 
    private ImageView playerImage;
    

    private int playerTurn = 0;

    ArrayList<ImageView> imageButtons;
    ArrayList<Button> buttons;

    playerdata data = playerdata.getInstance();

    ImageView view;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttons = new ArrayList<>(Arrays.asList(button1,button2,button3,button4,button5,button6,button7,button8,button9));

        if(playerTurn == 0)
        {
            winnerText.setText(data.getP1Name() + "'s turn");
            playerImage.setImage(data.getP1Avatar());

        }
        else
        {
            winnerText.setText(data.getP2Name() + "'s turn");
            playerImage.setImage(data.getP2Avatar());
        }

        buttons.forEach(button ->{
            setupButton(button);
            button.setFocusTraversable(false);
        });
        
    }

    @FXML
    void restartGame(ActionEvent event) {
        buttons.forEach(this::resetButton);
        winnerText.setText("Tic-Tac-Toe");
        playerTurn = 0;
        if(playerTurn == 0)
        {
            winnerText.setText(data.getP1Name() + "'s turn");
            playerImage.setImage(data.getP1Avatar());

        }
        else
        {
            winnerText.setText(data.getP2Name() + "'s turn");
            playerImage.setImage(data.getP2Avatar());
        }
    }

    public void resetButton(Button button){
        button.setDisable(false);
        ImageView view = new ImageView();
        button.setGraphic(view);
    }
    

   
    public void backtoMenu(ActionEvent event) throws IOException{
       
        root  = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }

    private void setupButton(Button button) {
        button.setOnMouseClicked(mouseEvent -> {
            setPlayerSymbol(button);
            button.setDisable(true);
            
            if(playerTurn == 0)
            {
                winnerText.setText(data.getP1Name() + "'s turn");
                playerImage.setImage(data.getP1Avatar());
    
            }
            else
            {
                winnerText.setText(data.getP2Name() + "'s turn");
                playerImage.setImage(data.getP2Avatar());
            }
            checkIfGameIsOver();
        });
    }

    public void setPlayerSymbol(Button button){
        if(playerTurn % 2 == 0){
            ImageView view = new ImageView(data.getP1Avatar());
            view.setFitHeight(80);
            view.setFitWidth(80);
            button.setGraphic(view);
            playerTurn = 1;
        } else{
            ImageView view = new ImageView(data.getP2Avatar());
            view.setFitHeight(80);
            view.setFitWidth(80);
            button.setGraphic(view);
            playerTurn = 0;
        }
    }

    public void checkIfGameIsOver(){
        int count =0;
        for(Button button : buttons)
        {
            boolean checkbutton = button.isDisable();
            if(checkbutton == true)
            {
                count++;
            }

        }
        if(count ==9)
        {
            
            winnerText.setText("Game over\n\nTie game");
        }
 

            // //X winner
            // if (line.equals("XXX")) {
            //     winnerText.setText("X won!");
            // }

            // //O winner
            // else if (line.equals("OOO")) {
            //     winnerText.setText("O won!");
            // }
        }
    }
