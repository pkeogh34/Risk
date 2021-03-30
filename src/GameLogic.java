//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    public static final Board board = new Board();
    public static final UIWindow uiWindow = new UIWindow(board);
    public static final Player[] players = new Player[6];
    private static final Deck gameDeck = new Deck();//Territory card deck
    public static ArrayList<Integer> playerOrder = new ArrayList<>();//Arraylist of the playing order, containing player codes
    public static Player currPlayer;//The current player whose turn it is
    public static int territoryCode;//Holds the code of a territory
    private int numTurns=1;//Keeps track of the number turns
    public static int numSets=0;//Keeps track of the number of card sets turned in
    private static boolean getsCard;//True if the current player has conquered at least one territory
    public static String command;//Holds the command entered by the player


    public GameLogic(){
        //Initialised all the main data of the game
        Initialisation.initialisation(uiWindow,players,playerOrder);
        //Decides player order
        Initialisation.firstPlayer(uiWindow,players,playerOrder);
    }

    public void game(){
        initialTroopPlacement();//Runs the initial troop placement for the game
        for(int i = 0; !command.equals("GAME OVER"); i++){//Runs the loop until the game id finished i.e a player wins
            uiWindow.displayString("Turn "+ numTurns);//Prints turn number
            currPlayer=players[playerOrder.get(i)];//Changes current player
            uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n");
            if(i<=1){//Human Player
                turnPlayer();
            }else{//Neutral Player
                currPlayer.addArmies(getTroops());//Gives the number of troops earned to the current player
                while(currPlayer.getNumArmies()>0) {//Loops until all armies are placed
                    Random random =new Random();
                    int troops;
                    if(currPlayer.getNumArmies()==1){
                        troops=1;
                    }else{
                        //Gets a random number of troops
                        troops=(random.nextInt(currPlayer.getNumArmies()-1)+1);
                    }
                    randTroopPlacement(troops);//Random places the neutral players troops
                }
                uiWindow.displayString("" + currPlayer.getPlayerName() + " has placed all their troops\n");
            }
            if(i==playerOrder.size()-1){//Resets turn loop
                i=-1;
            }
            numTurns++;//Increases number of turns
        }
        //Prints who has won the game
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has won the game!");
    }

    public void initialTroopPlacement(){//Function to place initial troops
        for(int i=0;numTurns<=54;i++){
            currPlayer=players[playerOrder.get(i)];
            uiWindow.displayString("Turn "+ numTurns );
            if(i<=1){//Human Player
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 3 troops in a territory that you own\n");
                //placeTroops(true);
                randTroopPlacement(3);
            }else{//Neutral players
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()]+ "), it is your turn\n\nYou must place 2 troops in a territory that you own\n");
                randTroopPlacement(2);
            }

            if(i==playerOrder.size()-1){//Resets loop
                i=-1;
            }
            numTurns++;//Adds one to the number of turns
        }
    }

    public void turnPlayer(){//Function to execute player turns
       //Deploy phase
        Deploy.deploy();

        //Attack phase
        uiWindow.displayString("You have placed all your troops. It is now your attack phase.\nPlease enter 'CONTINUE' to attack with your troops or 'SKIP' to skip the Attack phase. You may also enter 'SKIP' at any time to move to the Fortify phase\n");
        Checks.checkCommand(new String[]{"CONTINUE", "SKIP"});//Checks if player wishes to continue attack or move to the fortify phase
        while(command.equals("CONTINUE")){
            Attack.attack();//Executes the attack functionality for the player
            if(command.equals("GAME OVER")){//Returns if the game is over
                return;
            }
            if(command.equals("SKIP")){//Moves to the fortify phase
                break;
            }
            uiWindow.displayString("Please enter 'CONTINUE' to continue attacking with your troops or 'SKIP' to move to the Fortify phase\n");
            Checks.checkCommand(new String[]{"CONTINUE", "SKIP"});
        }

        //Draw a card if a territory was conquered
        if(gameDeck.getCardPile().size()!=0){
            if(getsCard){
                getsCard=false;
                uiWindow.displayString("You have received a territory card for successfully conquering a territory");
                currPlayer.addTerritoryCard(gameDeck.drawCard());
            }
        }

        //Fortify phase
        uiWindow.displayString("It is now your fortify phase.\nPlease enter 'CONTINUE' to fortify one of your territories or 'END' to skip the Fortify phase and end your turn\n");
        Checks.checkCommand(new String[]{"CONTINUE", "END"});//Checks if user wishes to end their turn or continue
        if(command.equals("CONTINUE")){
            Fortify.fortify();//Executes functionality for players fortify phase
        }
    }

    public void randTroopPlacement(int troops){
        Random random =new Random();//Creates an instance of the random class
        //Gets a random territory owned by the player
        territoryCode=currPlayer.getPlayerTerritory(random.nextInt(currPlayer.getNumPlayerTerritories())).territoryCode;
        //Adds the unit to that territory
        board.addUnits(territoryCode,troops);
        currPlayer.addArmies(-troops);//Removes the troops from the players reserves
        //Displays where the troops were placed
        if(troops==1){
            uiWindow.displayString("" + currPlayer.getPlayerName() + " placed " + troops + " troop in " + board.getTerritory(territoryCode).territoryName + "\n");
        }else {
            uiWindow.displayString("" + currPlayer.getPlayerName() + " placed " + troops + " troops in " + board.getTerritory(territoryCode).territoryName + "\n");
        }
        uiWindow.displayMap();//Refreshes map
    }

    //Function for simulating a dice roll
    public static int diceRoll(){
        Random random =new Random();
        return random.nextInt(5)+1;
    }

    //Function that allows human player to place troops
    public static void placeTroops(boolean initial){
        do{//Gets the territory where the player wishes to place there troops
            uiWindow.displayString("Please enter the name of the territory in which you wish to place your troops\n");
            command = uiWindow.getCommand();//Get player command
            territoryCode = Checks.checkHasTerritory(1);//Checks they own the territory
            uiWindow.displayString("Do you wish to place your troops in " + board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
            Checks.checkCommand(new String[]{"YES", "NO"});//Double checks the player has selected the right territory
        } while (command.equals("NO"));

        int numTroops=3;
        if(!initial) {//Functionality for non-initial troops placement
            do {
                uiWindow.displayString("Please enter the number of troops you wish to place: \n");
                command = uiWindow.getCommand();
                numTroops = Checks.checkNumber(1);
                uiWindow.displayString("Do you wish to place " + numTroops + " troops in " + board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\nYou may enter 'RETURN' to choose the territory again\n");
                Checks.checkCommand(new String[]{"YES", "NO", "RETURN"});
                if (command.equals("RETURN")) {
                    placeTroops(false);
                    return;
                }
            } while (command.equals("NO"));
        }
        //adds troops to the board from player reserves
        board.addUnits(territoryCode,numTroops);
        currPlayer.addArmies(-numTroops);
        uiWindow.displayMap();//refreshes map
    }

    //Function to get the number of troops that a player earns in a turn
    public static int getTroops(){
        int numTroops;//Holds number of troops
        StringBuilder strForNumTroops= new StringBuilder();//Creates string to tell player how many troops were earned and why
        numTroops= (int) Math.floor(currPlayer.getNumPlayerTerritories()/3.0);//Gets number of troops from territories
        strForNumTroops.append("You received ").append(numTroops).append(" troops for holding ").append(currPlayer.getNumPlayerTerritories()).append(" territories\n");
        //Checks if the player will receive troops for holding full continents
        for(int i=0;i<Constants.NUM_CONTINENTS;i++){
            if(currPlayer.getNumTerritoriesInContinent(i)==Constants.CONTINENT_VALUES[0][i]){
                numTroops+=Constants.CONTINENT_VALUES[1][i];
                strForNumTroops.append("You received ").append(Constants.CONTINENT_VALUES[1][i]).append(" troops for holding ").append(Constants.CONTINENT_NAMES[i]).append("\n");
            }
        }

        if((currPlayer.getNumPlayerTerritories()<9&&numTroops<3)) {
            numTroops=3;
            uiWindow.displayString("You have received " + numTroops + " troops in total\n");
        }else{
            uiWindow.displayString("You have received " + numTroops + " troops in total\n");
            uiWindow.displayString(strForNumTroops.toString());
        }
        return numTroops;//returns number of troops earned
    }

    public static void setGetsCard(boolean getsCard) {
        GameLogic.getsCard = getsCard;
    }
}
