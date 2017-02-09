# README#

## Coding Challenge: ***Casino*** ##
* Casino allows users to play the games online with a dealer executing each match.
* Rock-Paper-Scissors is pre-loaded as directed by the design specification.

## System Requirements ##
* JDK 1.8 or higher
* Git
* Apache Maven

## Running the Casino ##

To play at the casino follow these steps.
### Step 1: (Required) Install Dependencies ###
* Unzip the dependencies directory.
* Follow the instructions in the included *dependencies_readme.txt* to install the jars required to build the project.

### Step 2: (Optional) Implement a strategy for Rock-Paper-Scissors ###
* Implement the **com.tw.casino.game.GameStrategy** interface.
* Annotate your implementation class with the tag **@EmployStrategy** and place in under the same package or any sub-package. Do not place it any where else.
* For this implementation only the first implementation found reflectively in the class path is picked up and assigned as the strategy to be used.
* The *RandomGuessingRPSStrategy* described above has been currently annotated and may be used without change.

### Step 3: (Required) Build the project ###
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

### For questions, comments or concerns ###
* **Contact**: Siddhartha Sengupta