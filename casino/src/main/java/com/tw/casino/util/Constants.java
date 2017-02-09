package com.tw.casino.util;

public class Constants
{
    public static final String STARTUP_SERVER = "Provide port as: server <port>";
    public static final String STARTUP_DEALER = "Provide host and port as: dealer <host> <port>";
    public static final String STARTUP_PLAYER = "Provide host and port as: player <host> <port> <starting_balance>";
    
    // Dealer
    public static final String DEALER_READY = "Dealer is online.";
    public static final String WELCOME = "Welcome to the Casino.";
    
    // Player
    public static final String START = "To begin, please enter your starting "
            + "account balance as a decimal number:";
    public static final String RE_ENTER = "Invalid amount, please enter a decimal value.";
    public static final String GAME_LIST_AVAILABLE = "You may choose from the following games.";
    public static final String REDO = "Invalid choice. Please enter a choice as stated.";
    public static final String AWAIT = "Waiting for more players to join.";
    public static final String REJECT = "Request to play game has been denied.";
    public static final String INSUFFICIENT_FUNDS = 
            "No more funds to play. Please update your balance";
    public static final String CONGRATULATIONS = "Congratulations! You win $";
    public static final String REGRET = "Sorry! You didn't win this time.";
    public static final String BALANCE_ADD = 
            "Enter the amount you wish to add as a decimal number: ";
    public static final String BALANCE = "Account Balance: " ;
    
    // Game
    public static final String GAME_LOCATION = "com.tw.casino.game";
    public static final String RPS = "Rock-Paper-Scissors";
    public static final String WINNER = "Winner";
    public static final String OTHERS = "Did Not Win";
    public static final String TIE = "Game Match Tied";
    
    // Game Strategy Names
    public static final String ALWAYS_SHARP = "Always Sharp";
    public static final String RANDOM_GUESSING = "Random Guessing";
}
