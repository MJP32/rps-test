# README#

## Coding Challenge: ***Casino*** ##
* Casino allows users to play the games online with a dealer executing each match.
* Rock-Paper-Scissors is pre-loaded as directed by the design specification.

## System Requirements ##
* JDK 1.8 or higher
* Git
* Apache Maven

## Basic Architecture ##
There are three *actors* in the Casino.

* **CasinoManager**: Associated with a server instance, the CasinoManager is not meant for interaction with the user. It manages the forwarding of messages between the Dealer and Player and maintaining the House account. The CasinoManager starts up by loading available games from a GameDataLoader.

* **Dealer**: Handles requests for games, and is built to continually handle several requests simultaneously. When a dealer is brought online, it first requests Game Details which it then uses to initialize the games it has been allowed to offer. Based on the game results, it lets the CasinoManager how the payouts are to be made. Whenever a game ends in a tie, the Dealer sends the game payout to the CasinoManager for adding to the House account. 

* **Player**: These users are required to be started up with a starting balance and an implementation of GameStrategy that is reflectively loaded into their profile. When they request the CasinoManager to play a game, the entry fee is deducted from their account balance.

Interaction between these actors is driven by *messages* as described below.

* *GameListRequest, GameListResponse*: Used by the Player to get a list of available games and their entry fees. The GameListRequest is sent by a Player to the CasinoManager, and the GameListResponse from the CasinoManager to the Player.
* *GameDataRequest, GameDataResponse*: The Dealer employs these messages to initialize the game instances it is sent. The GameDataRequest is sent by a Dealer to the CasinoManager, and the GameDataResponse from the CasinoManager to the Dealer.
* *GameRequest*: A Player sends this message to the CasinoManager, who simply forwards it to an dealer assigned by it's discretion. For the purposes of the current implementation, the Dealer simply assigns the most recently registered Dealer to the GameRequest.
* *GameCompleteResponse*: This message is sent per player with their corresponding winnings.

The following messages are simply forwarded to the Player.
* *GameWaitResponse*: The is sent by the Dealer when more Players need to join for a game.
* *GameRejectResponse*: When a Player submits a fee less than the Game's entry fee.

Other *components*:

* **Game**: Along with a basic interface, the Rock-Paper-Scissors implementation is provided. For testing out the game play, two GameStrategy implementations are also present.
* *RandomGuessingRPSGameStrategy*: generates a random move every time.
* *SharpRPSGameStrategy*: Always plays 'scissors'.

* **GameDataLoader**: This loads up configured games and offers them to the CasinoManager at startup. While the current implementation simply has a hard-coded game loading up, it can potentially be implemented to load data from external files or databases. Since it is initialized only at startup, it does not affect the runtime performance of the Casino.

## Running the Casino ##

To play at the casino follow these steps.

### Step 1: Implement a strategy for Rock-Paper-Scissors ###
* Implement the **com.tw.casino.game.GameStrategy** interface.
* Annotate your implementation class with the tag **@EmployStrategy** and place in under the same package or any sub-package. Do not place it any where else.
* For this implementation only the first implementation found reflectively in the class path is picked up and assigned as the strategy to be used.
* The *RandomGuessingRPSStrategy* described above has been currently annotated and may be used without change.

### Step 2: Build the project ###
* Open up a terminal/shell or cmd window. 
* From the *casino* project directory run the following command.

```
#!command line

mvn install
```

* From the generated **target** directory you can start up each of the actors as shown.

```
#!command line

$ java -jar casino_server.jar <server_port>
$ java -jar casino_dealer.jar <server_host> <server_port>
$ java -jar casino_player.jar <server_host> <server_port> <starting_balance>
```
Once you have instantiated as server, you can instantiate any number of Dealers and Players. However, as described earlier only the most recently started Dealer will be asked to handle the Player's GameRequests.


## External libraries used ##
* [Netty](http://netty.io)
* [Reflections](https://github.com/ronmamo/reflections)
* [Maven Assembly Plugin](http://maven.apache.org/plugins/maven-assembly-plugin/)
* Related dependencies: Guava, Slf4j-API, Javassist.

### For questions, comments or concerns ###
* **Contact**: Siddhartha Sengupta (siddharths1787@gmail.com)