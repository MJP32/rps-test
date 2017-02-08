package com.tw.casino.util;

public class CasinoConstants
{
    public static final String AWAIT = "Initializing connection to Casino...";
    public static final String SERVER_READY = "Casino is live!";
    
    public static final String WELCOME = "Welcome to the Casino.";
    
    // Player set up
    public static final String PLAYER_START = "To begin, please enter your starting "
            + "account balance as a decimal number:";
    public static final String PLAYER_RE_ENTER = "Invalid amount, please enter a decimal value.";
    public static final String PLAYER_REDO = "Invalid choice. Please enter a choice as stated.";
    public static final String PLAYER_AWAIT = "Waiting for more players to join.";
    public static final String PLAYER_REJECT = "Request to play game has been denied.";
    public static final String ALLOW_STRATEGY = "Allowed";
    public static final String NO_STRATEGY = "Not Allowed";
    
    public static final String GAME_LIST_AVAILABLE = "You may choose from the following games.";
    
    // Dealer
    public static final String DEALER_READY = "Dealer is online.";
    
    // Game
    public static final String RPS = "Rock-Paper-Scissors";
    public static final String WINNER = "Winner";
    // If it is a tie every player falls into the category of others
    public static final String OTHERS = "Did Not Win";
    public static final String TIE = "Game Match Tied";
}
