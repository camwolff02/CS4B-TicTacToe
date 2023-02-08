package com.example;

public class Data {
    
    private static final Data instance = new Data();

    private String player1N;

    private String player2N;

    public static Data getInstance(){
        return instance;
    }

    public String getPlayer1Name()
    {
        return player1N;
    }

    public String getPlayer2Name()
    {
        return player2N;
    }

    public void setPlayerNames(String player1, String player2){
        this.player1N = player1;
        this.player2N = player2;
    }

    public void printName()
    {
        System.out.println(player1N);
    }
    
}
