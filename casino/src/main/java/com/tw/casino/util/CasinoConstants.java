package com.tw.casino.util;

public class CasinoConstants
{
    public static final String STARTUP_SERVER = "Provide port as: server <port>";
    public static final String STARTUP_DEALER = "Provide host and port as: dealer <host> <port>";
    public static final String STARTUP_PLAYER = "Provide host and port as: player <host> <port>";
    
    public static final String WELCOME = "Welcome to the Casino.";
    public static final String GAME_LIST_AVAILABLE = "You may choose from the following games.";
    
    // Player set up
    public static final String PLAYER_START = "To begin, please enter your starting "
            + "account balance as a decimal number:";
    public static final String PLAYER_RE_ENTER = "Invalid amount, please enter a decimal value.";
    public static final String PLAYER_REDO = "Invalid choice. Please enter a choice as stated.";
    public static final String PLAYER_AWAIT = "Waiting for more players to join.";
    public static final String PLAYER_REJECT = "Request to play game has been denied.";
    public static final String PLAYER_INSUFFICIENT_FUNDS = 
            "No more funds to play. Please update your balance";
    public static final String PLAYER_CONGRATULATIONS = "Congratulations! You win $";
    public static final String PLAYER_REGRET = "Sorry! You didn't win this time.";
    public static final String PLAYER_BALANCE_ADD = 
            "Enter the amount you wish to add as a decimal number: ";
    public static final String PLAYER_BALANCE = "Account Balance: " ;
    
    // Game Strategy Names
    public static final String ALWAYS_SHARP = "Always Sharp";
    public static final String RANDOM_GUESSING = "Random Guessing";
    

    
    // Dealer
    public static final String DEALER_READY = "Dealer is online.";
    
    // Game
    public static final String RPS = "Rock-Paper-Scissors";
    public static final String WINNER = "Winner";
    // If it is a tie every player falls into the category of others
    public static final String OTHERS = "Did Not Win";
    public static final String TIE = "Game Match Tied";
}
