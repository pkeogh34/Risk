//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.ArrayList;
import java.util.Random;

public class GameLogic {
    public static final Board board = new Board();
    public static final UIWindow uiWindow = new UIWindow(board);
    public static final Player[] players = new Player[6];
    public static ArrayList<Integer> playerOrder = new ArrayList<>();//Arraylist of the playing order, containing player codes
    public static Player currPlayer;//The current player whose turn it is
    public static int territoryCode;//Holds the code of a territory
    private int numTurns=1;//Keeps track of the number turns
    private int numSets=0;//Keeps track of the number of card sets turned in
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
        //Prints who ahs one the game
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has one the game!");
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
        currPlayer.addArmies(getTroops());//Gives the current player the number of troops that they have earned
        placeTroops(false);//Allows the current player to place their troops
        while(currPlayer.getNumArmies()>0){//Loops until the player has placed all there troops
            uiWindow.displayString("You have " + currPlayer.getNumArmies() + " troops to place\n");
            placeTroops(false);
        }

        //Attack phase
        uiWindow.displayString("You have placed all your troops. It is now your attack phase.\nPlease enter 'CONTINUE' to attack with your troops or 'SKIP' to skip the Attack phase. You may also enter 'SKIP' at any time to move to the Fortify phase\n");
        checkCommand(new String[]{"CONTINUE", "SKIP"});//Checks if player wishes to continue attack or move to the fortify phase
        while(command.equals("CONTINUE")){
            Attack.attack();//Executes the attack functionality for the player
            if(command.equals("GAME OVER")){//Returns if the game is over
                return;
            }
            if(command.equals("SKIP")){//Moves to the fortify phase
                break;
            }
            uiWindow.displayString("Please enter 'CONTINUE' to continue attacking with your troops or 'SKIP' to move to the Fortify phase\n");
            checkCommand(new String[]{"CONTINUE", "SKIP"});
        }

        //Fortify phase
        uiWindow.displayString("It is now your fortify phase.\nPlease enter 'CONTINUE' to fortify one of your territories or 'END' to skip the Fortify phase and end your turn\n");
        checkCommand(new String[]{"CONTINUE", "END"});//Checks if user wishes to end their turn or continue
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
    private void placeTroops(boolean initial){
        do{//Gets the territory where the player wishes to place there troops
            uiWindow.displayString("Please enter the name of the territory in which you wish to place your troops\n");
            command = uiWindow.getCommand();//Get player command
            territoryCode = checkHasTerritory(1);//Checks they own the territory
            uiWindow.displayString("Do you wish to place your troops in " + board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
            checkCommand(new String[]{"YES", "NO"});//Double checks the player has selected the right territory
        } while (command.equals("NO"));

        int numTroops=3;
        if(!initial) {//Functionality for non-initial troops placement
            do {
                uiWindow.displayString("Please enter the number of troops you wish to place: \n");
                command = uiWindow.getCommand();
                numTroops = checkNumber(1);
                uiWindow.displayString("Do you wish to place " + numTroops + " troops in " + board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\nYou may enter 'RETURN' to choose the territory again\n");
                checkCommand(new String[]{"YES", "NO", "RETURN"});
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
    private int getTroops(){
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

    //Recursive function to check if a player has entered a valid command
    public static void checkCommand(String[] correctInputs) {
        command= uiWindow.getCommand();//Get player command
        boolean check=false;//Check to see if command is correct
        StringBuilder msg = new StringBuilder(("'" + correctInputs[0] + "'"));//Creates a String to hold the correct inputs
        for (int i = 0; i < correctInputs.length;i++) {
            if(command.length()>=correctInputs[i].length()) {
                if (command.substring(0,correctInputs[i].length()).equalsIgnoreCase(correctInputs[i])){
                    command=correctInputs[i];
                    check=true;
                }
            }
            if(i==correctInputs.length-1 && i!=0){
                msg.append(" or '").append(correctInputs[i]).append("'");
            }else if(i>=1){
                msg.append(", ").append(correctInputs[i]);
            }
        }

        //Switch statement for various commands that allow a player to end a phase early or stop attacking
        switch (command) {
            case "SKIP" -> {//Moves turn from attack phase to fortify phase
                uiWindow.displayString("Are sure you wish to move to the Fortify phase?\nEnter 'YES' to move to the Fortify phase or 'NO' to continue Attack phase\n");
                checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "SKIP";
                } else {
                    command = "CONTINUE";
                }
                return;
            }
            case "STOP" -> {//Stops an attack
                uiWindow.displayString("Are you sure you wish to stop attacking?\nEnter 'YES' to stop attacking or 'NO' to continue attacking\n");
                checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "STOP";
                } else {
                    command = "CONTINUE";
                }
                return;
            }
            case "END" -> {//Ends the players turn
                uiWindow.displayString("Are you sure you wish to end your turn?\nEnter 'YES' to end your turn or 'NO' to continue Fortify phase\n");
                checkCommand(new String[]{"YES", "NO"});
                if (command.equals("YES")) {
                    command = "END";
                } else {
                    command = "CONTINUE";
                }
                return;
            }
        }

        //Displays the correct inputs that the player must enter and asks them to enter a command again
        if (!check&&!correctInputs[0].equals("SKIP")&&!correctInputs[0].equals("END")){
            uiWindow.displayString("You must enter " + msg.toString()  + ". Please enter your command again\n");
            checkCommand(correctInputs);//Checks the new command
        }
    }

    //Recursive function to check if a player owns the entered territory
    public static int checkHasTerritory(int checkType) {
        boolean check = false;
        int territoryCode=0,cutOff=4;
        if(command.length()<4){
            cutOff=command.length();
            if(cutOff==3 && command.substring(0,cutOff).equalsIgnoreCase("gre")){
                cutOff=2;
            }
        }

        for (int i = 0; i < Constants.COUNTRY_NAMES.length; i++) {
            if (command.substring(0,cutOff).equalsIgnoreCase(Constants.COUNTRY_NAMES[i].replaceAll(" ", "").substring(0,cutOff))) {
               command=Constants.COUNTRY_NAMES[i];
               check=true;
            }
        }

        if(cutOff<=2) {
            check=false;
        }
        if(!check){
            uiWindow.displayString("You must enter a valid territory name. Please try again\n");
            command = uiWindow.getCommand();
            territoryCode = checkHasTerritory(checkType);
        }

        check=false;
        for (int i = 0; i < currPlayer.getNumPlayerTerritories(); i++) {
            if (command.equalsIgnoreCase(currPlayer.getPlayerTerritory(i).territoryName)) {
                check = true;
                territoryCode=currPlayer.getPlayerTerritory(i).territoryCode;
            }
        }

        if (!check&&checkType==1) {//Tells the player that they do not own the territory and must select another one
            uiWindow.displayString("You do not own " + command + ". Please enter the name of another territory\n");
            command = uiWindow.getCommand();
            territoryCode=checkHasTerritory(1);
        }else if(check&&checkType==2){//Tells the player that they do not own the territory and must select another one (for attacking)
            uiWindow.displayString("You already own " + command + ". Please enter the name of another territory\n");
            command = uiWindow.getCommand();
            territoryCode=checkHasTerritory(2);
        }

        //If a territory that a player does not own is required,  this searches all territories
        if(checkType==2){
            for (int i = 0; i < Constants.COUNTRY_NAMES.length; i++) {
                if (command.equalsIgnoreCase(Constants.COUNTRY_NAMES[i])) {
                    territoryCode=i;
                }
            }
        }

        return territoryCode;//Returns the code of the territory
    }

    //Recursive function to check if the number supplied by the player is valid
    public static int checkNumber(int numType){
        int number;
        //Checks if the String entered is indeed a number
        try{
            Integer.parseInt(command);
        }catch(Exception e){
            //If not a number, ask the player to enter another number
            uiWindow.displayString("You must enter a number. Please try again\n");
            command=uiWindow.getCommand();
            checkNumber(numType);
        }

        //Converts the String to an int
        number=Integer.parseInt(command);

        if(number<=0){//If not less than 0, ask the player to enter another number
            uiWindow.displayString("You must enter a number greater than 0. Please try again\n");
            command=uiWindow.getCommand();
            number=checkNumber(numType);
        }

        if(numType==1){//If placing troops
            if(number>currPlayer.getNumArmies()){//If more troops than player has in reserve, ask the player to enter another number
                uiWindow.displayString("You do not have that many troops. Please try again\n");
                command=uiWindow.getCommand();
                number=checkNumber(numType);
            }
        }else if(numType==2){//If rolling dice
            if(number>3){//If greater than 3, ask the player to enter another number
                do {
                    uiWindow.displayString("The maximum number of dice that can be rolled is 3. Please try again\n");
                    checkCommand(new String[]{"SKIP"});
                }while(command.equals("CONTINUE"));
            }else if(number>=board.getNumUnits(territoryCode)){//If greater than or equal to number of troops in the territory, ask the player to enter another number
                do {
                    uiWindow.displayString("You can only roll " + (board.getNumUnits(territoryCode) - 1) + " dice. Please try again\n");
                    checkCommand(new String[]{"SKIP"});
                }while(command.equals("CONTINUE"));
            }
            if(command.equals("SKIP")){//Returns -1 if the player wishes to skip attack phase
                return -1;
            }
            number=checkNumber(numType);
        }else if(number==board.getTerritory(territoryCode).numOccupyingArmies){//If equal to number of troops in the territory, ask the player to enter another number
            uiWindow.displayString("You must leave at least one troop in your territory at all times\n");
            command=uiWindow.getCommand();
            number=checkNumber(numType);
        }else if(number>board.getTerritory(territoryCode).numOccupyingArmies){//If greater than the number of troops in the territory, ask the player to enter another number
            uiWindow.displayString("You do not have that many troops to transfer\n");
            command=uiWindow.getCommand();
            number=checkNumber(numType);
        }else if(numType>3) {//Tell the player the minimum number of troops they must transfer based on the last number of dice they rolled
            if (number < (numType - 4)) {
                if ((numType - 4) == 1) {
                    uiWindow.displayString("You must transfer at least " + (numType - 4) + " troop");
                } else {
                    uiWindow.displayString("You must transfer at least " + (numType - 4) + " troops");
                }
                command = uiWindow.getCommand();
                number = checkNumber(numType);
            }
        }

        return number;//returns the validated number
    }

    //Recursive function to check the two territories are adjacent
    public static boolean checkAdjacent(int territory1, int territory2, int checkType) {
        boolean check = false;
        for (int i = 0; i < Constants.ADJACENT[territory1].length; i++) {
            if (Constants.ADJACENT[territory1][i] == territory2) {
                check=true;
                break;
            }
        }

        //If territories must be adjacent
        if(!check&&checkType==1) {
            uiWindow.displayString("These territories are not adjacent. Please enter the name of another territory\n");
            command = uiWindow.getCommand();
            territoryCode=checkHasTerritory(2);
            checkAdjacent(territory1,territoryCode,1);//maybe remove territory2
        }else return check || checkType != 2;//If territories need not be adjacent

        return true;
    }

    //Recursive to find if there is a valid path between the territories a player wishes to move troops between
    public static boolean checkHasValidPath(int territory1,int territory2,ArrayList<Territory> playerTerritories){
        int i=0;
        /*Checks all the paths through a players owned territories to check if there
        is path to the destination. Each territory is only visited once*/
        while(playerTerritories.size()>0&&i<playerTerritories.size()){
            if(checkAdjacent(territory1,territory2,2)){//If has a valid path
                return true;
            }

            if(checkAdjacent(territory1,playerTerritories.get(i).territoryCode,2)){
                territory1=playerTerritories.get(i).territoryCode;
                playerTerritories.remove(i);//Removes the territory once it is visited
                i--;
                //Recursive call
                if(checkHasValidPath(territory1,territory2,playerTerritories)){
                    return true;
                }
            }
            i++;
        }
        return false;
    }
}
