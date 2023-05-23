package com.example;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.fxml.FXML;
import javafx.scene.image.Image;

public class playerdata {
    
    private static final playerdata instance = new playerdata();
    ArrayList<Image> images ;
    private Image player1Avatar;
    private Image player2Avatar;
    private String player1Name;
    private String player2Name;
    private int player1AvatarInt;
    private int player2AvatarInt;
    private String player1ID;
    private String player2ID;
    private int p1Wins;
    private int p2Wins;

    @FXML
    private Image gigachad = new Image("file:TicTacToeUI/src/images/gigachad.png");
    @FXML
    private Image soyjack  = new Image("file:TicTacToeUI/src/images/soyjack.png");
    @FXML
    private Image mittens  = new Image("file:TicTacToeUI/src/images/mittens.jpg");
    @FXML
    private Image pika     = new Image("file:TicTacToeUI/src/images/pika.jpg");
    @FXML
    private Image jack     = new Image("file:TicTacToeUI/src/images/jackhorner.jpg");
    @FXML
    private Image bubble     = new Image("file:TicTacToeUI/src/images/xbox360Bubble.jpg");
    @FXML
    private Image monkey     = new Image("file:TicTacToeUI/src/images/xbox360Monkey.jpg");
    
    public static playerdata getInstance(){
        return instance;
    }
  

    public String getP1Name()
    {
        return player1Name;
    }

    public String getP2Name()
    {
        return player2Name;
    }
    public String getP1ID()
    {
        return player1ID;
    }
    public String getP2ID()
    {
        return player2ID;
    }
    public Image getP1Avatar()
    {
        return player1Avatar;
    }
    public Image getP2Avatar()
    {
        return player2Avatar;
    }
    public int getP1AvatarInt()
    {
        return player1AvatarInt;
    }
    public int getP2AvatarInt()
    {
        return player2AvatarInt;
    }
    public int getP1Wins()
    {
        return p1Wins;
    }
    public int getP2Wins()
    {
        return p2Wins;
    }
    public boolean usersTurn()
    {
        if(Integer.parseInt(player1ID) < Integer.parseInt(player2ID))
        {
            return true;
        }

        return false;
    }


    public void setPlayerNames(String player1, String player2){
        this.player1Name = player1;
        this.player2Name = player2;
        System.out.println(player1);
    }

    public void setPlayerWins(int player1, int player2){
        this.p1Wins = player1;
        this.p2Wins = player2;
    }

    public void setPlayerAvatares(Image player1, Image player2){
        this.player1Avatar = player1;
        this.player2Avatar = player2;
    }

    public void setPlayerAvatareInts(int player1, int player2){
        this.player1AvatarInt = player1;
        this.player2AvatarInt = player2;

        player1Avatar= images.get(player1AvatarInt);
        player2Avatar = images.get(player2AvatarInt);
    }

    public void setPlayerIDs(String player1, String player2){
        this.player1ID = player1;
        this.player2ID = player2;
    }
    
    public void initAvatar()
    {
        images = new ArrayList<>(Arrays.asList(gigachad, soyjack, mittens, pika, jack, bubble, monkey));
        player1Avatar= images.get(0);
        player2Avatar = images.get(2);
    }

    //public getPlayerData()
}
