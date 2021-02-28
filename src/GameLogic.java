//Team name: NinjaAPY
//Team members: Yanni Qu (19415824), Patrick Keogh (19321326), Anamaria Andreian (19459304)

import java.util.Random;

public class GameLogic {
    Initialisation I;
    private final UIWindow uiWindow;
    private Player[] players;
    private final int[] playerOrder;
    private int numPlayers=6;
    private Player currPlayer;
    private int territoryCode;
    private int numTurns=1;
    public static String command; /*Maybe change to enum when all commands
                                    are known (will require changing return type
                                    of getCommand() function also).
                                    Creating a drop down menu is another option*/
    public GameLogic(){
        I=new Initialisation();
        I.initialisation();
        uiWindow=I.getUiWindow();
        players=I.getPlayers();
        playerOrder=I.getPlayerOrder();
    }

    public void game(){
        initialTroopPlacement();
        for(int i=0;numPlayers>1;i++){

            currPlayer=players[playerOrder[i]];
            uiWindow.displayString("Turn "+ numTurns);
            uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n");
            if(i<=1){
               turnPlayer();
            }else{
                turnNeutral();
            }
            if(i==numPlayers){
                i=-1;
            }
            numTurns++;

        }
        uiWindow.displayString("" + currPlayer.getPlayerName() +" has one the game!");
    }

    public void initialTroopPlacement(){
        for(int i=0;numTurns<54;i++){
            currPlayer=players[playerOrder[i]];
            uiWindow.displayString("Turn "+ numTurns );
            if(i<=1){
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 3 troops in a territory that you own\n");
                placeTroops(true);
            }else{
                uiWindow.displayString("" + currPlayer.getPlayerName() +" (" + Constants.PLAYER_COLOR_NAME[currPlayer.getPlayerCode()] + "), it is your turn\n\nYou must place 1 troop in a territory that you own\n");
                Random random =new Random();
                territoryCode=currPlayer.getPlayerTerritory(random.nextInt(currPlayer.getNumPlayerTerritories())).territoryCode;
                uiWindow.board.addUnits(territoryCode,1);
                currPlayer.addArmies(-1);
                uiWindow.displayString("" + currPlayer.getPlayerName() + " placed their troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "\n");
                uiWindow.displayMap();
            }

            if(i==numPlayers-1){
                i=-1;
            }
            numTurns++;
        }
    }

    public void turnPlayer(){
        currPlayer.addArmies(getTroops());
        placeTroops(false);
        while(currPlayer.getNumArmies()>0){
            uiWindow.displayString("You have " + currPlayer.getNumArmies() + " troops to place\n");
            placeTroops(false);
        }
        uiWindow.displayString("You have placed all your troops. It is now your attack phase.");
    }

    public void turnNeutral(){

    }

    //Maybe just use checkCommmand from initialisation class?
    //Recursive function to check if a player has entered a valid command
    public void checkCommand(String[] correctInputs) {
        boolean check=false;
        String msg = ("'" + correctInputs[0] + "'");
        for (int i = 0; i < correctInputs.length;i++) {
            if (command.equalsIgnoreCase(correctInputs[i])) {
                check=true;
            }
            if(i==correctInputs.length-1 && i!=0){
                msg+=(" or '" + correctInputs[i] + "'");
            }else if(i>1){
                msg+=(", " + correctInputs[i]);
            }
        }
        if (!check) {
            uiWindow.displayString("You must enter " + msg  + ". Please enter your command again\n");
            command = uiWindow.getCommand();
            checkCommand(correctInputs);
        }
    }

    //Recursive function to check if a player owns the entered territory
    public int checkHasTerritory() {
        boolean check = false;
        int territoryCode=0;
        for (int i = 0; i < currPlayer.getNumPlayerTerritories(); i++) {

            if (command.replaceAll(" ", "").substring(0,3).equalsIgnoreCase(currPlayer.getPlayerTerritory(i).territoryName.replaceAll(" ", "").substring(0,3))) {
                check = true;
                territoryCode=currPlayer.getPlayerTerritory(i).territoryCode;
            }
        }
        if (!check) {
            uiWindow.displayString("You do not own this territory. Please enter the name of another territory\n");
            GameLogic.command = uiWindow.getCommand();
            territoryCode=checkHasTerritory();
        }

        return territoryCode;
    }

    public static int diceRoll(){
        Random random =new Random();
        return random.nextInt(5)+1;
    }

    private void placeTroops(boolean initial){
        do{
            uiWindow.displayString("Please enter the name of the territory in which you wish to place your troops\n");
            command = uiWindow.getCommand();
            territoryCode = checkHasTerritory();
            uiWindow.displayString("Do you wish to place your troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to choose another territory\n");
            command = uiWindow.getCommand();
            checkCommand(new String[]{"YES", "NO"});
        } while (command.equalsIgnoreCase("NO"));

        int numTroops=3;
        if(!initial){
            do {
                uiWindow.displayString("Please enter the number of troops you wish to place: \n");
                numTroops = Integer.parseInt(uiWindow.getCommand());
                //Check has sufficient troops
                uiWindow.displayString("Do you wish to place " + numTroops + " troops in " + uiWindow.board.getTerritory(territoryCode).territoryName + "?\nEnter 'YES' to continue or 'NO' to change number of troops.\nYou may enter 'RETURN' to choose the territory again");
                command = uiWindow.getCommand();
                checkCommand(new String[]{"YES", "NO", "RETURN"});
            } while (command.equalsIgnoreCase("NO"));
        }
        if(!command.equalsIgnoreCase("RETURN")){
            uiWindow.board.addUnits(territoryCode,numTroops);
            currPlayer.addArmies(-numTroops);
            uiWindow.displayMap();
        }

    }

    private int getTroops(){
        int numTroops;
        String strForNumTroops="";
        numTroops= (int) Math.floor(currPlayer.getNumPlayerTerritories()/3);
        strForNumTroops +=("You received " + numTroops + " for holding " + currPlayer.getNumPlayerTerritories() + " territories");
        for(int i=0;i<Constants.NUM_CONTINENTS;i++){
            if(currPlayer.getNumTerritoriesInContinent(i)==Constants.CONTINENT_VALUES[0][i]){
                numTroops+=Constants.CONTINENT_VALUES[1][i];
                strForNumTroops+=("\nYou received " + Constants.CONTINENT_VALUES[1][i] + " for holding " + Constants.CONTINENT_NAMES[i]);
            }
        }
        if(numTroops<3){
            numTroops=3;
        }

        uiWindow.displayString("You have received " + numTroops + " in total");
        uiWindow.displayString(strForNumTroops);
        return numTroops;
    }

}
